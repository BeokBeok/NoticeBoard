package com.beok.noticeboard.main

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.beok.noticeboard.BR
import com.beok.noticeboard.R
import com.beok.noticeboard.common.BaseAdapter
import com.beok.noticeboard.common.BaseViewHolder
import com.beok.noticeboard.databinding.RvMainDaylifeItemBinding
import com.beok.noticeboard.databinding.RvMainItemBinding
import com.beok.noticeboard.model.DayLife

class MainAdapter(
    @LayoutRes
    private val layoutRes: Int = R.layout.rv_main_item,
    private val bindingId: Int? = BR.daylife
) : BaseAdapter<DayLife, RvMainItemBinding>(layoutRes, bindingId) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<RvMainItemBinding> = ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) :
        BaseViewHolder<RvMainItemBinding>(layoutRes, parent, bindingId) {

        override fun bindViewHolder(item: Any?) {
            super.bindViewHolder(item)

            binding.rvDaylife.adapter = object : BaseAdapter<String, RvMainDaylifeItemBinding>(
                R.layout.rv_main_daylife_item, BR.imgUrl
            ) {}
        }
    }
}