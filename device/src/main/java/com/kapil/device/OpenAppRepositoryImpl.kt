package com.kapil.device

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import com.kapil.domain.entity.Event
import com.kapil.domain.openapp.OpenAppRepository
import io.reactivex.rxjava3.core.Completable

class OpenAppRepositoryImpl(private val context: Context) : OpenAppRepository {

    override fun openEmailClient(emailId: String): Completable = openAppUsingImplicitIntent(
        Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", emailId, null))
    )

    override fun openDialer(phoneNumber: String): Completable = openAppUsingImplicitIntent(
        Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:$phoneNumber") }
    )

    override fun openMaps(address: String): Completable = openAppUsingImplicitIntent(
        Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$address"))
    )

    override fun openBrowser(url: String): Completable = openAppUsingImplicitIntent(
        Intent(Intent.ACTION_VIEW, Uri.parse(url))
    )

    override fun openCalendar(event: Event): Completable = openAppUsingImplicitIntent(
        Intent(Intent.ACTION_EDIT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, event.title)
            putExtra(CalendarContract.Events.DESCRIPTION, event.notes)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.startTime.time)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.endTime?.time)
        }
    )

    private fun openAppUsingImplicitIntent(intent: Intent) = Completable.fromAction {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}