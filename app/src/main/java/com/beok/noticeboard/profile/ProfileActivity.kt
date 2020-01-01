package com.beok.noticeboard.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beok.noticeboard.MyApplication
import com.beok.noticeboard.R
import com.beok.noticeboard.databinding.ActivityProfileBinding
import com.beok.noticeboard.utils.ActivityCommand
import com.beok.noticeboard.wrapper.Glide
import javax.inject.Inject

class ProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.profileComponent()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
        setupBinding()
        viewModel.setupProfile()
        setupObserver()
    }

    private fun setupObserver() {
        val owner = this@ProfileActivity
        viewModel.run {
            imageUri.observe(
                owner,
                Observer { imageUri ->
                    Glide.showImageForCenterCrop(binding.ivProfile, imageUri)
                }
            )
            isLoading.observe(
                owner,
                Observer {
                    binding.pbLoading.isVisible = it
                }
            )
            startActivityForResultEvent.observe(
                owner,
                Observer {
                    it.getContentIfNotHandled()?.let { cmd ->
                        if (cmd is ActivityCommand.StartActivityForResult) {
                            startActivityForResult(cmd.intent, cmd.requestCode)
                        }
                    }
                }
            )
        }

    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.vm = viewModel
    }

    companion object {
        fun startActivity(context: Context?) {
            context?.startActivity(Intent(context, ProfileActivity::class.java))
        }
    }
}
