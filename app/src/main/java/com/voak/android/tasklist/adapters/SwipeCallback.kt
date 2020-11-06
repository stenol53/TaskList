package com.voak.android.tasklist.adapters

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.voak.android.tasklist.R
import com.voak.android.tasklist.db.entities.Task

class SwipeCallback(
    private val adapter: ItemTouchHelperAdapter,
    private val swipeCallback: ISwipeCallback
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.LEFT) {
            val position = viewHolder.adapterPosition
            val item = adapter.getSwipedItem(position)
            swipeCallback.onSwipeLeft(item)

//            Snackbar.make(viewHolder.itemView, "Задание удалено.", Snackbar.LENGTH_LONG)
//                .setAction("Отменить", View.OnClickListener {
//
//                }).show()

        } else if (direction == ItemTouchHelper.RIGHT) {
            val position = viewHolder.adapterPosition
            val item = adapter.getSwipedItem(position)
            swipeCallback.onSwipeRight(item)
        }
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
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        var icon: Drawable?
        var background: ColorDrawable
        if (dX < 0) {
            icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_delete_forever_24)
            background = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.colorSwipeLeft));
        } else {
            icon = ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_check_24)
            background = ColorDrawable(ContextCompat.getColor(itemView.context, R.color.colorSwipeRight));
        }

        icon?.let { ic ->
//            val iconMargin: Int = (itemView.height - ic.intrinsicHeight) / 2
            val iconMargin: Int = 40
            val iconTop: Int = itemView.top + (itemView.height - ic.intrinsicHeight) / 2
            val iconBottom: Int = iconTop + ic.intrinsicHeight

            if (dX < 0) { // Swiping to the left
                val iconLeft: Int = itemView.right - iconMargin - ic.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                ic.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(
                    itemView.right + dX.toInt() - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom
                )
            } else {
                val iconLeft: Int = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + ic.intrinsicWidth
                ic.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(
                    itemView.left,
                    itemView.top, itemView.left + dX.toInt(), itemView.bottom
                )
            }

            background.draw(c);
            ic.draw(c);
        }
    }
}