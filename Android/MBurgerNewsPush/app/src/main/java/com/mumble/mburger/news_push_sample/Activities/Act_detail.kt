package com.mumble.mburger.news_push_sample.Activities

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.mumble.mburger.news_push_sample.Controllers.Config
import com.mumble.mburger.news_push_sample.Controllers.CustomComponents
import com.mumble.mburger.news_push_sample.Data.News
import com.mumble.mburger.news_push_sample.R
import io.noties.markwon.Markwon
import kotlinx.android.synthetic.main.act_detail.*
import mumble.mburger.sdk.MBClient.MBApiResultsLIsteners.MBApiSectionResultListener
import mumble.mburger.sdk.MBClient.MBData.MBElements.MBImages
import mumble.mburger.sdk.MBClient.MBData.MBSections.MBSection
import mumble.mburger.sdk.MBClient.MBMapper.MBFieldsMapping
import mumble.mburger.sdk.MBClient.MBurgerMapper
import mumble.mburger.sdk.MBClient.MBurgerTasks
import java.text.SimpleDateFormat

class Act_detail : AppCompatActivity() {

    var news: News? = null
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm")
    lateinit var pd: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.extras == null) {
            finish()
            return
        }

        if (intent.extras.containsKey("news")) {
            news = intent.getSerializableExtra("news") as News
        }

        val section_id = intent.getLongExtra("section_id", -1)

        setContentView(R.layout.act_detail)

        val imageSize = CustomComponents.getScreenWidth(this) / 1.5
        detail_images_layout.layoutParams.height = imageSize.toInt()

        pd = ProgressDialog(this)
        pd.setMessage(getString(R.string.loading))

        if (news != null) {
            updateUI()
        } else {
            MBurgerTasks.askForSection(applicationContext, section_id, true, object : MBApiSectionResultListener {
                override fun onSectionApiResult(section: MBSection, section_id: Long) {
                    val fieldsMapping = MBFieldsMapping()
                    fieldsMapping.putMap("title", Config.ELEM_TITLE)
                    fieldsMapping.putMap("content", Config.ELEM_CONTENT)
                    fieldsMapping.putMap("images", Config.ELEM_IMAGES, arrayOf<String>())
                    fieldsMapping.putMap("category", Config.ELEM_CATEGORY)
                    news = MBurgerMapper.mapToCustomObject(section, fieldsMapping, News(), false) as News
                    news!!.creation_date = section.available_atMillis
                    updateUI()
                    pd.dismiss()
                }

                override fun onSectionApiError(error: String) {
                    Log.d("Mburger Section Error", error)
                    pd.dismiss()
                }
            })

            pd.show()
        }

        setSupportActionBar(detail_toolbar)
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

    fun updateUI() {
        detail_txt_title.text = news!!.title!!
        detail_txt_date.text = dateFormatter.format(news!!.creation_date)
        detail_txt_category.text = CustomComponents.getCategoryName(applicationContext, news!!.category!!)
        Markwon.create(this).setMarkdown(detail_txt_content, news!!.content!!)
        detail_images_pager.adapter = CustomPagerAdapter(applicationContext, news!!.images!!)

        detail_layout.visibility = View.VISIBLE
        supportActionBar?.title = news!!.title!!
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
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
