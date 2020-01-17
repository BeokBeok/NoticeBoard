package com.beok.noticeboard.main

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beok.noticeboard.common.BaseViewModel
import com.beok.noticeboard.data.FirebaseRepository
import com.beok.noticeboard.model.DayLife
import com.beok.noticeboard.utils.ActivityCommand
import com.beok.noticeboard.utils.Event
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: FirebaseRepository) :
    BaseViewModel() {

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
                _dayLife.value = it
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
        repository.updateProfileImage(
            uri,
            onComplete = {
                hideProgressbar()
                showProfileImage(uri)
            }, onFailure = {
                hideProgressbar()
                _errMsg.value = it?.message ?: ""
            }
        )
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