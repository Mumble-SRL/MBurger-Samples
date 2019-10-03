package com.mumble.mburger.news_sample.Activities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.mumble.mburger.news_sample.Controllers.Config
import com.mumble.mburger.news_sample.Controllers.Item_News_Adapter
import com.mumble.mburger.news_sample.Data.News
import com.mumble.mburger.news_sample.R
import kotlinx.android.synthetic.main.act_list.*
import mumble.mburger.sdk.MBClient.MBApiFilters.MBFilterParameter
import mumble.mburger.sdk.MBClient.MBApiResultsLIsteners.MBApiSectionsResultListener
import mumble.mburger.sdk.MBClient.MBData.MBPaginationInfo
import mumble.mburger.sdk.MBClient.MBData.MBSections.MBSection
import mumble.mburger.sdk.MBClient.MBMapper.MBFieldsMapping
import mumble.mburger.sdk.MBClient.MBurgerMapper
import mumble.mburger.sdk.MBClient.MBurgerTasks
import java.util.*

class Act_list : MBApiSectionsResultListener, AppCompatActivity() {

    lateinit var pd: ProgressDialog
    var news: ArrayList<News>? = null
    var selected_category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_list)

        list_tablayout.addTab(list_tablayout.newTab().setText(R.string.all))
        list_tablayout.addTab(list_tablayout.newTab().setText(R.string.sport))
        list_tablayout.addTab(list_tablayout.newTab().setText(R.string.tech))
        list_tablayout.addTab(list_tablayout.newTab().setText(R.string.society))
        list_tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab!!.position) {
                    0 -> {
                        selected_category = null
                    }

                    1 -> {
                        selected_category = Config.CAT_SPORT
                    }

                    2 -> {
                        selected_category = Config.CAT_TECH
                    }
                    3 -> {
                        selected_category = Config.CAT_SOCIETY
                    }
                }

                callForSections()
            }
        })

        list_rview.layoutManager = LinearLayoutManager(this)
        list_rview.setHasFixedSize(true)

        pd = ProgressDialog(this)
        pd.setMessage(getString(R.string.loading))

        setSupportActionBar(list_toolbar)
    }

    override fun onResume() {
        super.onResume()
        if (news == null) {
            callForSections()
        }
    }

    fun createList() {
        pd.dismiss()
        if (news != null) {
            if (news!!.isEmpty()) {
                list_no_elements_txt.visibility = View.VISIBLE
            } else {
                list_no_elements_txt.visibility = View.GONE
            }

            val adapter = Item_News_Adapter(this, news)
            list_rview.adapter = adapter
        } else {
            list_no_elements_txt.visibility = View.VISIBLE
        }
    }

    fun callForSections() {
        val filters = ArrayList<Any>()
        if (selected_category != null) {
            filters.add(MBFilterParameter("category", selected_category))
        }

        MBurgerTasks.askForSections(this, Config.BLOCK_NEWS, filters, true, this)
        pd.show()
    }

    override fun onSectionsApiResult(sections: ArrayList<MBSection>?, block_id: Long, paginationInfos: MBPaginationInfo?) {
        if (!isFinishing) {
            news = ArrayList()
            val fieldsMapping = MBFieldsMapping()
            fieldsMapping.putMap("title", Config.ELEM_TITLE)
            fieldsMapping.putMap("content", Config.ELEM_CONTENT)
            fieldsMapping.putMap("images", Config.ELEM_IMAGES, arrayOf<String>())
            fieldsMapping.putMap("category", Config.ELEM_CATEGORY)
            for (section in sections!!) {
                val s_news = MBurgerMapper.mapToCustomObject(section, fieldsMapping, News(), false) as News
                s_news.creation_date = section.available_atMillis
                news!!.add(s_news)
            }

            createList()
        }
    }

    override fun onSectionsApiError(error: String?) {
        if (!isFinishing) {
            Log.d("Mburger News Error", error)
            pd.dismiss()
        }
    }
}
