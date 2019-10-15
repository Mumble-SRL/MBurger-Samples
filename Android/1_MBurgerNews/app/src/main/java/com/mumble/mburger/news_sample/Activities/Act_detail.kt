package com.mumble.mburger.news_sample.Activities

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.mumble.mburger.news_sample.Controllers.CustomComponents
import com.mumble.mburger.news_sample.Data.News
import com.mumble.mburger.news_sample.R
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.act_detail.*
import mumble.mburger.sdk.MBClient.MBData.MBElements.MBImages
import java.text.SimpleDateFormat

class Act_detail : AppCompatActivity() {

    lateinit var news: News
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.extras == null) {
            finish()
            return
        }

        news = intent.getSerializableExtra("news") as News

        setContentView(R.layout.act_detail)

        val imageSize = CustomComponents.getScreenWidth(this) / 1.5
        detail_images_layout.layoutParams.height = imageSize.toInt()

        detail_txt_title.text = news.title!!
        detail_txt_date.text = dateFormatter.format(news.creation_date)
        detail_txt_category.text = CustomComponents.getCategoryName(applicationContext, news.category!!)
        Markwon.create(this).setMarkdown(detail_txt_content, news.content!!)
        detail_images_pager.adapter = CustomPagerAdapter(applicationContext, news.images!!)

        setSupportActionBar(detail_toolbar)
        supportActionBar?.title = news.title!!
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    internal inner class CustomPagerAdapter(val mContext: Context, val images: MBImages) : PagerAdapter() {

        override fun getCount(): Int {
            return images.images.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(applicationContext)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            Glide.with(mContext).load(images.images[position].url).into(imageView)
            container.addView(imageView)
            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as ImageView)
        }
    }
}
