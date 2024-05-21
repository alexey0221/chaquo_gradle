package com.example.chaquo_gradle

import FileAdapter
import FileItem
import PermissionUtils
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquo_gradle.CNN_Text.doInference
import com.example.chaquo_gradle.CNN_Text.loadModelFile
import com.example.chaquo_gradle.CNN_Text.textCNN
import com.example.chaquo_gradle.CNN_photo.findTextOnPhoto
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context


class MainActivity : AppCompatActivity() {

    private lateinit var fileAdapter: FileAdapter
    private val PERMISSION_STORAGE = 101
    private val downloadDir =
        File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
    private lateinit var tflite: Interpreter
    private lateinit var tflitemodel : ByteBuffer
    private lateinit var localFiles : File//(this.filesDir.absolutePath)

    @SuppressLint("SetTextI18n")
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initPython()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        localFiles = File(this.filesDir.absolutePath)
//        var mFilesDir = File(this.filesDir, "my_files")
        val text = findViewById<TextView>(R.id.textView)
        if (!PermissionUtils.hasPermissions(this)) {
            PermissionUtils.requestPermissions(this, PERMISSION_STORAGE);
//            text.text =
        }
        val myFilesDir = File(this.filesDir, "my_files")
        if (!myFilesDir.exists()) {
            // Create the directory
            myFilesDir.mkdir()
        }
        val files = myFilesDir.listFiles()?.map { FileItem(it.name) } ?: emptyList()
        fileAdapter = FileAdapter(files)
        val recyclerViewFiles = findViewById<RecyclerView>(R.id.recyclerViewFiles)
        recyclerViewFiles.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = fileAdapter
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle("Внимание!")
            .setMessage("Ваши файлы в папке \"Donwload\" будут отсортированны по папкам " +
                    "(Видео, Фото, Текстовые документы, Аудио)")
            .setPositiveButton("Понял") { dialog, which ->
                sortFilesInDownloadDirectory()
            }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        getListFromMyFiles()

        val sortText = findViewById<Button>(R.id.sort_text)
        sortText.setOnClickListener {
//            text.text = assetFilePath(this, "model_cnn_photo_CPU_script.pt") //modelCNNphoto.ptl
//
//            context.getAsset()
            val modelFile = "model_text_10_6.tflite"
            tflitemodel = loadModelFile(this.assets, modelFile)
            tflite = Interpreter(tflitemodel)
            textCNN(this, tflite)
            getListFromMyFiles()
        }


//        val toGetText = findViewById<Button>(R.id.toGetText)
//        toGetText.setOnClickListener{
//            val intent = Intent(this, ActivityGetText::class.java)
//            startActivity(intent)
//        }
        val sortImg = findViewById<Button>(R.id.sort_img)
        sortImg.setOnClickListener {
//            val photoFiles = File(downloadDir, "photos")
//            photoFiles.listFiles()?.forEach { file ->
//                val path =  File(photoFiles.toString(), file.name).absolutePath
//                Log.d("PATH", path)
//                if (!File(photoFiles.toString(), file.name).exists()) {
//                    text.text =
//                        "$path не существует"
//                } else {
//                    try {
//                        val bitmap = BitmapFactory.decodeStream(
////                            assets.open(path)
//                            File(path).inputStream()
//                        )
//                        val imgView = findViewById<ImageView>(R.id.imageView)
//                        imgView.setImageBitmap(bitmap)
//                    }
//                    catch (e: java.lang.Exception){
//                        Log.d("ERROR", "Photo file don't open")
//                    }
//                    val predict = CNN(File(photoFiles.toString(), file.name).absolutePath)
//                    text.text = predict[0].toString()
//                    if (predict[0] > 0.5f) {
//                        file.copyTo(
//                            File(
//                                File(this.filesDir, "my_files"),
//                                file.name
//                            )
//                        ) // переносим файлы с расширением фотографий в новую папку
//                        file.delete() // удаляем исходный файл
//                    }
////                }
//                }
            findTextOnPhoto(this)
            getListFromMyFiles()
        }
    }
//    }
//    private fun initPython() {
//        if (!Python.isStarted()) {
//            Python.start(AndroidPlatform (this));
//        }
//    }
//    private fun getHelloPy(name:String):String{ // Получение функции написанной на Python
//        val python = Python.getInstance()
//        val pythonFile = python.getModule("test")
//        return pythonFile.callAttr("test", name).toString()
//    }

