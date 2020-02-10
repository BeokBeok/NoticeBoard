package com.beok.noticeboard.base

import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<VDB : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
    parent: ViewGroup,
    private val bindingId: Int?,
    private val viewModels: ArrayMap<Int?, BaseViewModel>? = null
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(layoutRes, parent, false)
) {
    protected val binding: VDB = DataBindingUtil.bind(itemView)!!

    open fun bindViewHolder(item: Any?) {
        setupItemBinding(item)
        setupViewModelsBinding()
    }

    private fun setupViewModelsBinding() {
        if (viewModels == null) return
        for (key in viewModels.keys) {
            if (key == null) continue
            binding.setVariable(key, viewModels[key])
        }
    }

    private fun setupItemBinding(item: Any?) {
        if (bindingId == null || item == null) return
        binding.setVariable(bindingId, item)
    }
}