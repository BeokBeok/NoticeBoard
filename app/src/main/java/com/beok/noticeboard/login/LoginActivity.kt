package com.beok.noticeboard.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.beok.noticeboard.R
import com.beok.noticeboard.databinding.ActivityLoginBinding
import com.beok.noticeboard.profile.MainActivity
import com.beok.noticeboard.utils.ActivityCommand
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by lazy {
        LoginViewModel(
            googleSignInClient = GoogleSignIn.getClient(
                this,
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            )
        )
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        goActivityIfLoggedIn()
        setupObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            viewModel.onResultFromActivity(requestCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setupDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
    }

    private fun setupObserver() {
        viewModel.startActivityForResultEvent.observe(
            this,
            Observer { event ->
                event.getContentIfNotHandled()?.let { cmd ->
                    if (cmd is ActivityCommand.StartActivityForResult) {
                        startActivityForResult(cmd.intent, cmd.requestCode)
                    }
                }
            }
        )
        viewModel.isSuccessLogin.observe(
            this,
            Observer { isSuccessLogin ->
                if (isSuccessLogin) {
                    MainActivity.startActivity(
                        this
                    )
                } else {
                    showToast(getString(R.string.msg_login_fail))
                }
            }
        )
        viewModel.errMsg.observe(
            this,
            Observer {
                showToast(it)
            }
        )
    }

    private fun goActivityIfLoggedIn() {
        if (viewModel.existCurrentUser()) {
            MainActivity.startActivity(this)
        }
    }

    private fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
