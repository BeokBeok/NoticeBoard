package com.beok.noticeboard.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : ViewModel() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser ?: error("User invalidate")
    private val spaceRef =
        FirebaseStorage.getInstance().reference.child("images/profile/${firebaseUser.email}.jpg")

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> get() = _imageUri

    private val _profileName = MutableLiveData<String>()
    val profileName: LiveData<String> get() = _profileName

    val showPicker = fun(uri: Uri) {
        uploadProfileImage(uri)
    }

    fun setupProfile() {
        showProgressbar()
        spaceRef.downloadUrl
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

    private fun showProfileName() {
        _profileName.value = firebaseUser.displayName
    }

    private fun uploadProfileImage(uri: Uri) {
        showProgressbar()
        spaceRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    hideProgressbar()
                    error(task.exception ?: "")
                }
                spaceRef.downloadUrl
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
}