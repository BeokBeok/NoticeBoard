package com.beok.noticeboard.ext

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.beok.noticeboard.profile.ProfileAdapter
import com.beok.noticeboard.profile.model.DayLife

@BindingAdapter("replaceItem")
fun RecyclerView.replaceItem(items: List<DayLife>?) {
    (this.adapter as? ProfileAdapter)?.run {
        replaceItem(items)
        notifyDataSetChanged()
    }
}