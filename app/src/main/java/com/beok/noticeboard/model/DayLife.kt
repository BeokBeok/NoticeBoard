package com.beok.noticeboard.model

import android.net.Uri

data class DayLife(
    val date: String = "",
    val imageUriList: List<Uri?> = listOf(),
    val imgCnt: Int = 0,
    val posts: String = ""
)