//import android.R
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.chaquo_gradle.MainActivity
import java.io.File

//import com.example.protection.R
//import com.example.chaquo_gradle.R

class FileAdapter(private val files: List<FileItem>) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.chaquo_gradle.R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.bind(file)
    }

    override fun getItemCount(): Int {
        return files.size
    }


    @SuppressLint("NotConstructor")
    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var mainActivity: MainActivity = MainActivity()

        private val textViewFileName: TextView = itemView.findViewById(com.example.chaquo_gradle.R.id.documentNameTextView)
        val deleteButton = itemView.findViewById<View>(com.example.chaquo_gradle.R.id.deleteButton)
        val sendButton = itemView.findViewById<View>(com.example.chaquo_gradle.R.id.sendButton)
        fun bind(file: FileItem) {
            textViewFileName.text = file.name


            deleteButton.setOnClickListener(View.OnClickListener {
                // обработка нажатия на кнопку удаления
                val position = adapterPosition
                // выполнение действий по удалению файла на позиции position
                val builder: AlertDialog.Builder = AlertDialog.Builder(it.context)
                builder
                    .setMessage("Вы уверены, что хотите удалить файл")
                    .setTitle("Удалить?")
                    .setPositiveButton("Да") { dialog, which ->
                        deleteFile(it.context, file.name)
//                        notifyDataSetChanged()
                    }
                    .setNegativeButton("Нет") { dialog, which ->
                        Toast.makeText(it.context, "Удаление отменено", Toast.LENGTH_SHORT)
                            .show() // Do something.// Do something else.
                    }
                val dialog: AlertDialog = builder.create()
                dialog.show()
//                val mainActivity = MainActivity()//=========================================================
//                mainActivity.getListFromMyFiles()
                try {
                    callGetListFromMyFiles()
                }
                catch (e: Exception){
                    Log.e("callGetListFromMyFiles()Error", e.toString())
                    Toast.makeText(it.context, "Ошибка обновления списка", Toast.LENGTH_SHORT)
                        .show()
                }
            })

            sendButton.setOnClickListener(View.OnClickListener {
                // обработка нажатия на кнопку отправки
                val position = adapterPosition
                // выполнение действий по отправке файла на позиции position
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    val myFilesDir = File(it.context.filesDir, "my_files")
                    val myFile = File(myFilesDir, file.name)
                    putExtra(Intent.EXTRA_TEXT, myFile)
                    type = "application/pdf"//"text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                itemView.context.startActivity(shareIntent)
            })

            textViewFileName.setOnClickListener {
//                MainActivity.openPDF()
                val myFilesDir = File(it.context.filesDir, "my_files")//MainActivity.AppFileDirectory.filesDir
                val file = File(myFilesDir, file.name)
                val pdfFileUri: Uri = FileProvider.getUriForFile(
                    itemView.context,
                    "com.example.protection.fileprovider",
                    file
                )
//                val pdfFileUri: Uri = file.toUri() // Replace with the URI of the PDF file you want to view
                val intent = Intent(Intent.ACTION_VIEW)
                //Определение формата файла
                when (file.toString().substring(file.toString().lastIndexOf("."))) {
                    "pdf" -> intent.setDataAndType(pdfFileUri, "//")
                    "docx" -> intent.setDataAndType(pdfFileUri, "application/msword")
                    "jpg" -> intent.setDataAndType(pdfFileUri, "image/*")
                }
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                val intentStart = Intent.createChooser(intent, "Open File");
                try {
                    itemView.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    // Instruct the user to install a PDF reader here, or something
                }
            }
        }
        private fun deleteFile(context: Context, nameFile: String){
//        val appDirectoryPath = applicationContext.filesDir.path

            val myFilesDir = File(context.filesDir, "my_files")//MainActivity.AppFileDirectory.filesDir
            val file = File(myFilesDir,nameFile)
            if (file.exists()) {
                if (file.delete()) {
                    // File deleted successfully
                    Toast.makeText(context, "Файл успешно удален", Toast.LENGTH_SHORT).show() // Do something.
                } else {
                    // Failed to delete file
                    Toast.makeText(context, "Ошибка, удалить файл не удалось", Toast.LENGTH_SHORT).show() // Do something.
                }
            } else {
                // File does not exist
            }
        }
        fun FileViewHolder(itemView: View, mainActivity: MainActivity) {
            super.itemView//itemView)
            this.mainActivity = mainActivity
        }

        // Call getListFromMyFiles function
        fun callGetListFromMyFiles() {
            mainActivity.getListFromMyFiles()
        }
    }
//    override fun notifyDataSetChanged() {
//        // In your FileAdapter class
//        notifyItemRangeChanged(0, files.size)
//    }

}
