package com.kapil.data.common.realmmodels

import io.realm.RealmObject
import java.util.*

open class EventDb(
    var title: String = "",
    var startTime: Date = Date(0),
    var endTime: Date? = null,
    var notes: String? = null
) : RealmObject()