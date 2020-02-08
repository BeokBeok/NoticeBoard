package com.beok.noticeboard.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beok.noticeboard.MyApplication
import com.beok.noticeboard.R
import com.beok.noticeboard.base.BaseActivity
import com.beok.noticeboard.databinding.ActivityMainBinding
import com.beok.noticeboard.utils.ActivityCommand
import com.beok.noticeboard.wrapper.BeokGlide

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun setupInjection() =
        (application as MyApplication).appComponent.profileComponent()
            .create()
            .inject(this)

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
