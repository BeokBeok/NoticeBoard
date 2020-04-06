package com.beok.noticeboard.data

import android.net.Uri
import com.beok.noticeboard.model.DayLife

interface FirebaseRepository {

    suspend fun downloadProfileImage(onComplete: (Uri?) -> Unit, onFailure: (Exception?) -> Unit)

    fun getProfileName(): String

    suspend fun requestDayLife(
        onComplete: (List<DayLife>?) -> Unit,
        onFailure: (Exception?) -> Unit
    )

    suspend fun updateProfileImage(
        uri: Uri,
        onComplete: (Boolean) -> Unit,
        onFailure: (Exception?) -> Unit
    )

    suspend fun postDayLife(
        uriList: List<Uri>,
        posts: String,
        onComplete: (Boolean) -> Unit,
        onFailure: (Exception?) -> Unit
    )

    fun startEventLog(itemId: String, itemName: String, contentType: String)

    fun registerFCMToken()
}