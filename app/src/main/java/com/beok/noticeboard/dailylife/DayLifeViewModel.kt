package com.beok.noticeboard.dailylife

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beok.noticeboard.base.BaseViewModel
import com.beok.noticeboard.data.FirebaseRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DayLifeViewModel @Inject constructor(
    private val repository: FirebaseRepository
) : BaseViewModel() {

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
        showProgressbar()
        val startPostingSec = System.currentTimeMillis()
        _imageUriList.value?.let { uriList ->
            repository.postDayLife(
                uriList, posts,
                onComplete = { isSuccess ->
                    if (isSuccess) {
                        startPostingTimeEventLog(System.currentTimeMillis() - startPostingSec)
                        doPost()
                    } else doCancel()
                }, onFailure = {
                    doCancel()
                }
            )
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

    private fun startPostingTimeEventLog(sec: Long) {
        repository.startEventLog(
            "uploadTime",
            "${TimeUnit.MILLISECONDS.toSeconds(sec)}",
            "posting"
        )
    }

    companion object {
        private const val RES_RESULT_OK = -1
        private const val RES_RESULT_CANCELED = 0
    }
}