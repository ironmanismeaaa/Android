package com.example.note.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action= intent?.action
        Log.d("debug","$action")
        if (TextUtils.equals(action, "add successfully")) {
            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show()
        }
    }
}