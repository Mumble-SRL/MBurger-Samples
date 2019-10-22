package com.mumble.mburger.relation_sample.Data

import mumble.mburger.sdk.MBClient.MBData.MBElements.MBImages
import mumble.mburger.sdk.MBClient.MBData.MBElements.MBRelationElement
import java.io.Serializable

class Author : Serializable {

    var name: String? = null
    var photo: MBImages? = null
    var about_me: String? = null

    constructor()
}