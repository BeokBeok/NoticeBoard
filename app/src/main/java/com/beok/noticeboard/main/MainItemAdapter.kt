package com.beok.noticeboard.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.beok.noticeboard.BR
import com.beok.noticeboard.R
import com.beok.noticeboard.databinding.RvMainDaylifeItemBinding

class MainItemAdapter(
    @LayoutRes
    private val layoutRes: Int = R.layout.rv_main_daylife_item,
    private val bindingId: Int? = BR.imgUrl
) : RecyclerView.Adapter<MainItemAdapter.ViewHolder>() {

    private val items = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindViewHolder(items[position])

    fun replaceItem(item: List<String>?) {
        if (item == null) return
        items.run {
            clear()
            addAll(item)
        }
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(layoutRes, parent, false)
    ) {

        private val binding: RvMainDaylifeItemBinding = DataBindingUtil.bind(itemView)!!

        fun bindViewHolder(item: String?) {
            if (item == null) return
            if (bindingId == null) return

            binding.run { setVariable(bindingId, item) }
        }
    }
}