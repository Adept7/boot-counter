package com.example.bootcounter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.bootcounter.BootWorker
import com.example.bootcounter.WORK_INTERVAL
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.wtf("ttt", "receive ${intent?.action}")
        context?.let { ctx ->
            createWork(ctx)
        }
    }

    private fun createWork(ctx: Context) {
        val workRequest = PeriodicWorkRequest.Builder(BootWorker::class.java, WORK_INTERVAL, TimeUnit.MINUTES).build()
        WorkManager.getInstance(ctx).enqueue(workRequest)
    }
}