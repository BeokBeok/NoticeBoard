package com.beok.noticeboard.utils

import android.content.Intent

sealed class ActivityCommand {

    data class StartActivityForResult(val intent: Intent?, val requestCode: Int) : ActivityCommand()
}