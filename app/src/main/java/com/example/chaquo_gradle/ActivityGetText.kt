package com.example.chaquo_gradle

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class ActivityGetText: AppCompatActivity()  {

    private val mDataPath = Environment.getExternalStorageDirectory().absolutePath + "/tessdata/"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_text)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.getText)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val myFiles = File(this.filesDir, "my_files")
        copyFiles()
        val mTess = TessBaseAPI()
        val lang = "rus"
        val butGetText = findViewById<Button>(R.id.ButGetText)
        val textOnImage = findViewById<TextView>(R.id.textOnImage)
        butGetText.setOnClickListener{
            mTess.init(Environment.getExternalStorageDirectory().absolutePath, lang)
            val bitmap = BitmapFactory.decodeStream(myFiles.listFiles()?.get(0)?.inputStream())
            mTess.setImage(bitmap)
            val OCRresult = mTess.utF8Text
            textOnImage.text = OCRresult
        }

//        myFiles.listFiles()?.forEach { file ->
//
//        }
    }
    private fun copyFiles() {
        val datafilepaths = arrayOf<String>(
            mDataPath + "/chi_sim.traineddata",
            mDataPath + "/eng.traineddata"
        ) // Copy two fonts in the past
        for (datafilepath in datafilepaths) {
            copyFile(datafilepath)
        }
    }

    private fun copyFile(datafilepath: String) {
        try {
            val filesegment =
                datafilepath.split(File.separator.toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            val filename = filesegment[filesegment.size - 1] // Get the font file name
            val assetManager = assets
            val instream = assetManager.open(filename) // Open font file
            val outstream: OutputStream = FileOutputStream(datafilepath)
            val buffer = ByteArray(1024)
            var read: Int
            while (instream.read(buffer).also { read = it } != -1) {
                outstream.write(buffer, 0, read)
            }
            outstream.flush()
            outstream.close()
            instream.close()
            val file = File(datafilepath)
            if (!file.exists()) {
                throw FileNotFoundException()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}