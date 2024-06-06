package com.wspyo.sololife.setting

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wspyo.sololife.R
import com.wspyo.sololife.auth.IntroActivity

class SettingActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)

        auth = Firebase.auth

        val logoutBtn : Button = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener{
            auth.signOut()

            Toast.makeText(this,"로그아웃",Toast.LENGTH_SHORT).show()

            val intent = Intent(this,IntroActivity::class.java)
            // 현재 스택으로 쌓이고 있는 Intent들을 Clear 하여 뒤로가기시 앱이 종료될수 있도록한다.
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}