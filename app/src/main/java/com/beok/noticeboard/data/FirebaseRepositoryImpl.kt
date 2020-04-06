package com.beok.noticeboard.data

import android.net.Uri
import androidx.core.os.bundleOf
import com.beok.noticeboard.data.service.FirebaseService
import com.beok.noticeboard.model.DayLife
import com.beok.noticeboard.utils.await
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.Query
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val service: FirebaseService
) : FirebaseRepository {

    override suspend fun downloadProfileImage(
        onComplete: (Uri?) -> Unit,
        onFailure: (Exception?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val profileImgFileName = service.firebaseAuth.currentUser?.email ?: "Anonymous"
        val profileStorageRef = service.firebaseStorage.reference
            .child("profile")
            .child("$profileImgFileName.jpg")

        try {
            val uri = profileStorageRef.downloadUrl.await()
            onComplete(uri)
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    override fun getProfileName(): String =
        service.firebaseAuth.currentUser?.displayName ?: "Anonymous"

    override suspend fun requestDayLife(
        onComplete: (List<DayLife>?) -> Unit,
        onFailure: (Exception?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val dayLifeFirestoreRef = service.firebaseFirestore.collection("daylife")
        val dayLifeContents = mutableListOf<DayLife>()

        try {
            val snapShot =
                dayLifeFirestoreRef.orderBy("date", Query.Direction.DESCENDING).get().await()
            for (dayLife in snapShot) {
                dayLifeContents.add(dayLife.toObject(DayLife::class.java))
                if (dayLifeContents.size == snapShot.size()) {
                    onComplete(dayLifeContents)
                }
            }
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    override suspend fun updateProfileImage(
        uri: Uri,
        onComplete: (Boolean) -> Unit,
        onFailure: (Exception?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val profileImgFileName = service.firebaseAuth.currentUser?.email ?: "Anonymous"
        val profileStorageRef = service.firebaseStorage.reference
            .child("profile")
            .child("$profileImgFileName.jpg")

        try {
            profileStorageRef.putFile(uri).await()
            val downloadUri = profileStorageRef.downloadUrl.await()
            updateFirebaseProfile(downloadUri, onFailure, onComplete)
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    override suspend fun postDayLife(
        uriList: List<Uri>,
        posts: String,
        onComplete: (Boolean) -> Unit,
        onFailure: (Exception?) -> Unit
    ) = withContext(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis().toString()
        val urlList = mutableListOf<String>()

        try {
            uriList.forEachIndexed { index, uri ->
                val targetStorageRef = service.firebaseStorage.reference
                    .child("daylife")
                    .child(currentTime)
                    .child("$index.jpg")

                targetStorageRef.putFile(uri).await()
                val downloadUri = targetStorageRef.downloadUrl.await()
                if (index + 1 <= uriList.size - 1) {
                    urlList.add(downloadUri.toString())
                    return@forEachIndexed
                }

                urlList.add(downloadUri.toString())
                val dayLife = DayLife(date = currentTime, imageUrls = urlList, posts = posts)
                uploadFireStore(currentTime, dayLife, onComplete)
            }
        } catch (e: Exception) {
            onFailure(e)
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

    override fun registerFCMToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
            })
    }

    private suspend fun updateFirebaseProfile(
        uri: Uri,
        onFailure: (Exception?) -> Unit,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {
        val request = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()

        try {
            service.firebaseAuth.currentUser?.updateProfile(request)?.await()
            onComplete(true)
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    private suspend fun uploadFireStore(
        currentTime: String,
        dayLife: DayLife,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            service.firebaseFirestore
                .collection("daylife")
                .document(currentTime)
                .set(dayLife)
                .await()
            onComplete(true)
        } catch (e: Exception) {
            onComplete(false)
        }
    }
}