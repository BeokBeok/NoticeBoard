package com.beok.noticeboard.data

import android.net.Uri

interface FirebaseRepository {

    fun downloadProfileImage(onComplete: (uri: Uri?) -> Unit, onFailure: (e: Exception?) -> Unit)

    fun getProfileName(): String
}