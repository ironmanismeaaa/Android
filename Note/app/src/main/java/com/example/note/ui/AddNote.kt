@file:Suppress("DEPRECATION")

package com.example.note.ui

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.note.data.Note
import com.example.note.databinding.ActivityAddNoteBinding
import java.security.AccessController.getContext
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddNote : AppCompatActivity() {
    var fromalbum=123
    var imgPath:String=""
    private var binding: ActivityAddNoteBinding? = null
    lateinit var viewModel: NoteViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentTime() :String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formatted = current.format(formatter)
//        attendanceout_sign_time.setText(formatted.toString())
        return formatted.toString()
    }//zyy
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        binding?.btnAdd?.setOnClickListener {
            if (binding?.etNoteTitle?.text.toString().isNotEmpty()
                && binding?.etNoteDes?.text.toString().isNotEmpty()) {
                addNote(binding?.etNoteTitle?.text.toString(), binding?.etNoteDes?.text.toString(),
                    null,imgPath,getCurrentTime()) //zyy
                val mainIntent = Intent(this, MainActivity::class.java)
                var intent=Intent("add successfully")
                intent.setPackage(packageName)
                sendBroadcast(intent)
                startActivity(mainIntent)

            }else {
                Toast.makeText(this, "标题和内容一定要添加！", Toast.LENGTH_SHORT).show()
            }
        }
        binding!!.btnAddImg.setOnClickListener {
            val intent=Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type="image/*"
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, fromalbum)
        }
    }

    private fun addNote (title: String, des: String, color: Int?,uri: String?,time:String?) {

        viewModel.insertNote(Note(0, title, des, color,uri,time))
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
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
    private fun getBipmapFromUri(uri: Uri)= contentResolver.openFileDescriptor(uri,"r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }
}