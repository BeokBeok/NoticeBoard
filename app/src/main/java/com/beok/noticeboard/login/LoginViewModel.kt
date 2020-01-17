package com.beok.noticeboard.login

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beok.noticeboard.common.BaseViewModel
import com.beok.noticeboard.utils.ActivityCommand
import com.beok.noticeboard.utils.Event
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val googleSignInClient: GoogleSignInClient) :
    BaseViewModel() {

    private val _startActivityForResultEvent = MutableLiveData<Event<ActivityCommand>>()
    val startActivityForResultEvent: LiveData<Event<ActivityCommand>>
        get() = _startActivityForResultEvent

    private val _isSuccessLogin = MutableLiveData<Boolean>()
    val isSuccessLogin: LiveData<Boolean> get() = _isSuccessLogin

    private val _errMsg = MutableLiveData<String>()
    val errMsg: LiveData<String> get() = _errMsg

    private val auth = FirebaseAuth.getInstance()

    fun existCurrentUser(): Boolean = auth.currentUser != null

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        _startActivityForResultEvent.value =
            Event(ActivityCommand.StartActivityForResult(signInIntent, REQ_RC_SIGN_IN))
    }

    fun onResultFromActivity(requestCode: Int, data: Intent?) {
        if (requestCode == REQ_RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                _errMsg.value = e.message
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _isSuccessLogin.value = task.isSuccessful
                if (!task.isSuccessful) _errMsg.value = task.exception?.message
            }
    }

    companion object {
        private const val REQ_RC_SIGN_IN = 676
    }
}