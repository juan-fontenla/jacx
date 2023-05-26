package com.apm.jacx.adapter

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemSwiped(position: Int)
}
