package com.beok.noticeboard.data

import android.net.Uri
import com.beok.noticeboard.data.service.FirebaseService
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val service: FirebaseService
) : FirebaseRepository {

    override fun downloadProfileImage(
        onComplete: (uri: Uri?) -> Unit,
        onFailure: (e: Exception?) -> Unit
    ) {
        val profileImgFileName = service.firebaseAuth.currentUser?.email ?: "Anonymous"
        val profileStorageRef = service.firebaseStorage.reference
            .child("profile")
            .child("$profileImgFileName.jpg")

        profileStorageRef.downloadUrl
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onFailure(task.exception)
                    return@addOnCompleteListener
                }
                onComplete(task.result)
            }
            .addOnFailureListener { onFailure(it) }
    }

    override fun getProfileName(): String =
        service.firebaseAuth.currentUser?.displayName ?: "Anonymous"
}