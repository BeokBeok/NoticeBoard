package com.beok.noticeboard.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : ViewModel() {

    private val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""
    private val spaceRef =
        FirebaseStorage.getInstance().reference.child("images/profile/${userEmail}.jpg")

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val showPicker = fun(uri: Uri) { uploadProfileImage(uri) }

    private fun uploadProfileImage(uri: Uri) {
        showProgressbar()
        spaceRef.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) error(task.exception ?: "")
                spaceRef.downloadUrl
            }
            .addOnCompleteListener { task ->
                hideProgressbar()
                if (!task.isSuccessful) return@addOnCompleteListener
            }
    }

    private fun hideProgressbar() {
        _isLoading.value = false
    }

    private fun showProgressbar() {
        _isLoading.value = true
    }
}