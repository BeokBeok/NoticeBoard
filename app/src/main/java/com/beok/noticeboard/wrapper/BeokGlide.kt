package com.beok.noticeboard.wrapper

import android.net.Uri
import android.widget.ImageView
import com.beok.noticeboard.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

object BeokGlide {

    fun showImageForCenterCrop(iv: ImageView, uri: Uri) {
        Glide.with(iv.context)
            .load(uri)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop()
            .into(iv)
    }

    fun showImage(iv: ImageView, uri: Uri) {
        Glide.with(iv.context)
            .load(uri)
            .placeholder(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)
    }
}