//    private fun findTextOnPhoto(){
//        val photoFiles = File(downloadDir, "Фото")
//        photoFiles.listFiles()?.forEach { file ->
//            val path =  File(photoFiles.toString(), file.name).absolutePath
//            Log.d("PATH", path)
//            if (!File(photoFiles.toString(), file.name).exists()) {
//                showToastFileNotExist(
//                    "$path не существует")
//            } else {
//                try {
//                    val bitmap = BitmapFactory.decodeStream(
//                        File(path).inputStream()
//                    )
//                }
//                catch (e: java.lang.Exception){
//                    Log.d("ERROR", "Photo file don't open")
//                }
//                val predict = CNN(this, File(photoFiles.toString(), file.name).absolutePath)
//                showToastFileNotExist(predict[0].toString())
//                if (predict[0] > 0.5f) {
//                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//                    builder
//                        .setTitle("Внимание!")
//                        .setMessage("На фото ${file.name} обнаружет текст, нужна проверка на наличие личной информации")
//                        .setPositiveButton("Понял") { dialog, which ->
//
//                        }
//                    val dialog: AlertDialog = builder.create()
//                    dialog.show()
////                    file.copyTo(
////                        File(
////                            File(this.filesDir, "my_files"),
////                            file.name
////                        )
////                    ) // переносим файлы с расширением фотографий в новую папку
////                    file.delete() // удаляем исходный файл
//                }
//            }
//        }
//    }
    fun getListFromMyFiles(){
        val myFilesDir = File(localFiles, "my_files")
        val files = myFilesDir.listFiles()?.map { FileItem(it.name) } ?: emptyList()
        // Формирование RecicleView и вывод его содержимого
        fileAdapter = FileAdapter(files)
        val recyclerViewFiles = findViewById<RecyclerView>(R.id.recyclerViewFiles)
        recyclerViewFiles.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = fileAdapter
        }
    }
//    private fun CNN(pathPhoto:String):FloatArray{
//        return try {
//            val model: Module? = /*LiteModuleLoader*/Module.load(assetFilePath(this, "model_cnn_photo_CPU_script.pt"))//"model_cnn_photo_CPU_script.pt"))//modelCNNphoto.ptl
////                "source_NN_models/model_cnn_photo_classification_CPU_script.ptl")
////                "res/source_NN_models/model_cnn_photo_classification_CPU_script.pt")//"/data/data/com.example.chaquo_gradle/files/chaquopy/AssetFinder/app/source_NN_models/cnnCPUscript.pt")//"python/source_NN_models/cnnCPUscript.pt")
//            val bitmap = BitmapFactory.decodeStream(File(pathPhoto).inputStream())
//            val resizedBitmap = resizeBitmap(bitmap, 200, 200)
//            val inputTensor: Tensor = TensorImageUtils.bitmapToFloat32Tensor(
//                resizedBitmap,
//                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB
//            )
//            val outputTensor = model!!.forward(IValue.from(inputTensor)).toTensor()
//            val outputData = outputTensor.dataAsFloatArray
//            for (element in outputData) {
//                println(element)
//                Log.i("RETURN", element.toString())
//            }
//
////            outputTensor.
//            return outputTensor.dataAsFloatArray
//        }
//        catch (e: Exception){
//            Log.d("Error CNN", e.toString())
//            e.toString()
//            return FloatArray(1)
//        }
//    }
//    private fun resizeBitmap(originalBitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
//        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
//    }

    //    private fun assetFilePath(context: Context, assetName: String): String? {
//        val file: File = File(context.cacheDir, assetName)
//        try {
//            val inputStream = context.assets.open(assetName)
//            val outputStream = FileOutputStream(file)
//            val buffer = ByteArray(4 * 1024)
//            var read: Int
//            while (inputStream.read(buffer).also { read = it } != -1) {
//                outputStream.write(buffer, 0, read)
//            }
//            outputStream.flush()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return "!!!"
//        }
//        return file.absolutePath
//    }
    @Throws(IOException::class)
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
//    object AppFileDirectory {
//        var myFilesDir = File(this.filesDir, "my_files")
//    }
    fun showToastFileNotExist(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}