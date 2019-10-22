package com.mumble.mburger.relation_sample.Controllers

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.mumble.mburger.relation_sample.R
import mumble.mbpush.MBPushMethods.MBPushResultsListener.MBPushSendTokenListener
import mumble.mbpush.MBPushMethods.MBPushResultsListener.MBPushUnregisterTopicsListener
import mumble.mbpush.MBPushMethods.MBurgerPushTasks
import org.json.JSONArray
import org.json.JSONObject

class CustomComponents {
    companion object {

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT < 26) {
                return
            }
            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val id = context.getString(R.string.notif_channel_id)
            val name = context.getString(R.string.notif_channel_name)
            val description = context.getString(R.string.notif_channel_desc)
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.description = description
            mChannel.enableLights(true)
            mChannel.lightColor = ContextCompat.getColor(context, R.color.colorAccent)
            mChannel.setShowBadge(true)
            mChannel.enableVibration(true)
            mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            mNotificationManager.createNotificationChannel(mChannel)
        }

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

        fun isJSONOk(json: JSONObject, key: String): Boolean {
            var control = false
            if (json.has(key)) {
                if (!json.isNull(key)) {
                    control = true
                }
            }
            return control
        }

        fun getAndroidID(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun registerForPush(context: Context) {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
                val token = instanceIdResult.token
                MBurgerPushTasks.sendToken(context, object : MBPushSendTokenListener {
                    override fun onTokenSent() {
                        val jTopics = JSONArray()
                        jTopics.put(Config.PushTopic)
                        MBurgerPushTasks.registerTopics(context, getAndroidID(context), jTopics)
                    }

                    override fun onTokenSentError(error: String) {

                    }
                }, getAndroidID(context), token)
            }
        }

        fun unregisterForPush(context: Context){
            val jTopics = JSONArray()
            jTopics.put(Config.PushTopic)

            MBurgerPushTasks.unregisterTopics(context, object : MBPushUnregisterTopicsListener {
                override fun onTopicsUnregistered() {
                }

                override fun onTopicsUnregisteredError(error: String?) {
                }
            }, getAndroidID(context), jTopics)
        }
    }
}