package com.beok.noticeboard.data

import android.net.Uri
import androidx.core.os.bundleOf
import com.beok.noticeboard.data.service.FirebaseService
import com.beok.noticeboard.model.DayLife
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.Query
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

        dayLifeFirestoreRef.orderBy("date", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { wholeDayLifeList ->
                for (dayLife in wholeDayLifeList) {
                    dayLifeContents.add(dayLife.toObject(DayLife::class.java))

                    if (dayLifeContents.size == wholeDayLifeList.size()) {
                        onComplete(dayLifeContents)
                    }
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

    override fun postDayLife(
        uriList: List<Uri>,
        posts: String,
        onComplete: (Boolean) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        val currentTime = System.currentTimeMillis().toString()
        val urlList = mutableListOf<String>()

        uriList.forEachIndexed { index, uri ->
            val targetStorageRef = service.firebaseStorage.reference
                .child("daylife")
                .child(currentTime)
                .child("$index.jpg")

            targetStorageRef.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        onFailure(task.exception)
                        return@continueWithTask null
                    }
                    targetStorageRef.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        onFailure(task.exception)
                        return@addOnCompleteListener
                    }
                    if (index + 1 <= uriList.size - 1) {
                        urlList.add(task.result?.toString() ?: "")
                        return@addOnCompleteListener
                    }
                    urlList.add(task.result?.toString() ?: "")
                    val dayLife = DayLife(date = currentTime, imageUrls = urlList, posts = posts)
                    service.firebaseFirestore
                        .collection("daylife")
                        .document(currentTime)
                        .set(dayLife)
                        .addOnSuccessListener { onComplete(true) }
                        .addOnCanceledListener { onComplete(false) }
                }
        }
    }

    override fun startEventLog(itemId: String, itemName: String, contentType: String) {
        service.firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.SELECT_CONTENT,
            bundleOf(
                FirebaseAnalytics.Param.ITEM_ID to itemId,
                FirebaseAnalytics.Param.ITEM_NAME to itemName,
                FirebaseAnalytics.Param.CONTENT_TYPE to contentType
            )
        )
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