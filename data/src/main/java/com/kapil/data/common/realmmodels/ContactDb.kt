package com.kapil.data.common.realmmodels

import io.realm.RealmObject

open class ContactDb(
    var name: String? = null,
    var email: String? = null,
    var phone: String? = null
) : RealmObject()