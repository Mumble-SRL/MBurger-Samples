package com.mumble.mburger.evidence_sample.Controllers

class Config {
    companion object {

        /**Edit with your MBurger APi Key and News Block id**/
        val MBurgerAPiKey = ""
        val MPushKey = ""
        val PushTopic = ""

        val BLOCK_NEWS = -1L

        //Category values
        const val CAT_SPORT = "sport"
        const val CAT_TECH = "tech"
        const val CAT_SOCIETY = "society"

        //Elements names form MBurger
        const val ELEM_TITLE = "title"
        const val ELEM_CONTENT = "content"
        const val ELEM_IMAGES = "images"
        const val ELEM_CATEGORY = "category"

        const val PROPERTY_PUSH_ENABLED = "push_enabled"
    }
}