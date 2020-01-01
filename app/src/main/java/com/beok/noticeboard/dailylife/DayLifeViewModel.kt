package com.beok.noticeboard.dailylife

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class DayLifeViewModel @Inject constructor() : ViewModel() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser ?: error("User invalidate")
    private val spaceRef =
        FirebaseStorage.getInstance().reference
            .child("${firebaseUser.email}")
            .child("daylife")

    private val _imageUri = MutableLiveData<Uri>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val imageUpload = fun(uri: Uri) {
        setImageUri(uri)
    }

    fun postDayLife(post: String) {
        showProgressbar()
        uploadDayLifeImage()
    }

    fun doCancel() {

    }

    private fun showProgressbar() {
        _isLoading.value = true
    }

    private fun hideProgressbar() {
        _isLoading.value = false
    }

    private fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    private fun uploadDayLifeImage() {
        val uploadTime = System.currentTimeMillis().toString()
        val uploadSpaceRef = spaceRef.child("$uploadTime.jpg")
        _imageUri.value?.let { uri ->
            uploadSpaceRef.putFile(uri)
                .continueWith { task ->
                    if (!task.isSuccessful) {
                        hideProgressbar()
                        error(task.exception ?: "")
                    }
                    uploadSpaceRef.downloadUrl
                }
                .addOnCompleteListener { task ->
                    hideProgressbar()
                    if (!task.isSuccessful) return@addOnCompleteListener
                }
        }
    }
}