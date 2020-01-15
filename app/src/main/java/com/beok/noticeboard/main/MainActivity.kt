package com.beok.noticeboard.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beok.noticeboard.MyApplication
import com.beok.noticeboard.R
import com.beok.noticeboard.databinding.ActivityMainBinding
import com.beok.noticeboard.utils.ActivityCommand
import com.beok.noticeboard.wrapper.BeokGlide
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.profileComponent()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
        setupBinding()
        setupUi()
        setupObserver()
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
            setupProfile()
            refreshDayLife()
        }
    }

    private fun setupObserver() {
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

    private fun showToast(msg: String?) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }

    companion object {
        fun startActivity(context: Context?) {
            context?.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}