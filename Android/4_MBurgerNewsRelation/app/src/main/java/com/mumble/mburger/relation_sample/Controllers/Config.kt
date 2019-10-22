package com.mumble.mburger.relation_sample.Controllers

class Config {
    companion object {

        /**Edit with your MBurger APi Key and News Block id**/
        val MBurgerAPiKey = ""
        val MPushKey = ""
        val PushTopic = ""

        val BLOCK_NEWS = -1L
        val BLOCK_AUTHOR = -1L

        //Category values
        const val CAT_SPORT = "sport"
        const val CAT_TECH = "tech"
        const val CAT_SOCIETY = "society"

        //News Elements names from MBurger
        const val ELEM_TITLE = "title"
        const val ELEM_CONTENT = "content"
        const val ELEM_IMAGES = "images"
        const val ELEM_CATEGORY = "category"
        const val ELEM_AUTHOR = "author"

        //Author Elements names from MBurger
        const val ELEM_AUTH_NAME = "name"
        const val ELEM_AUTH_PHOTO = "photo"
        const val ELEM_AUTH_ABOUT_ME = "about-me"

        const val PROPERTY_PUSH_ENABLED = "push_enabled"
    }
}