package com.mumble.mburger.news_sample.Data

import mumble.mburger.sdk.MBClient.MBData.MBElements.MBImages
import java.io.Serializable

class News : Serializable {

    var title: String? = null
    var content: String? = null
    var images: MBImages? = null
    var category: String? = null
    var creation_date: Long = -1L

    constructor()
}
