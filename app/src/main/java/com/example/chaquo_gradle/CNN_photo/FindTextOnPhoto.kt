package com.example.chaquo_gradle.CNN_photo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream

fun findTextOnPhoto(context: Context) {
    val downloadDir =
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
    val photoFiles = File(downloadDir, "Фото")
    photoFiles.listFiles()?.forEach { file ->
        val path = File(photoFiles.toString(), file.name).absolutePath
        Log.d("PATH", path)
        if (!File(photoFiles.toString(), file.name).exists()) {
            showToastFileNotExist(
                context,
                "$path не существует"
            )
        } else {
            try {
                val bitmap = BitmapFactory.decodeStream(
                    File(path).inputStream()
                )
            } catch (e: java.lang.Exception) {
                Log.d("ERROR", "Photo file don't open")
            }
            val predict = photoCNN(context, File(photoFiles.toString(), file.name).absolutePath)
            showToastFileNotExist(context, predict[0].toString())
            if (predict[0] > 0.5f) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder
                    .setTitle("Внимание!")
                    .setMessage("На фото ${file.name} обнаружет текст, нужна проверка на наличие личной информации")
                    .setPositiveButton("Понял") { dialog, which ->

                    }
                val dialog: AlertDialog = builder.create()
                dialog.show()
//                    file.copyTo(
//                        File(
//                            File(this.filesDir, "my_files"),
//                            file.name
//                        )
//                    ) // переносим файлы с расширением фотографий в новую папку
//                    file.delete() // удаляем исходный файл
            }
        }
    }
}
fun photoCNN(context: Context, pathPhoto:String):FloatArray{
    return try {
        val model: Module? = /*LiteModuleLoader*/Module.load(assetFilePath(context, "model_cnn_photo_CPU_script.pt"))//"model_cnn_photo_CPU_script.pt"))//modelCNNphoto.ptl
//                "source_NN_models/model_cnn_photo_classification_CPU_script.ptl")
//                "res/source_NN_models/model_cnn_photo_classification_CPU_script.pt")//"/data/data/com.example.chaquo_gradle/files/chaquopy/AssetFinder/app/source_NN_models/cnnCPUscript.pt")//"python/source_NN_models/cnnCPUscript.pt")

        val bitmap = BitmapFactory.decodeStream(File(pathPhoto).inputStream())
        val resizedBitmap = resizeBitmap(bitmap, 200, 200)
        val inputTensor: Tensor = TensorImageUtils.bitmapToFloat32Tensor(
            resizedBitmap,
            TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB
        )
        val outputTensor = model!!.forward(IValue.from(inputTensor)).toTensor()
        val outputData = outputTensor.dataAsFloatArray
        for (element in outputData) {
            println(element)
            Log.i("RETURN", element.toString())
        }

//            outputTensor.
        return outputTensor.dataAsFloatArray
    }
    catch (e: Exception){
        Log.d("Error CNN", e.toString())
        e.toString()
        return FloatArray(1)
    }
}
fun assetFilePath(context: Context, assetName: String?): String? {
    val file = File(context.filesDir, assetName)
    if (file.exists() && file.length() > 0) {
        return file.absolutePath
    }
    context.assets.open(assetName!!).use { `is` ->
        FileOutputStream(file).use { os ->
            val buffer = ByteArray(4 * 1024)
            var read: Int
            while (`is`.read(buffer).also { read = it } != -1) {
                os.write(buffer, 0, read)
            }
            os.flush()
        }
        return file.absolutePath
    }
}
private fun resizeBitmap(originalBitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
    return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
}
fun showToastFileNotExist(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}