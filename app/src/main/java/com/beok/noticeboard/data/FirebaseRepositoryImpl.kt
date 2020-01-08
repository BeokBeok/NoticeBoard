package com.beok.noticeboard.data

import android.net.Uri
import com.beok.noticeboard.data.service.FirebaseService
import com.beok.noticeboard.profile.model.DayLife
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
        val (dayLifeStorageRef, dayLifeFirestoreRef) =
            service.firebaseStorage.reference.child("daylife") to
                    service.firebaseFirestore.collection("daylife")
        val dayLifeContents = mutableListOf<DayLife>()
        var totalCnt: Int

        dayLifeFirestoreRef.get()
            .addOnSuccessListener { result ->
                totalCnt = result.size()
                for (document in result) {
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
            }
            .addOnFailureListener { onFailure(it) }
    }
}