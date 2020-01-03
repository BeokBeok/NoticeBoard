package com.beok.noticeboard.ext

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.beok.noticeboard.wrapper.Glide
import gun0912.tedimagepicker.builder.TedImagePicker

@BindingAdapter("showImagePicker")
fun ImageView.showImagePicker(imgUpload: (uri: Uri) -> Unit) {
    setOnClickListener {
        TedImagePicker.with(this.context)
            .start { uri -> imgUpload.invoke(uri) }
    }
}

@BindingAdapter("srcWithGlide")
fun ImageView.srcWithGlide(uri: Uri?) {
    if (uri == null) return
    Glide.showImage(this, uri)
}