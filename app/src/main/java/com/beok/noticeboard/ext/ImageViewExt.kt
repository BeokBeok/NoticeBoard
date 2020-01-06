package com.beok.noticeboard.ext

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.beok.noticeboard.wrapper.BeokGlide
import com.beok.noticeboard.wrapper.BeokImagePicker

@BindingAdapter("showImagePicker")
fun ImageView.showImagePicker(imgUpload: (uri: Uri) -> Unit) =
    setOnClickListener { BeokImagePicker.showImagePicker(this.context, imgUpload) }

@BindingAdapter("showMultiImagePicker")
fun ImageView.showMultiImagePicker(imgListUpload: (uriList: List<Uri>) -> Unit) =
    setOnClickListener { BeokImagePicker.showMultiImagePicker(this.context, imgListUpload) }

@BindingAdapter("srcWithGlide")
fun ImageView.srcWithGlide(uri: Uri?) {
    if (uri == null) return
    BeokGlide.showImage(this, uri)
}