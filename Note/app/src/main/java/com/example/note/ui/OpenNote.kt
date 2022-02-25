package com.example.note.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import com.example.note.data.Note
import com.example.note.databinding.ActivityOpenNoteBinding

class OpenNote : AppCompatActivity() {
    var fromalbum=123
    private var binding: ActivityOpenNoteBinding? = null
    private var selectedNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenNoteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        selectedNote = intent.getSerializableExtra("SelectedNote") as Note

        binding?.llBg?.setBackgroundColor(selectedNote!!.color!!)
        binding?.tvTitleOpen?.text = selectedNote?.title
        binding?.tvDesOpen?.text = selectedNote?.des

       if(!selectedNote?.uri.isNullOrEmpty()) {
//           val intent=Intent(Intent.ACTION_OPEN_DOCUMENT)
//           intent.addCategory(Intent.CATEGORY_OPENABLE)
//           intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
//             var imgPath=selectedNote?.uri
//           val imgUri=Uri.parse(imgPath)
//           val bitmap=getBipmapFromUri(imgUri)
//             binding?.imgnote?.setImageBitmap(BitmapFactory.decodeFile(imgPath))
//             binding?.imgnote?.visibility=View.VISIBLE
//           val intent=Intent(Intent.ACTION_OPEN_DOCUMENT)
//           intent.type="image/*"
//           intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
//           intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//           startActivityForResult(intent, fromalbum)
//           var inputStream=contentResolver.openInputStream(imgPath?.toUri()!!)
//           var bitmap=BitmapFactory.decodeStream(inputStream)
//           binding?.imgnote?.setImageBitmap(bitmap)
//           binding?.imgnote?.visibility= View.VISIBLE
             var imgPath = selectedNote?.uri
             val imgUri = Uri.parse(imgPath)
             val bitmap = getBipmapFromUri(imgUri)
             binding?.imgnote?.setImageBitmap(bitmap)
             binding?.imgnote?.visibility= View.VISIBLE
       }



        binding?.ivBackButton?.setOnClickListener {
            finish()
        }

        binding?.ivEditButton?.setOnClickListener {
            val editNoteIntent = Intent(this, EditNote::class.java)
            editNoteIntent.putExtra("EditSelectedNote", selectedNote)
            startActivity(editNoteIntent)
        }

        binding?.tvTitleOpen?.text = selectedNote?.title
        binding?.tvDesOpen?.text = selectedNote?.des
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
    private fun getBipmapFromUri(uri: Uri)= contentResolver.openFileDescriptor(uri,"r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when(requestCode){
//            fromalbum->{
//                if(resultCode== Activity.RESULT_OK&&data!=null)
//                {
//                    var imgPath=selectedNote?.uri
//                    binding?.imgnote?.setImageBitmap(BitmapFactory.decodeFile(imgPath))
//                    binding?.imgnote?.visibility=View.VISIBLE
//                    var inputStream=contentResolver.openInputStream(imgPath?.toUri()!!)
//                    var bitmap=BitmapFactory.decodeStream(inputStream)
//                    binding?.imgnote?.setImageBitmap(bitmap)
//                    binding?.imgnote?.visibility= View.VISIBLE
//                }
//            }
//        }
//    }
}