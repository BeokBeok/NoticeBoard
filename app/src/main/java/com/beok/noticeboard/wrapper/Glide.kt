package com.beok.noticeboard.wrapper

import android.net.Uri
import android.widget.ImageView
import com.beok.noticeboard.R
import com.bumptech.glide.Glide

object Glide {

    fun showImageForCircleCrop(iv: ImageView, uri: Uri) {
        Glide.with(iv.context)
            .load(uri)
            .placeholder(R.mipmap.ic_launcher)
            .circleCrop()
            .into(iv)
    }
}