package com.beok.noticeboard.main

import android.content.Intent
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.beok.noticeboard.base.BaseViewModel
import com.beok.noticeboard.data.FirebaseRepository
import com.beok.noticeboard.model.DayLife
import com.beok.noticeboard.utils.ActivityCommand
import com.beok.noticeboard.utils.Event
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val repository: FirebaseRepository) :
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

    fun setupProfile() = viewModelScope.launch {
        showProgressbar()
        showProfileName(repository.getProfileName())
        repository.downloadProfileImage(
            onComplete = {
                hideProgressbar()
                showProfileImage(it)
            },
            onFailure = {
                hideProgressbar()
                setErrorMsg(it)
            }
        )
    }

    fun refreshDayLife() = viewModelScope.launch {
        showProgressbar()
        repository.requestDayLife(
            onComplete = {
                hideProgressbar()
                setDayLifeContents(it)
            }, onFailure = {
                hideProgressbar()
                setErrorMsg(it)
            }
        )
    }

    fun registerFCMToken() {
        repository.registerFCMToken()
    }

    private fun showProfileName(profileName: String) {
        _profileName.postValue(profileName)
    }

    private fun uploadProfileImage(uri: Uri) = viewModelScope.launch {
        showProgressbar()
        repository.updateProfileImage(
            uri,
            onComplete = {
                hideProgressbar()
                showProfileImage(uri)
            }, onFailure = {
                hideProgressbar()
                setErrorMsg(it)
            }
        )
    }

    private fun showProfileImage(uri: Uri?) {
        _imageUri.postValue(uri)
    }

    private fun hideProgressbar() {
        _isLoading.postValue(false)
    }

    private fun showProgressbar() {
        _isLoading.postValue(true)
    }

    private fun setErrorMsg(e: Exception?) {
        _errMsg.postValue(e?.message ?: "")
    }

    private fun setDayLifeContents(contents: List<DayLife>?) {
        _dayLife.postValue(contents)
    }

    companion object {
        private const val REQ_POST_DAY_LIFE = 744
    }
}