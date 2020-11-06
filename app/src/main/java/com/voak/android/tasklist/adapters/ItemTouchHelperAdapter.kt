package com.voak.android.tasklist.adapters

import com.voak.android.tasklist.db.entities.Task

interface ItemTouchHelperAdapter {
    fun getSwipedItem(position: Int): Task
}