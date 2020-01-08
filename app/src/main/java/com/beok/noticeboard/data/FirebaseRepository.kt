package com.beok.noticeboard.data

import android.net.Uri
import com.beok.noticeboard.profile.model.DayLife

interface FirebaseRepository {

    fun downloadProfileImage(onComplete: (Uri?) -> Unit, onFailure: (Exception?) -> Unit)

    fun getProfileName(): String

    fun requestDayLife(
        onComplete: (List<DayLife>?) -> Unit,
        onFailure: (Exception?) -> Unit
    )

    fun updateProfileImage(uri: Uri, onComplete: (Boolean) -> Unit, onFailure: (Exception?) -> Unit)
}