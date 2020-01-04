package com.beok.noticeboard.profile

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beok.noticeboard.profile.model.DayLife
import com.beok.noticeboard.utils.ActivityCommand
import com.beok.noticeboard.utils.Event
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : ViewModel() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser ?: error("User invalidate")
    private val spaceRef = FirebaseStorage.getInstance().reference
        .child("${firebaseUser.email}")
    private val profileSpaceRef = spaceRef
        .child("profile")
        .child("${firebaseUser.displayName}.jpg")
    private val dayLifeSpaceRef = spaceRef
        .child("daylife")
    private val dayLifeCollectionRef = FirebaseFirestore.getInstance()
        .collection("daylife")

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> get() = _imageUri

    private val _profileName = MutableLiveData<String>()
    val profileName: LiveData<String> get() = _profileName

    private val _startActivityForResultEvent = MutableLiveData<Event<ActivityCommand>>()
    val startActivityForResultEvent: LiveData<Event<ActivityCommand>>
        get() = _startActivityForResultEvent

    private val _dayLife = MutableLiveData<List<DayLife>>()
    val dayLife: LiveData<List<DayLife>> get() = _dayLife

    private val dayLifeItem = mutableListOf<DayLife>()

    private var totalItemSize: Int = 0
    private var currentItemCnt: Int = 0

    val imgUpload = fun(uri: Uri) {
        uploadProfileImage(uri)
    }

    val dayLifeActivityStart = fun(dayLifeIntent: Intent) {
        _startActivityForResultEvent.value =
            Event(ActivityCommand.StartActivityForResult(dayLifeIntent, REQ_POST_DAY_LIFE))
    }

    fun setupProfile() {
        showProgressbar()
        profileSpaceRef.downloadUrl
            .addOnCompleteListener { task ->
                hideProgressbar()
                if (!task.isSuccessful) {
                    showProfileImage(firebaseUser.photoUrl)
                    return@addOnCompleteListener
                }
                showProfileImage(task.result)
            }
        showProfileName()
    }

    fun refreshDayLife() {
        showProgressbar()
        dayLifeCollectionRef.get()
            .addOnSuccessListener { result ->
                dayLifeItem.clear()
                totalItemSize = result.count()
                for (document in result) {
                    takeDayLifeData(document)
                }
                hideProgressbar()
            }
            .addOnFailureListener { hideProgressbar() }
    }

    private fun takeDayLifeData(document: QueryDocumentSnapshot) {
        dayLifeSpaceRef.child("${document.id}.jpg").downloadUrl
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) return@addOnCompleteListener
                dayLifeItem.add(
                    DayLife(
                        task.result,
                        document.data["posts"].toString()
                    )
                )
                currentItemCnt++
                setDayLifeItem()
            }
    }

    private fun setDayLifeItem() {
        if (currentItemCnt == totalItemSize) {
            _dayLife.value =
                dayLifeItem.sortedByDescending { it.imageUrl.toString() }
            currentItemCnt = 0
        }
    }

    private fun showProfileName() {
        _profileName.value = firebaseUser.displayName
    }

    private fun uploadProfileImage(uri: Uri) {
        showProgressbar()
        profileSpaceRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    hideProgressbar()
                    error(task.exception ?: "")
                }
                profileSpaceRef.downloadUrl
            }
            .addOnCompleteListener { task ->
                hideProgressbar()
                if (!task.isSuccessful) return@addOnCompleteListener
                updateProfileImage(task, uri)
            }
    }

    private fun updateProfileImage(task: Task<Uri>, uri: Uri) {
        showProgressbar()
        val request = UserProfileChangeRequest.Builder()
            .setPhotoUri(task.result)
            .build()
        firebaseUser.updateProfile(request)
            .addOnCompleteListener { profileTask ->
                hideProgressbar()
                if (!profileTask.isSuccessful) return@addOnCompleteListener
                showProfileImage(uri)
            }
    }

    private fun showProfileImage(uri: Uri?) {
        _imageUri.value = uri
    }

    private fun hideProgressbar() {
        _isLoading.value = false
    }

    private fun showProgressbar() {
        _isLoading.value = true
    }

    companion object {
        private const val REQ_POST_DAY_LIFE = 744
    }
}