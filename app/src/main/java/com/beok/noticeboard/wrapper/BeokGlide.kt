package com.beok.noticeboard.wrapper

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object BeokGlide {

    fun showImageForCenterCrop(iv: ImageView, uri: Uri) {
        Glide.with(iv.context)
            .load(uri)
            .placeholder(android.R.mipmap.sym_def_app_icon)
            .centerCrop()
            .into(iv)
    }

    fun showImage(iv: ImageView, uri: Uri) {
        Glide.with(iv.context)
            .load(uri)
            .placeholder(android.R.mipmap.sym_def_app_icon)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)
    }
}