package com.beok.noticeboard.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beok.noticeboard.main.MainAdapter
import com.beok.noticeboard.main.MainItemAdapter
import com.beok.noticeboard.model.DayLife

@BindingAdapter("replaceItem")
fun RecyclerView.replaceItem(items: List<DayLife>?) {
    (this.adapter as? MainAdapter)?.run {
        replaceItem(items)
        notifyDataSetChanged()
    }
}

@BindingAdapter("replaceImageItem")
fun RecyclerView.replaceImageItem(items: List<String>?) {
    (this.adapter as? MainItemAdapter)?.run {
        replaceItem(items)
        notifyDataSetChanged()
    }
}