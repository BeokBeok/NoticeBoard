package com.beok.noticeboard.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<VDB : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    parent: ViewGroup,
    private val bindingId: Int?
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(layoutRes, parent, false)
) {
    protected val binding: VDB = DataBindingUtil.bind(itemView)!!

    open fun bindViewHolder(item: Any?) {
        if (bindingId == null || item == null) return

        binding.run { setVariable(bindingId, item) }
    }
}