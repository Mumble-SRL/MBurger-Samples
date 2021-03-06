package com.mumble.mburger.relation_sample.Activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.mumble.mburger.relation_sample.Controllers.Config
import com.mumble.mburger.relation_sample.Controllers.CustomComponents
import com.mumble.mburger.relation_sample.Controllers.Item_News_Adapter
import com.mumble.mburger.relation_sample.Data.News
import com.mumble.mburger.relation_sample.R
import kotlinx.android.synthetic.main.act_list.*
import mumble.mburger.sdk.MBClient.MBApiFilters.MBFilterParameter
import mumble.mburger.sdk.MBClient.MBApiResultsLIsteners.MBApiProjectResultListener
import mumble.mburger.sdk.MBClient.MBApiResultsLIsteners.MBApiSectionsResultListener
import mumble.mburger.sdk.MBClient.MBData.MBPaginationInfo
import mumble.mburger.sdk.MBClient.MBData.MBProjects.MBEvidenceObject
import mumble.mburger.sdk.MBClient.MBData.MBProjects.MBProject
import mumble.mburger.sdk.MBClient.MBData.MBSections.MBSection
import mumble.mburger.sdk.MBClient.MBMapper.MBFieldsMapping
import mumble.mburger.sdk.MBClient.MBurgerMapper
import mumble.mburger.sdk.MBClient.MBurgerTasks
import java.util.*

class Act_list : MBApiProjectResultListener, MBApiSectionsResultListener, AppCompatActivity() {

    lateinit var pd: ProgressDialog
    var news: ArrayList<News>? = null
    var selected_category: String? = null

    var askedForEvidence = false
    var evidenceObject: MBEvidenceObject? = null

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

        if (getSharedPreferences("preferences", Context.MODE_PRIVATE).getBoolean(Config.PROPERTY_PUSH_ENABLED, true)) {
            CustomComponents.registerForPush(applicationContext)
        }

        list_evidence_layout.layoutParams.height = CustomComponents.getScreenWidth(this) / 2
        list_evidence_layout.setOnClickListener {
            val intentD = Intent(applicationContext, Act_detail::class.java)
            intentD.putExtra("section_id", evidenceObject!!.section_id)
            startActivity(intentD)
        }

        if (intent.extras != null) {
            val block_id = intent.getLongExtra("block_id", -1L)
            val section_id = intent.getLongExtra("section_id", -1L)
            if (block_id != -1L) {
                when (block_id) {
                    Config.BLOCK_NEWS -> {
                        val intentD = Intent(applicationContext, Act_detail::class.java)
                        intentD.putExtra("section_id", section_id)
                        startActivity(intentD)
                    }
                }
            }
        }

        setSupportActionBar(list_toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        menu.findItem(R.id.action_push_notifications).isChecked =
                getSharedPreferences("preferences", Context.MODE_PRIVATE).getBoolean(Config.PROPERTY_PUSH_ENABLED, true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_push_notifications -> {
                if (getSharedPreferences("preferences", Context.MODE_PRIVATE).getBoolean(Config.PROPERTY_PUSH_ENABLED, true)) {
                    CustomComponents.unregisterForPush(applicationContext)
                    getSharedPreferences("preferences", Context.MODE_PRIVATE).edit().putBoolean(Config.PROPERTY_PUSH_ENABLED, false).apply()
                    item.isChecked = false
                } else {
                    CustomComponents.registerForPush(applicationContext)
                    getSharedPreferences("preferences", Context.MODE_PRIVATE).edit().putBoolean(Config.PROPERTY_PUSH_ENABLED, true).apply()
                    item.isChecked = true
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (!askedForEvidence) {
            MBurgerTasks.askForProject(this, this, false)
            pd.show()
        } else {
            if (news == null) {
                callForSections()
            }
        }
    }

    fun createList() {
        if (evidenceObject != null) {
            setEvidence()
            list_evidence_layout.visibility = View.VISIBLE
        } else {
            list_evidence_layout.visibility = View.GONE
        }

        pd.dismiss()
        if (news != null) {
            if (news!!.isEmpty()) {
                list_no_elements_txt.visibility = View.VISIBLE
            } else {
                list_no_elements_txt.visibility = View.GONE
            }

            var evidenceSectionId = -1L
            if (evidenceObject != null) {
                evidenceSectionId = evidenceObject!!.section_id
            }

            val adapter = Item_News_Adapter(this, news, evidenceSectionId)
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

    fun setEvidence() {
        list_evidence_title.text = evidenceObject!!.title
        if (evidenceObject!!.image != null) {
            Glide.with(this).load(evidenceObject!!.image).into(list_evidence_img)
        }
    }

    override fun onProjectApiResult(project: MBProject?) {
        askedForEvidence = true
        if (project!!.evidenceObject != null) {
            evidenceObject = project.evidenceObject
        }

        if (news == null) {
            callForSections()
        }
    }

    override fun onProjectApiError(error: String?) {
        if (news == null) {
            callForSections()
        }
    }

    override fun onSectionsApiResult(sections: ArrayList<MBSection>?, block_id: Long, paginationInfos: MBPaginationInfo?) {
        if (!isFinishing) {
            news = ArrayList()
            val fieldsMapping = MBFieldsMapping()
            fieldsMapping.putMap("title", Config.ELEM_TITLE)
            fieldsMapping.putMap("content", Config.ELEM_CONTENT)
            fieldsMapping.putMap("images", Config.ELEM_IMAGES, arrayOf<String>())
            fieldsMapping.putMap("category", Config.ELEM_CATEGORY)
            fieldsMapping.putMap("author", Config.ELEM_AUTHOR)
            for (section in sections!!) {
                val s_news = MBurgerMapper.mapToCustomObject(section, fieldsMapping, News(), false) as News
                s_news.section_id = section.id
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