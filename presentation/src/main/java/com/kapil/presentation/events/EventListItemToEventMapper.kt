package com.kapil.presentation.events

import com.kapil.domain.entity.Event
import com.kapil.presentation.common.parseDateTime
import io.reactivex.rxjava3.functions.Function

class EventListItemToEventMapper : Function<EventListItem, Event> {

    override fun apply(item: EventListItem) = Event(
        title = item.title,
        startTime = item.startTime.parseDateTime()!!,
        endTime = item.endTime.parseDateTime(),
        notes = item.notes
    )
}