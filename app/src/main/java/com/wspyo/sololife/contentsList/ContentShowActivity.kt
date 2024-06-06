package com.wspyo.sololife.contentsList

import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.wspyo.sololife.R

class ContentShowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_content_show)

        val getUrl = intent.getStringExtra("url")
        Toast.makeText(this,getUrl,Toast.LENGTH_LONG).show()


        val webView = findViewById<WebView>(R.id.webView)
        webView.loadUrl(getUrl.toString())
    }
}