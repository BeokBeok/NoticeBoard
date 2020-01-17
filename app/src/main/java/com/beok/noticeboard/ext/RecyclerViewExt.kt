package com.beok.noticeboard.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beok.noticeboard.base.BaseAdapter

@BindingAdapter("replaceItem")
fun RecyclerView.replaceItem(items: List<Any>?) {
    @Suppress("UNCHECKED_CAST")
    (this.adapter as? BaseAdapter<Any, *>)?.run {
        replaceItems(items)
        notifyDataSetChanged()
    }
}