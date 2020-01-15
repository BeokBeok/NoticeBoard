package com.beok.noticeboard.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beok.noticeboard.model.DayLife
import com.beok.noticeboard.profile.ProfileAdapter
import com.beok.noticeboard.profile.ProfileItemAdapter

@BindingAdapter("replaceItem")
fun RecyclerView.replaceItem(items: List<DayLife>?) {
    (this.adapter as? ProfileAdapter)?.run {
        replaceItem(items)
        notifyDataSetChanged()
    }
}

@BindingAdapter("replaceImageItem")
fun RecyclerView.replaceImageItem(items: List<String>?) {
    (this.adapter as? ProfileItemAdapter)?.run {
        replaceItem(items)
        notifyDataSetChanged()
    }
}