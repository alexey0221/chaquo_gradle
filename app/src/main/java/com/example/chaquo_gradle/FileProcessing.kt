//package com.example.chaquo_gradle
//
//import com.chaquo.python.Python
//
//fun getExtension(file: String): String {
//    return file.substring(file.lastIndexOf("."))
//}
//
//fun PyEctractingTextFromImage(image_path:String, lng:String){
//    val python = Python.getInstance()
//    val pythonFile = python.getModule("extractionTextFromImage")
//    pythonFile.callAttr("image_to_text", image_path, lng)
////    return pythonFile.callAttr("test", "Alex").toString()
//}
//
//fun PyEctractingTextFromPDF(pdf_path:String, lng:String){
//    val python = Python.getInstance()
//    val pythonFile = python.getModule("extractionTextFromPDF")
//    pythonFile.callAttr("extractingTextFromPDF", pdf_path, lng)
////    return pythonFile.callAttr("test", "Alex").toString()
//}
//
//fun PyEctractingTextFromDOCX(docx_path:String, lng:String){
//    val python = Python.getInstance()
//    val pythonFile = python.getModule("extractionTextFromDOCX")
//    pythonFile.callAttr("extractingTextFromDocx", docx_path)
////    return pythonFile.callAttr("test", "Alex").toString()
//}
//
//fun PyPhotoFindText(image_path: String): String{
//    val python = Python.getInstance()
//    val pythonFile = python.getModule("CNN_photo")
//    return pythonFile.callAttr("photoFindText", image_path).toString()//.toDouble()
//}