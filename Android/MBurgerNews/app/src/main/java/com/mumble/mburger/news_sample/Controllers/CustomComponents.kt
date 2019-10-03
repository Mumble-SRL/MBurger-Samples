package com.mumble.mburger.news_sample.Controllers

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import com.mumble.mburger.news_sample.Activities.Act_list
import com.mumble.mburger.news_sample.R

class CustomComponents {
    companion object {

        fun getCategoryName(context: Context, category: String): String {
            return when (category) {
                Config.CAT_SOCIETY -> {
                    context.getString(R.string.society)
                }

                Config.CAT_SPORT -> {
                    context.getString(R.string.sport)
                }

                else -> {
                    context.getString(R.string.tech)
                }
            }
        }

        fun getScreenWidth(act: Activity?): Int {
            return if (Build.VERSION.SDK_INT >= 17) {
                val d = act!!.windowManager.defaultDisplay

                val realDisplayMetrics = DisplayMetrics()
                d.getRealMetrics(realDisplayMetrics)

                realDisplayMetrics.widthPixels
            } else {
                val wm = act!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val display = wm.defaultDisplay
                display.width
            }
        }
    }
}