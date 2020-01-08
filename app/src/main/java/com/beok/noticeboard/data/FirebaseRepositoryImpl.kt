package com.beok.noticeboard.data

import android.net.Uri
import com.beok.noticeboard.data.service.FirebaseService
import com.beok.noticeboard.profile.model.DayLife
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.QueryDocumentSnapshot
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val service: FirebaseService
) : FirebaseRepository {

    override fun downloadProfileImage(
        onComplete: (Uri?) -> Unit,
        onFailure: (Exception?) -> Unit
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

    override fun requestDayLife(
        onComplete: (List<DayLife>?) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        val dayLifeFirestoreRef = service.firebaseFirestore.collection("daylife")
        val dayLifeContents = mutableListOf<DayLife>()

        dayLifeFirestoreRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    downloadDayLifeImg(
                        document,
                        result.size(),
                        dayLifeContents,
                        onComplete,
                        onFailure
                    )
                }
            }
            .addOnFailureListener { onFailure(it) }
    }

    override fun updateProfileImage(
        uri: Uri,
        onComplete: (Boolean) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        val profileImgFileName = service.firebaseAuth.currentUser?.email ?: "Anonymous"
        val profileStorageRef = service.firebaseStorage.reference
            .child("profile")
            .child("$profileImgFileName.jpg")

        profileStorageRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    onFailure(task.exception)
                    return@continueWithTask null
                }
                profileStorageRef.downloadUrl
            }
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onFailure(task.exception)
                    return@addOnCompleteListener
                }
                updateFirebaseProfile(task, onFailure, onComplete)
            }
    }

    private fun downloadDayLifeImg(
        document: QueryDocumentSnapshot,
        totalCnt: Int,
        dayLifeContents: MutableList<DayLife>,
        onComplete: (List<DayLife>?) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        val dayLifeStorageRef = service.firebaseStorage.reference.child("daylife")

        dayLifeStorageRef.child(document.id).child("0.jpg").downloadUrl
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    onFailure(task.exception)
                    return@addOnCompleteListener
                }
                dayLifeContents.add(
                    DayLife(task.result, document.data["posts"].toString())
                )
                if (totalCnt == dayLifeContents.size) {
                    onComplete(dayLifeContents)
                }
            }
            .addOnFailureListener { onFailure(it) }
    }

    private fun updateFirebaseProfile(
        task: Task<Uri>,
        onFailure: (Exception?) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        val request = UserProfileChangeRequest.Builder()
            .setPhotoUri(task.result)
            .build()
        service.firebaseAuth.currentUser?.updateProfile(request)
            ?.addOnCompleteListener { profileTask ->
                if (!profileTask.isSuccessful) {
                    onFailure(task.exception)
                    return@addOnCompleteListener
                }
                onComplete(true)
            }
            ?.addOnFailureListener { onFailure(it) }
    }
}