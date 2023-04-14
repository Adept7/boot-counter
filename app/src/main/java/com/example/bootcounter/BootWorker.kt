package com.example.bootcounter

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.bootcounter.room.BootDatabase
import com.example.bootcounter.room.BootEntity

const val WORK_INTERVAL = 15L
private const val CHANNEL_ID = "boot_id"
private const val CHANNEL_NAME = "Boot channel"
private const val CHANNEL_DESCRIPTION = "For Boot intent"

class BootWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        val bootTimestamp = System.currentTimeMillis()
        Log.wtf("ttt", "doWork")
        updateDb(bootTimestamp)
        createNotificationChannel()
        createNotification(bootTimestamp)
        return Result.success()
    }

    private fun updateDb(bootTimestamp: Long) {
        val db = Room.databaseBuilder(
            applicationContext,
            BootDatabase::class.java, "boot-db"
        ).build()

        db.bootDao().insert(BootEntity(bootTimestamp))
        db.close()
    }

    private fun createNotification(bootTimestamp: Long) {
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.checkbox_on_background)
            .setContentTitle("No boots detected")
            .setContentText("$bootTimestamp")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            Log.wtf("ttt", "run")
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}