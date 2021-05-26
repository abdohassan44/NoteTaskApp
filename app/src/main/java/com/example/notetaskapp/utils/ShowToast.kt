package com.example.notetaskapp.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun currentDate(): Calendar {
    return Calendar.getInstance()
}

fun formatReminderDate(date: Long): String {
    return SimpleDateFormat("dd MMM, yyyy h:mm a", Locale.getDefault()).format(date)
}
