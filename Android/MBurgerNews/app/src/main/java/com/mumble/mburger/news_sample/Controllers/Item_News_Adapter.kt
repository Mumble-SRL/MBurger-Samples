package com.mumble.mburger.news_sample.Controllers

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mumble.mburger.news_sample.Activities.Act_detail
import com.mumble.mburger.news_sample.Data.News
import com.mumble.mburger.news_sample.R
import java.text.SimpleDateFormat

class Item_News_Adapter(val act: Activity, val items: ArrayList<News>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_ITEM = 1

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm")

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layout: LinearLayout = itemView.findViewById(R.id.item_news_layout)
        var txt_title: AppCompatTextView = itemView.findViewById(R.id.item_news_txt_title)
        var txt_date: AppCompatTextView = itemView.findViewById(R.id.item_news_txt_date)
        var img: ImageView = itemView.findViewById(R.id.item_new_img)

        init {
            var dimen = (CustomComponents.getScreenWidth(act) / 5)
            img.layoutParams.width = dimen
            img.layoutParams.height = dimen
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items!![position]
        if (holder is ViewHolder) {
            holder.txt_title.text = item.title
            holder.txt_date.text = dateFormatter.format(item.creation_date)
            Glide.with(act).load(item.images!!.firstImage.url).into(holder.img)

            holder.layout.setOnClickListener {
                val intent = Intent(act, Act_detail::class.java)
                intent.putExtra("news", item)
                act.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(act).inflate(R.layout.item_news, parent, false))
    }
}