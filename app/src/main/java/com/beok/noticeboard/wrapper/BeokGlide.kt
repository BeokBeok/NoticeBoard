package com.beok.noticeboard.wrapper

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

object BeokGlide {

    fun showImageForCenterCrop(iv: ImageView, uri: Uri?) {
        Glide.with(iv.context)
            .load(uri)
            .placeholder(android.R.mipmap.sym_def_app_icon)
            .centerCrop()
            .into(iv)
    }
}