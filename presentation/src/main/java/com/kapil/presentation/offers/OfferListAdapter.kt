package com.kapil.presentation.offers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kapil.presentation.R
import com.kapil.presentation.common.formatDate
import kotlinx.android.synthetic.main.offer_list_item.view.*

class OfferListAdapter :
    ListAdapter<OfferListItem, OfferListAdapter.OfferViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<OfferListItem>() {
            override fun areItemsTheSame(oldItem: OfferListItem, newItem: OfferListItem) =
                oldItem.jobId == newItem.jobId

            override fun areContentsTheSame(oldItem: OfferListItem, newItem: OfferListItem) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OfferViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.offer_list_item, parent, false)
    )

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) =
        holder.bindTo(getItem(position)!!)

    inner class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val company: TextView = itemView.company
        private val role: TextView = itemView.role
        private val amount: TextView = itemView.amount
        private val doj: TextView = itemView.doj
        private val notes: TextView = itemView.notes

        fun bindTo(item: OfferListItem) {
            company.text = item.companyName
            role.text = item.role
            amount.text = item.amount
            doj.text = item.dateOfJoining.formatDate()
            notes.text = item.notes
        }
    }
}