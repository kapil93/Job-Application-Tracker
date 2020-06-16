package com.kapil.presentation.jobs.addoredit

import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ScaleDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kapil.domain.entity.Event
import com.kapil.presentation.R
import com.kapil.presentation.common.formatDateTime
import kotlinx.android.synthetic.main.job_event_list_item.view.*


class JobEventListAdapter(
    private val mode: Mode
) : ListAdapter<Event, JobEventListAdapter.JobEventViewHolder>(DIFF_CALLBACK) {

    var onItemClickListener: ((Int) -> Unit)? = null
    var onItemRemoveRequestListener: ((Int) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Event>() {
            // Since Event doesn't have an id there is no good way of telling whether both items are
            // same or not
            override fun areItemsTheSame(oldItem: Event, newItem: Event) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Event, newItem: Event) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = JobEventViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.job_event_list_item, parent, false)
            .apply { setTimelineViewDrawable(viewType) }
    )

    override fun onBindViewHolder(holder: JobEventViewHolder, position: Int) =
        holder.bindTo(getItem(position)!!)

    override fun getItemViewType(position: Int) = when {
        itemCount == 1 -> R.drawable.timeline_only
        position == 0 -> R.drawable.timeline_first
        position == (itemCount - 1) -> R.drawable.timeline_last
        else -> R.drawable.timeline_mid
    }

    private fun View.setTimelineViewDrawable(drawableResId: Int) {
        timelineView.setImageDrawable(context.getDrawable(drawableResId)!!.apply {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                (this as? LayerDrawable)?.findDrawableByLayerId(R.id.bg_line)?.apply {
                    (this as? ScaleDrawable)?.apply { level = 1 }
                }
            }
        })
    }

    inner class JobEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.title
        private val startTime: TextView = itemView.startTime
        private val endTime: TextView = itemView.endTime
        private val notes: TextView = itemView.notes
        private val closeButton: ImageButton = itemView.closeButton

        fun bindTo(item: Event) {
            title.text = item.title
            startTime.text = item.startTime.formatDateTime()
            endTime.text = item.endTime?.formatDateTime()
            endTime.isVisible = item.endTime != null
            notes.text = item.notes
            notes.isVisible = !item.notes.isNullOrBlank()
            itemView.setOnClickListener { onItemClickListener?.invoke(adapterPosition) }
            closeButton.setOnClickListener {
                onItemRemoveRequestListener?.invoke(adapterPosition)
                it.setOnClickListener(null)
            }
            closeButton.isVisible = when (mode) {
                Mode.READ -> false
                Mode.EDIT -> true
            }
        }
    }

    enum class Mode {
        // Delete option disabled
        READ,

        // Delete option enabled
        EDIT
    }
}