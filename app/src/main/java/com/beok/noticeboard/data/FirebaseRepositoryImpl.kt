package com.beok.noticeboard.data

import android.net.Uri
import com.beok.noticeboard.data.service.FirebaseService
import com.beok.noticeboard.model.DayLife
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.UserProfileChangeRequest
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
            .addOnSuccessListener { wholeDayLifeList ->
                for (dayLife in wholeDayLifeList) {
                    loadDayLife(
                        dayLife.toObject(DayLife::class.java),
                        wholeDayLifeList.size(),
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

    override fun postDayLife(
        uriList: List<Uri>,
        posts: String,
        onComplete: (Boolean) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        val currentTime = System.currentTimeMillis().toString()
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
                    if (index + 1 != uriList.size) return@addOnCompleteListener
                    val dayLife = DayLife(date = currentTime, imgCnt = uriList.size, posts = posts)
                    service.firebaseFirestore
                        .collection("daylife")
                        .document(currentTime)
                        .set(dayLife)
                        .addOnSuccessListener { onComplete(true) }
                        .addOnCanceledListener { onComplete(false) }
                }
        }
    }

    private fun loadDayLife(
        dayLife: DayLife,
        postedWholeSize: Int,
        dayLifeContents: MutableList<DayLife>,
        onComplete: (List<DayLife>?) -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        val dayLifeStorageRef = service.firebaseStorage.reference.child("daylife")
        val imgUriList = mutableListOf<Uri?>()

        for (i in 0 until dayLife.imgCnt) {
            dayLifeStorageRef.child(dayLife.date).child("$i.jpg").downloadUrl
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        onFailure(task.exception)
                        return@addOnCompleteListener
                    }
                    if (i != dayLife.imgCnt - 1) {
                        imgUriList.add(task.result)
                        return@addOnCompleteListener
                    }
                    dayLifeContents.add(
                        DayLife(
                            date = dayLife.date,
                            imageUriList = imgUriList,
                            posts = dayLife.posts
                        )
                    )
                    if (postedWholeSize == dayLifeContents.size) {
                        onComplete(dayLifeContents.sortedByDescending { it.date })
                    }
                }
                .addOnFailureListener { onFailure(it) }
        }
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