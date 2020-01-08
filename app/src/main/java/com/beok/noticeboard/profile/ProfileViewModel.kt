package com.beok.noticeboard.profile

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.beok.noticeboard.data.FirebaseRepository
import com.beok.noticeboard.profile.model.DayLife
import com.beok.noticeboard.utils.ActivityCommand
import com.beok.noticeboard.utils.Event
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: FirebaseRepository
) : ViewModel() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser ?: error("User invalidate")
    private val storeRef = FirebaseStorage.getInstance().reference
    private val profileStoreRef = storeRef
        .child("profile")
        .child("${firebaseUser.email}.jpg")

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

    private val _errMsg = MutableLiveData<String>()
    val errMsg: LiveData<String> get() = _errMsg

    val imgUpload = fun(uri: Uri) {
        uploadProfileImage(uri)
    }

    val dayLifeActivityStart = fun(dayLifeIntent: Intent) {
        _startActivityForResultEvent.value =
            Event(ActivityCommand.StartActivityForResult(dayLifeIntent, REQ_POST_DAY_LIFE))
    }

    fun setupProfile() {
        showProgressbar()
        showProfileName(repository.getProfileName())
        repository.downloadProfileImage(
            onComplete = {
                hideProgressbar()
                showProfileImage(it)
            },
            onFailure = {
                hideProgressbar()
                _errMsg.value = it?.message ?: ""
            }
        )
    }

    fun refreshDayLife() {
        showProgressbar()
        repository.requestDayLife(
            onComplete = {
                hideProgressbar()
                _dayLife.value = it?.sortedByDescending { it.imageUrl.toString() }
            }, onFailure = {
                hideProgressbar()
                _errMsg.value = it?.message ?: ""
            }
        )
    }

    private fun showProfileName(profileName: String) {
        _profileName.value = profileName
    }

    private fun uploadProfileImage(uri: Uri) {
        showProgressbar()
        profileStoreRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    hideProgressbar()
                    error(task.exception ?: "")
                }
                profileStoreRef.downloadUrl
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