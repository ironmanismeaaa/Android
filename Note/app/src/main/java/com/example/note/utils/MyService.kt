package com.example.note.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.note.R
import com.example.note.ui.MainActivity

class MyService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
//        Log.d("My service","On")
//        Toast.makeText(null, "修改成功", Toast.LENGTH_SHORT).show()
        Log.d("MyService","onCreate executed")

        val manager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel= NotificationChannel("my_service","前台Service通知", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }
        val intent=Intent(this, MainActivity::class.java)
        val pi= PendingIntent.getActivity(this,0,intent,0)
        val notification= NotificationCompat.Builder(this,"my_service")
                .setContentTitle("心记 正在运行")
                .setContentText("点击进入")
                .setSmallIcon(R.drawable.icon_small)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.icon_large))
                .setContentIntent(pi)
                .build()
        startForeground(1,notification)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}