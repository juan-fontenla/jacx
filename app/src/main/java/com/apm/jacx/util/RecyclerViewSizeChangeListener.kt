package com.apm.jacx.util

import androidx.recyclerview.widget.RecyclerView

// Detecta los cambios de tamaño en un recycler view
class RecyclerViewSizeChangeListener(private val listener: () -> Unit) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        listener.invoke()
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            listener.invoke()
        }
    }
}
