package com.beok.noticeboard.dailylife

import android.net.Uri
import android.util.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject

class DayLifeViewModel @Inject constructor() : ViewModel() {

    private val firebaseUser = FirebaseAuth.getInstance().currentUser ?: error("User invalidate")
    private val spaceRef =
        FirebaseStorage.getInstance().reference
            .child("${firebaseUser.email}")
            .child("daylife")

    private val documentRef = FirebaseFirestore.getInstance()
        .collection("daylife")
        .document("${firebaseUser.email}")
        .collection(getCurrentDateForYYMMDD())

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> get() = _imageUri

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _onActivityResultConst = MutableLiveData<Int>()
    val onActivityResultConst: LiveData<Int> get() = _onActivityResultConst

    val imageUpload = fun(uri: Uri) {
        setImageUri(uri)
    }

    fun postDayLife(posts: String) {
        showProgressbar()
        uploadDayLifeImage(posts)
    }

    fun doCancel() {
        _onActivityResultConst.value = RES_RESULT_CANCELED
        hideProgressbar()
    }

    private fun doPost() {
        _onActivityResultConst.value = RES_RESULT_OK
        hideProgressbar()
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

    private fun uploadDayLifeImage(posts: String) {
        val uploadDate = System.currentTimeMillis().toString()
        val uploadSpaceRef = spaceRef.child("$uploadDate.jpg")
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
                    uploadPostingData(uploadDate, posts)
                }
        } ?: hideProgressbar()
    }

    private fun uploadPostingData(uploadDate: String, posts: String) {
        showProgressbar()
        val user = ArrayMap<String, String>()
        user["posts"] = posts

        documentRef.document(uploadDate)
            .set(user)
            .addOnSuccessListener { doPost() }
            .addOnCanceledListener { doCancel() }
    }

    private fun getCurrentDateForYYMMDD(): String {
        Calendar.getInstance().run {
            return "${get(Calendar.YEAR)}-${get(Calendar.MONTH) + 1}-${get(Calendar.DAY_OF_MONTH)}"
        }
    }

    companion object {
        private const val RES_RESULT_OK = -1
        private const val RES_RESULT_CANCELED = 0
    }
}