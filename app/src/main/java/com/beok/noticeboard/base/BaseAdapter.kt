package com.beok.noticeboard.base

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<ITEM : Any, VDB : ViewDataBinding>(
    @LayoutRes
    private val layoutRes: Int,
    private val bindingId: Int?
) : RecyclerView.Adapter<BaseViewHolder<VDB>>() {

    private val itemList = mutableListOf<ITEM>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VDB> =
        object : BaseViewHolder<VDB>(layoutRes, parent, bindingId) {}

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: BaseViewHolder<VDB>, position: Int) =
        holder.bindViewHolder(itemList[position])

    fun replaceItems(item: List<ITEM>?) {
        if (item == null) return

        itemList.run {
            clear()
            addAll(item)
        }
    }
}