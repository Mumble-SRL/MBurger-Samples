package com.mumble.mburger.news_sample.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mumble.mburger.news_sample.R

class Act_splash : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash)

        Handler().postDelayed({
            val i = Intent(applicationContext, Act_list::class.java)
            startActivity(i)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}
