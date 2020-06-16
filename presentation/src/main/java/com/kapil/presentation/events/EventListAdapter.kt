package com.kapil.presentation.events

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kapil.presentation.R
import kotlinx.android.synthetic.main.event_list_item.view.*

class EventListAdapter :
    ListAdapter<EventListItem, EventListAdapter.EventViewHolder>(DIFF_CALLBACK) {

    var onItemClickListener: ((Int) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventListItem>() {
            override fun areItemsTheSame(oldItem: EventListItem, newItem: EventListItem) =
                oldItem.jobId == newItem.jobId

            override fun areContentsTheSame(oldItem: EventListItem, newItem: EventListItem) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EventViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.event_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) =
        holder.bindTo(getItem(position)!!)

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.title
        private val company: TextView = itemView.company
        private val role: TextView = itemView.role
        private val companyAddress: TextView = itemView.companyAddress
        private val startTime: TextView = itemView.startTime
        private val endTime: TextView = itemView.endTime
        private val notes: TextView = itemView.notes

        fun bindTo(item: EventListItem) {
            title.text = item.title
            company.text = item.companyName
            role.text = item.role
            companyAddress.text = item.companyAddress
            startTime.text = item.startTime
            endTime.text = item.endTime
            notes.text = item.notes
            itemView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }
        }
    }
}