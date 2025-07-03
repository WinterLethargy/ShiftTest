package com.example.user

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.core.dpToPx

class UserItemDecoration: RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = 16.dpToPx.toInt()
        outRect.right = 16.dpToPx.toInt()
        outRect.top = 10.dpToPx.toInt()
    }
}