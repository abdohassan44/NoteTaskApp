package com.example.notetaskapp.work

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.notetaskapp.dp.Note
import com.example.notetaskapp.utils.NOTE_ID
import com.example.notetaskapp.utils.NOTE_TITLE
import com.example.notetaskapp.utils.currentDate

import java.util.concurrent.TimeUnit

fun createSchedule(context: Context, note: Note) {
    val data = Data.Builder()
        .putInt(NOTE_ID, note.id!!)
        .putString(NOTE_TITLE, note.title)
        .build()

    val delay = note.reminder!! - currentDate().timeInMillis - (60 * 1000 * 10)
    if (delay > 0) scheduleReminder(delay, data, context)
    else scheduleReminder(note.reminder - currentDate().timeInMillis, data, context)
}

private fun scheduleReminder(delay: Long, data: Data, context: Context) {
    val reminderWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(data)
        .addTag("${context.packageName}.work.NotifyWork")
        .build()

    val workName = "Work ${data.getInt(NOTE_ID, 0)}"

    val instanceWorkManager = WorkManager.getInstance(context)
    instanceWorkManager.enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, reminderWork)
}

fun cancelAlarm(context: Context, noteId: Int) {
    val workName = "Work $noteId"
    val instanceWorkManager = WorkManager.getInstance(context)
    instanceWorkManager.cancelUniqueWork(workName)
}