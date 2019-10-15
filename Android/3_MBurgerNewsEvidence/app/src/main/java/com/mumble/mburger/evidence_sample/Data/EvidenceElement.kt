package com.mumble.mburger.evidence_sample.Data

import java.io.Serializable

class EvidenceElement : Serializable {

    var id: Long = -1
    var block_id: Long = -1
    var section_id: Long = -1
    var title: String? = null
    var image: String? = null

    constructor(id: Long, block_id: Long, section_id: Long, title: String?, image: String?) {
        this.id = id
        this.block_id = block_id
        this.section_id = section_id
        this.title = title
        this.image = image
    }
}
