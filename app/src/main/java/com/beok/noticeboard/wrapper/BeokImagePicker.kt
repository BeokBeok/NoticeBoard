package com.beok.noticeboard.wrapper

import android.content.Context
import android.net.Uri
import gun0912.tedimagepicker.builder.TedImagePicker

object BeokImagePicker {

    fun showImagePicker(context: Context, imgUpload: (uri: Uri) -> Unit) =
        TedImagePicker.with(context)
            .start { imgUpload.invoke(it) }

    fun showMultiImagePicker(context: Context, imgListUpload: (uriList: List<Uri>) -> Unit) =
        TedImagePicker.with(context)
            .startMultiImage { imgListUpload.invoke(it) }
}