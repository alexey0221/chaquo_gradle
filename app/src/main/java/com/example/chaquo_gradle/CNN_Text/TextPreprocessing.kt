package com.example.chaquo_gradle.CNN_Text

import android.content.Context
import android.content.res.AssetManager
import android.os.Environment
import android.util.Log
import android.widget.Toast
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.nio.ByteBuffer
import java.nio.channels.FileChannel


private val downloadDir =
    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)

fun textCNN(context: Context, tflite :Interpreter) {
    val photoFiles = File(downloadDir, "Текстовые документы")
    photoFiles.listFiles()?.forEach { file ->
        if (!file.exists()) {
            showToastFileNotExist(context,
                "${file.name} не существует"
            )
        } else {
            if(file.extension=="txt") {
                val predict = doInference(file.absolutePath, tflite)
                if (predict != null) {
                    var classOfText = ""
                    when (predict) {
                        0 -> classOfText = "Офциально-деловой"
                        1 -> classOfText = "Публицестический"
                        2 -> classOfText = "Художественный"
                        3 -> classOfText = "Научный"
                    }
                    showToastFileNotExist(context, file.name + " " + classOfText)
                    if (predict == 0) {
                        val builder: androidx.appcompat.app.AlertDialog.Builder =
                            androidx.appcompat.app.AlertDialog.Builder(context)
                        builder
                            .setTitle("Внимание!")
                            .setMessage("Текст ${file.name} принаделжит к классу документов, нужна проверка на наличие личной информации")
                            .setPositiveButton("Понял") { dialog, which ->
                            }
                        val dialog: androidx.appcompat.app.AlertDialog = builder.create()
                        dialog.show()
                    }
                } else {
                    showToastFileNotExist(context, "Ошибка при чтении файла")
                }
            }
        }
    }
}

fun loadModelFile(activity: AssetManager, MODEL_FILE: String): ByteBuffer {
    val fileDescriptor = activity.openFd(MODEL_FILE)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}

fun doInference(filePath: String, tflite: Interpreter): Int? {
    var text = readTxtFile(File(filePath)) ?: return null // считали файл в переменную
    Log.i("text ${File(filePath).name}", text)
    text = preprocessText(text) // удалили не нужные слова
    val listText = listOf(text)// создали список
    val tokenizer = Tokenizer(100_000) // инициализируем Токинезатор
    tokenizer.fitOnTexts(listText) // строим словарь
    var sequences = tokenizer.textsToSequences(listText) // подменяем слова индексами

    sequences = tokenizer.padSequences(sequences, 100_000) //выравниваем под длине

    val inputVal = convertIntListToFloatList(sequences)//floatArrayOf(userVal)
    val outputVal = arrayOf(FloatArray(4)) //ByteBuffer = ByteBuffer.allocateDirect(16)

    Log.d("List<List<Float>>", inputVal.toString())

    val flattenedList = inputVal.flatten()
    Log.i("flattenedList", flattenedList.toString())
    // Преобразование плоского списка в FloatArray
    val floatArray = flattenedList.toFloatArray()
    Log.i("floatArray size = ${floatArray.size}", floatArray.toString())
    try {
        tflite.run(
            arrayOf(floatArray),
            outputVal
        )
        val floatArray = outputVal[0]

//        val builder = AlertDialog.Builder(this)
//        /* val maxIndex = floatArray.indices.maxByOrNull { floatArray[it] } ?: -1*/
        return floatArray.indices.maxByOrNull { floatArray[it] } ?: -1
//        with(builder)
//        {
//            setTitle("TFLite Interpreter")
//            setMessage("Predict of text: $maxIndex")
//            setNeutralButton("OK", DialogInterface.OnClickListener { dialog, id ->
//                dialog.cancel()
//            })
//            show()
//        }
    }catch (e:Exception){
        Log.e("run", e.toString())
        return null
    }
}
fun readTxtFile(file: File):String? {
//        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "filename.txt")
    val text: StringBuilder = StringBuilder()
    try {
        Log.i("FileReader", "Before")
        val br = BufferedReader(FileReader(file))
        Log.i("FileReader", "After")
        var line: String?
        while (br.readLine().also { line = it } != null) {
            text.append(line)
            text.append('\n')
        }
        br.close()
        // Use the text variable as needed
        return text.toString()
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("readTxtFile", e.toString())
        return null
    }
}
fun preprocessText(text: String): String {
    val stopWords = setOf(
        "и",
        "в",
        "во",
        "не",
        "что",
        "он",
        "на",
        "я",
        "с",
        "со",
        "как",
        "а",
        "то",
        "все",
        "она",
        "так",
        "его",
        "но",
        "да",
        "ты",
        "к",
        "у",
        "же",
        "вы",
        "за",
        "бы",
        "по",
        "только",
        "ее",
        "мне",
        "было",
        "вот",
        "от",
        "меня",
        "еще",
        "нет",
        "о",
        "из",
        "ему",
        "теперь",
        "когда",
        "даже",
        "ну",
        "вдруг",
        "ли",
        "если",
        "уже",
        "или",
        "ни",
        "быть",
        "был",
        "него",
        "до",
        "вас",
        "нибудь",
        "опять",
        "уж",
        "вам",
        "ведь",
        "там",
        "потом",
        "себя",
        "ничего",
        "ей",
        "может",
        "они",
        "тут",
        "где",
        "есть",
        "надо",
        "ней",
        "для",
        "мы",
        "тебя",
        "их",
        "чем",
        "была",
        "сам",
        "чтоб",
        "без",
        "будто",
        "чего",
        "раз",
        "тоже",
        "себе",
        "под",
        "будет",
        "ж",
        "тогда",
        "кто",
        "этот",
        "того",
        "потому",
        "этого",
        "какой",
        "совсем",
        "ним",
        "здесь",
        "этом",
        "один",
        "почти",
        "мой",
        "тем",
        "чтобы",
        "нее",
        "сейчас",
        "были",
        "куда",
        "зачем",
        "всех",
        "никогда",
        "можно",
        "при",
        "наконец",
        "два",
        "об",
        "другой",
        "хоть",
        "после",
        "над",
        "больше",
        "тот",
        "через",
        "эти",
        "нас",
        "про",
        "всего",
        "них",
        "какая",
        "много",
        "разве",
        "три",
        "эту",
        "моя",
        "впрочем",
        "хорошо",
        "свою",
        "этой",
        "перед",
        "иногда",
        "лучше",
        "чуть",
        "том",
        "нельзя",
        "такой",
        "им",
        "более",
        "всегда",
        "конечно",
        "всю",
        "между"
    )
    return text.replace("\n", "")
        .lowercase()
        .replace(Regex("\\d+"), "")
        .split(" ")
        .filter { it.length > 2 && it !in stopWords }//.toIntArray()
        .joinToString(" ") // Объединить слова в единую строку
}
fun convertIntListToFloatList(intList: List<List<Int>>): List<List<Float>> {
    return intList.map { innerList ->
        innerList.map { it.toFloat() }
    }
}
fun showToastFileNotExist(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}