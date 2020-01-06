package com.beok.noticeboard.dailylife

import android.net.Uri
import android.util.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class DayLifeViewModel @Inject constructor() : ViewModel() {

    private val storageRef = FirebaseStorage.getInstance().reference
        .child("daylife")
    private val fireStoreRef = FirebaseFirestore.getInstance()
        .collection("daylife")

    private val _imageUriList = MutableLiveData<List<Uri>>()
    val imageUriList: LiveData<List<Uri>> get() = _imageUriList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _onActivityResultConst = MutableLiveData<Int>()
    val onActivityResultConst: LiveData<Int> get() = _onActivityResultConst

    val imageListUpload = fun(uriList: List<Uri>) {
        setImageUri(uriList)
    }

    fun postDayLife(posts: String) {
        val currentTime = System.currentTimeMillis().toString()
        _imageUriList.value?.let { uriList ->
            uriList.forEachIndexed { index, uri ->
                val fileName = "$index.jpg"
                val targetRef = storageRef.child(currentTime).child(fileName)
                targetRef.putFile(uri)
                    .continueWith { task ->
                        showProgressbar()
                        if (!task.isSuccessful) {
                            hideProgressbar()
                            error(task.exception ?: "")
                        }
                        targetRef.downloadUrl
                    }
                    .addOnCompleteListener { task ->
                        hideProgressbar()
                        if (!task.isSuccessful) return@addOnCompleteListener
                    }
            }
            updateDayLifeDatabase(posts)
        } ?: hideProgressbar()
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

    private fun setImageUri(uriList: List<Uri>) {
        _imageUriList.value = uriList
    }

    private fun updateDayLifeDatabase(posts: String) {
        showProgressbar()
        val user = ArrayMap<String, Any>()
        user["imgCnt"] = _imageUriList.value?.size ?: 0
        user["posts"] = posts

        fireStoreRef.document(System.currentTimeMillis().toString())
            .set(user)
            .addOnSuccessListener { doPost() }
            .addOnCanceledListener { doCancel() }
    }

    companion object {
        private const val RES_RESULT_OK = -1
        private const val RES_RESULT_CANCELED = 0
    }
}