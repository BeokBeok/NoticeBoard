package com.beok.noticeboard.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.beok.noticeboard.R
import com.beok.noticeboard.base.BaseActivity
import com.beok.noticeboard.databinding.ActivityLoginBinding
import com.beok.noticeboard.main.MainActivity
import com.beok.noticeboard.utils.ActivityCommand
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    override val viewModel by viewModels<LoginViewModel>()

    override fun setupViewModel() {
        binding.viewModel = viewModel
    }

    override fun setupObserver() {
        val owner = this@LoginActivity
        viewModel.run {
            startActivityForResultEvent.observe(
                owner,
                Observer { event ->
                    event.getContentIfNotHandled()?.let { cmd ->
                        if (cmd is ActivityCommand.StartActivityForResult) {
                            startActivityForResult(cmd.intent, cmd.requestCode)
                        }
                    }
                }
            )
            isSuccessLogin.observe(
                owner,
                Observer { isSuccessLogin ->
                    if (isSuccessLogin) {
                        MainActivity.startActivity(owner)
                    } else {
                        showToast(getString(R.string.msg_login_fail))
                    }
                }
            )
            errMsg.observe(
                owner,
                Observer {
                    showToast(it)
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goActivityIfLoggedIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            viewModel.onResultFromActivity(requestCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun goActivityIfLoggedIn() {
        if (viewModel.existCurrentUser()) {
            MainActivity.startActivity(this)
        }
    }
}
