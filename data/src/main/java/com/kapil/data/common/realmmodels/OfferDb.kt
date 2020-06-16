package com.kapil.data.common.realmmodels

import io.realm.RealmObject
import java.util.*

open class OfferDb(
    var amount: String = "",
    var dateOfJoining: Date = Date(0),
    var notes: String? = null
) : RealmObject()