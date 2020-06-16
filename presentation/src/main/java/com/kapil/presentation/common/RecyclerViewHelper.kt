package com.kapil.presentation.common

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kapil.presentation.R


fun getBottomMarginItemDecoration(bottomMargin: Int) =
    object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom =
                if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount - 1)
                    bottomMargin
                else 0
        }
    }

fun getItemSwipeDeleteHelper(onSwipedCallback: (viewHolder: RecyclerView.ViewHolder) -> Unit) =
    ItemTouchHelper(
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {

            private var isSwiped = false

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Action swipe initiated
                    isSwiped = false
                }
            }

            // Not expected to be called.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onSwipedCallback(viewHolder)
                isSwiped = true
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
                )

                if (isSwiped) return

                val itemView = viewHolder.itemView
                val icon = recyclerView.context.getDrawable(R.drawable.ic_delete)!!
                val iconPositionScalingFactor = 0.5

                when {
                    dX > 0 -> { // Swiping to the right
                        // (icon.intrinsicWidth / 4) is added as it is generally the icon padding
                        val rightBound =
                            itemView.left + (dX * iconPositionScalingFactor).toInt() + (icon.intrinsicWidth / 4)
                        icon.setBounds(
                            rightBound - icon.intrinsicWidth,
                            itemView.top + ((itemView.height - icon.intrinsicHeight) / 2),
                            rightBound,
                            itemView.bottom - ((itemView.height - icon.intrinsicHeight) / 2)
                        )
                    }
                    dX < 0 -> { // Swiping to the left
                        // (icon.intrinsicWidth / 4) is subtracted as it is generally the icon padding
                        val leftBound =
                            itemView.right + (dX * iconPositionScalingFactor).toInt() - (icon.intrinsicWidth / 4)
                        icon.setBounds(
                            leftBound,
                            itemView.top + ((itemView.height - icon.intrinsicHeight) / 2),
                            leftBound + icon.intrinsicWidth,
                            itemView.bottom - ((itemView.height - icon.intrinsicHeight) / 2)
                        )
                    }
                    else -> { // view is un-swiped
                        icon.setBounds(0, 0, 0, 0)
                    }
                }

                icon.draw(c)
            }
        }
    )