package com.example.chaquo_gradle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.io.File
import java.io.FileOutputStream

fun sortFilesInDownloadDirectory() {
    val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val files = downloadDir.listFiles() ?: return

    files.forEach { file ->
        val targetDir = when (file.extension.lowercase()) {
            in arrayOf("mp4", "avi", "mkv") -> File(downloadDir, "Видео")
            in arrayOf("jpg", "png", "gif") -> File(downloadDir, "Фото")
            in arrayOf("txt", "doc", "docx", "pdf") -> File(downloadDir, "Текстовые документы")
            in arrayOf("mp3", "wav", "flac") -> File(downloadDir, "Аудио")
            else -> return@forEach
        }
        if (!targetDir.exists()) {
            targetDir.mkdirs()
        }
        file.renameTo(File(targetDir, file.name))
    }
}
//fun findTextOnPhoto(context :Context){
//    val photoFiles = File(downloadDir, "photos")
//    photoFiles.listFiles()?.forEach { file ->
//        val path =  File(photoFiles.toString(), file.name).absolutePath
//        Log.d("PATH", path)
//        if (!File(photoFiles.toString(), file.name).exists()) {
//            text.text =
//                "$path не существует"
//        } else {
//            try {
//                val bitmap = BitmapFactory.decodeStream(
////                            assets.open(path)
//                    File(path).inputStream()
//                )
////                val imgView = findViewById<ImageView>(R.id.imageView)
////                imgView.setImageBitmap(bitmap)
//            }
//            catch (e: java.lang.Exception){
//                Log.d("ERROR", "Photo file don't open")
//            }
//            val predict = CNN(File(photoFiles.toString(), file.name).absolutePath)
//            text.text = predict[0].toString()
//            if (predict[0] > 0.5f) {
//                file.copyTo(
//                    File(
//                        File(this.filesDir, "my_files"),
//                        file.name
//                    )
//                ) // переносим файлы с расширением фотографий в новую папку
//                file.delete() // удаляем исходный файл
//            }
////                }
//        }
//        getListFromMyFiles()
//    }
//}

//fun CNN(context: Context, pathPhoto:String):FloatArray{
//    return try {
//        val model: Module? = /*LiteModuleLoader*/Module.load(assetFilePath(context, "model_cnn_photo_CPU_script.pt"))//"model_cnn_photo_CPU_script.pt"))//modelCNNphoto.ptl
////                "source_NN_models/model_cnn_photo_classification_CPU_script.ptl")
////                "res/source_NN_models/model_cnn_photo_classification_CPU_script.pt")//"/data/data/com.example.chaquo_gradle/files/chaquopy/AssetFinder/app/source_NN_models/cnnCPUscript.pt")//"python/source_NN_models/cnnCPUscript.pt")
//
//        val bitmap = BitmapFactory.decodeStream(File(pathPhoto).inputStream())
//        val resizedBitmap = resizeBitmap(bitmap, 200, 200)
//        val inputTensor: Tensor = TensorImageUtils.bitmapToFloat32Tensor(
//            resizedBitmap,
//            TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB
//        )
//        val outputTensor = model!!.forward(IValue.from(inputTensor)).toTensor()
//        val outputData = outputTensor.dataAsFloatArray
//        for (element in outputData) {
//            println(element)
//            Log.i("RETURN", element.toString())
//        }
//
////            outputTensor.
//        return outputTensor.dataAsFloatArray
//    }
//    catch (e: Exception){
//        Log.d("Error CNN", e.toString())
//        e.toString()
//        return FloatArray(1)
//    }
//}
//fun assetFilePath(context: Context, assetName: String?): String? {
//    val file = File(context.filesDir, assetName)
//    if (file.exists() && file.length() > 0) {
//        return file.absolutePath
//    }
//    context.assets.open(assetName!!).use { `is` ->
//        FileOutputStream(file).use { os ->
//            val buffer = ByteArray(4 * 1024)
//            var read: Int
//            while (`is`.read(buffer).also { read = it } != -1) {
//                os.write(buffer, 0, read)
//            }
//            os.flush()
//        }
//        return file.absolutePath
//    }
//}
//private fun resizeBitmap(originalBitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
//    return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
//}

//fun sortFileInDownload(){
//    val downloadDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
//    val videoDir = File(downloadDir, "Видео")
//    val photoDir = File(downloadDir, "Фото")
//    val textDir = File(downloadDir, "Текстовые документы")
//    val audioDir = File(downloadDir, "удио")
//
//    if (!videoDir.exists()) {
//        videoDir.mkdirs()
//    }
//    if (!photoDir.exists()) {
//        photoDir.mkdirs()
//    }
//    if (!textDir.exists()) {
//        textDir.mkdirs()
//    }
//    if (!audioDir.exists()) {
//        audioDir.mkdirs()
//    }
//
//    val files = downloadDir.listFiles()
//
//    files?.forEach { file ->
//        val extension = file.extension
//
//        when {
//            extension.equals("mp4", ignoreCase = true) || extension.equals("avi", ignoreCase = true) || extension.equals("mkv", ignoreCase = true) -> {
//                file.copyTo(File(videoDir, file.name), true)
//                file.delete()
//            }
//            extension.equals("jpg", ignoreCase = true) || extension.equals("png", ignoreCase = true) || extension.equals("gif", ignoreCase = true) -> {
//                file.copyTo(File(photoDir, file.name), true)
//                file.delete()
//            }
//            extension.equals("txt", ignoreCase = true) || extension.equals("doc", ignoreCase = true) || extension.equals(
//                "docx",
//                ignoreCase = true
//            ) -> {
//                file.copyTo(File(textDir, file.name), true)
//                file.delete()
//            }
//            extension.equals("mp3", ignoreCase = true) || extension.equals("wav", ignoreCase = true) || extension.equals("flac", ignoreCase = true) -> {
//                file.copyTo(File(audioDir, file.name), true)
//                file.delete()
//            }
//        }
//    }
//}
