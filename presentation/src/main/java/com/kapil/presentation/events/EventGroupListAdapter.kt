package com.kapil.presentation.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kapil.domain.entity.Event
import com.kapil.presentation.R
import com.kapil.presentation.jobs.addoredit.JobEventListAdapter
import kotlinx.android.synthetic.main.event_group_list_item.view.*

class EventGroupListAdapter :
    ListAdapter<EventGroupListItem, EventGroupListAdapter.EventGroupViewHolder>(DIFF_CALLBACK) {

    var onEventItemClickListener: ((Event) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventGroupListItem>() {
            override fun areItemsTheSame(oldItem: EventGroupListItem, newItem: EventGroupListItem) =
                oldItem.jobId == newItem.jobId

            override fun areContentsTheSame(oldItem: EventGroupListItem, newItem: EventGroupListItem) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EventGroupViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.event_group_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: EventGroupViewHolder, position: Int) =
        holder.bindTo(getItem(position)!!)

    inner class EventGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val role: TextView = itemView.role
        private val company: TextView = itemView.company
        private val location: TextView = itemView.location
        private val eventsListView: RecyclerView = itemView.eventsListView

        fun bindTo(item: EventGroupListItem) {
            role.text = item.role
            company.text = item.companyName
            location.text = item.companyAddress
            eventsListView.adapter = JobEventListAdapter(JobEventListAdapter.Mode.READ).apply {
                submitList(item.events.toMutableList())
                onItemClickListener = { onEventItemClickListener?.invoke(item.events[it]) }
            }
        }
    }
}