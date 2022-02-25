package com.example.note.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.note.data.Note
import com.example.note.databinding.ActivityAddNoteBinding
import com.example.note.databinding.ActivityEditNoteBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditNote : AppCompatActivity() {
    var fromalbum=123
    var imgPath:String=""
    private var binding: ActivityEditNoteBinding? = null
    lateinit var viewModel: NoteViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime() :String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
//        time.setText(formatted.toString())
        return formatted.toString()
    }//zyy
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val selectedNote: Note = intent.getSerializableExtra("EditSelectedNote") as Note
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        binding?.apply {
            etEditNoteTitle.setText(selectedNote.title ?: "")
            etEditNoteDes.setText(selectedNote.des ?: (""))
            timeShow.setText(selectedNote.time ?: getCurrentTime())//zyy
            if(!selectedNote?.uri.isNullOrEmpty()){
                imgPath= selectedNote?.uri.toString()
                val imgUri= Uri.parse(imgPath)
                val bitmap=getBipmapFromUri(imgUri)
                imgnote?.setImageBitmap(bitmap)
                imgnote?.visibility= View.VISIBLE
                imgnote.setOnClickListener {
                    val intent=Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type="image/*"
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivityForResult(intent, fromalbum)
                }
            }
        }


        binding?.apply {
            btnSave.setOnClickListener {
                val time=getCurrentTime()//zyy

                val editedNote = Note(
                    selectedNote.id,
                    etEditNoteTitle.text.toString().takeIf { it.isNotBlank() },
                    etEditNoteDes.text.toString().takeIf { it.isNotBlank() },
                    selectedNote.color,
                    imgPath,
                    time//zyy
                )
                viewModel.updateNote(editedNote)
                Toast.makeText(this@EditNote, "保存成功", Toast.LENGTH_SHORT).show()

                val mainIntent = Intent(this@EditNote, MainActivity::class.java)
                startActivity(mainIntent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            fromalbum->{
                if(resultCode== Activity.RESULT_OK&&data!=null)
                {
                    data.data?.let{
                            uri ->
                        var selectedImageUri=data.data
                        imgPath=selectedImageUri.toString()
                        var inputStream=contentResolver.openInputStream(selectedImageUri!!)
                        var bitmap=BitmapFactory.decodeStream(inputStream)
                        binding?.imgnote?.setImageBitmap(bitmap)
                        binding?.imgnote?.visibility= View.VISIBLE
                        Log.d("debug","$selectedImageUri")

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    private fun getBipmapFromUri(uri: Uri)= contentResolver.openFileDescriptor(uri,"r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

}