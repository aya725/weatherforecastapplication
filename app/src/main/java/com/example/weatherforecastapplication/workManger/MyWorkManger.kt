package com.example.weatherforecastapplication.workManger

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters


class MyWorkManger(val context: Context, workerParameters: WorkerParameters):
    Worker(context,workerParameters){


    override fun doWork(): Result {


        NotificationHelper(context).createNotification(
            inputData.getString("title").toString(),
            inputData.getString("message").toString())

        return Result.success()

    }




}