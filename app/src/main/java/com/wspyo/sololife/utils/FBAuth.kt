package com.wspyo.sololife.utils

import android.icu.util.Calendar
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Locale

class FBAuth {

    companion object {
        private lateinit var auth: FirebaseAuth
        fun getUid() : String{
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }
        fun getTime() : String {
            val currentDataTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREAN).format(currentDataTime)
            return dateFormat
        }
    }
}