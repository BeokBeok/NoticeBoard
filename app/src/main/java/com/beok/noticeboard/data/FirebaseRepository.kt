package com.beok.noticeboard.data

import android.net.Uri
import com.beok.noticeboard.model.DayLife

interface FirebaseRepository {

    fun downloadProfileImage(onComplete: (Uri?) -> Unit, onFailure: (Exception?) -> Unit)

    fun getProfileName(): String

    fun requestDayLife(
        onComplete: (List<DayLife>?) -> Unit,
        onFailure: (Exception?) -> Unit
    )

    fun updateProfileImage(uri: Uri, onComplete: (Boolean) -> Unit, onFailure: (Exception?) -> Unit)

    fun postDayLife(
        uriList: List<Uri>,
        posts: String,
        onComplete: (Boolean) -> Unit,
        onFailure: (Exception?) -> Unit
    )

    fun startEventLog(itemId: String, itemName: String, contentType: String)
}