package com.kapil.presentation.jobs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kapil.presentation.R
import com.kapil.presentation.common.getResIdFromPriority
import com.kapil.presentation.common.getResIdFromStatus
import com.kapil.presentation.common.getStatusColorResId
import kotlinx.android.synthetic.main.job_list_item.view.*

class JobListAdapter : ListAdapter<JobListItem, JobListAdapter.JobViewHolder>(DIFF_CALLBACK) {

    var onItemClickListener: (JobListItem.() -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<JobListItem>() {
            override fun areItemsTheSame(oldItem: JobListItem, newItem: JobListItem) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: JobListItem, newItem: JobListItem) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = JobViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.job_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) =
        holder.bindTo(getItem(position)!!)

    override fun getItemId(position: Int): Long = getItem(position)!!.id

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.title
        private val company: TextView = itemView.company
        private val priority: TextView = itemView.priority
        private val location: TextView = itemView.location
        private val contact: TextView = itemView.contact
        private val status: TextView = itemView.status

        fun bindTo(item: JobListItem) {
            title.text = item.role
            company.text = item.companyName
            priority.setText(item.priority.getResIdFromPriority())
            location.text = item.address
            contact.text = item.contactName
            status.setText(item.status.getResIdFromStatus())
            status.setTextColor(
                ContextCompat.getColor(itemView.context, item.status.getStatusColorResId())
            )

            itemView.setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }
}