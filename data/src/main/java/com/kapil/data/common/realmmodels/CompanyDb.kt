package com.kapil.data.common.realmmodels

import io.realm.RealmObject

open class CompanyDb(
    var name: String = "",
    var website: String? = null,
    var address: String? = null,
    var notes: String? = null
) : RealmObject() {

    object FIELDS {
        const val NAME = "name"
    }
}
