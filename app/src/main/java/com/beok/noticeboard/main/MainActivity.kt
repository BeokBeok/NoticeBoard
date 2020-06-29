package com.beok.noticeboard.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.beok.noticeboard.R
import com.beok.noticeboard.base.BaseActivity
import com.beok.noticeboard.databinding.ActivityMainBinding
import com.beok.noticeboard.utils.ActivityCommand
import com.beok.noticeboard.wrapper.BeokGlide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel by viewModels<MainViewModel>()

    override fun setupViewModel() {
        binding.vm = viewModel
    }

    override fun setupObserver() {
        val owner = this@MainActivity
        viewModel.run {
            imageUri.observe(owner, Observer { imageUri ->
                BeokGlide.showImageForCenterCrop(binding.ivProfile, imageUri)
            })
            startActivityForResultEvent.observe(owner, Observer {
                it.getContentIfNotHandled()?.let { cmd ->
                    if (cmd is ActivityCommand.StartActivityForResult) {
                        startActivityForResult(cmd.intent, cmd.requestCode)
                    }
                }
            })
            errMsg.observe(owner, Observer { showToast(it) })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        viewModel.refreshDayLife()
    }

    private fun setupUi() {
        binding.rvContent.run {
            setHasFixedSize(true)
            adapter = MainAdapter()
        }
        viewModel.run {
            registerFCMToken()
            setupProfile()
            refreshDayLife()
        }
    }

    companion object {
        fun startActivity(context: Context?) {
            context?.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
