package com.kapil.data.common.realmmodels

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.where

open class JobDb(
    @PrimaryKey
    var id: Long = ID_ILLEGAL_VALUE,
    var role: String = "",
    var company: CompanyDb? = null,
    var contact: ContactDb? = null,
    var offer: OfferDb? = null,
    var events: RealmList<EventDb> = RealmList(),
    var appliedThrough: String? = null,
    var notes: String? = null
) : RealmObject() {

    object FIELDS {
        const val ID = "id"
        const val ROLE = "role"
        const val COMPANY = "company"
        const val STATUS = "statusStr"
        const val PRIORITY = "priorityStr"
        const val OFFER = "offer"
        const val EVENTS = "events"
    }

    companion object {
        @Ignore
        const val ID_ILLEGAL_VALUE = -1L

        @Ignore
        const val ID_FIRST_VALUE = 1L
    }

    private var priorityStr: String = PriorityDb.LOW.name
    var priority: PriorityDb
        get() = PriorityDb.values().first { it.name == priorityStr }
        set(value) {
            priorityStr = value.name
        }

    private var statusStr: String = StatusDb.TO_APPLY.name
    var status: StatusDb
        get() = StatusDb.values().first { it.name == statusStr }
        set(value) {
            statusStr = value.name
        }

    enum class PriorityDb {
        LOW,
        MEDIUM,
        HIGH,
        VERY_HIGH
    }

    enum class StatusDb {
        TO_APPLY,
        APPLIED,
        IN_PROCESS,
        DID_NOT_WORK_OUT,
        GOT_THE_JOB
    }

    fun assignId(realm: Realm): JobDb = apply {
        id = (realm.where<JobDb>().max(FIELDS.ID)?.toLong()?.inc() ?: ID_FIRST_VALUE)
            .takeIf { isNewJob() } ?: id
    }

    private fun isNewJob() = id == ID_ILLEGAL_VALUE
}