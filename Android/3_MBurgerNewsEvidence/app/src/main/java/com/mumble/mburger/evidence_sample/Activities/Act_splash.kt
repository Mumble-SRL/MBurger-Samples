package com.mumble.mburger.evidence_sample.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mumble.mburger.evidence_sample.Controllers.CustomComponents
import com.mumble.mburger.evidence_sample.R

class Act_splash : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash)

        CustomComponents.createNotificationChannel(this)

        Handler().postDelayed({
            val i = Intent(applicationContext, Act_list::class.java)
            if (intent.extras != null) {
                i.putExtras(intent.extras)
            }

            startActivity(i)
            finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}
