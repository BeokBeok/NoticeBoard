package com.beok.noticeboard.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.beok.noticeboard.BR
import com.beok.noticeboard.R
import com.beok.noticeboard.databinding.RvMainItemBinding
import com.beok.noticeboard.model.DayLife

class MainAdapter(
    @LayoutRes
    private val layoutRes: Int = R.layout.rv_main_item,
    private val bindingId: Int? = BR.daylife
) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val items = mutableListOf<DayLife>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindViewHolder(items[position])

    fun replaceItem(item: List<DayLife>?) {
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
        private val binding: RvMainItemBinding = DataBindingUtil.bind(itemView)!!

        fun bindViewHolder(item: DayLife?) {
            if (bindingId == null) return
            if (item == null) return

            binding.run {
                setVariable(bindingId, item)
                rvDaylife.adapter = MainItemAdapter()
            }

        }
    }
}