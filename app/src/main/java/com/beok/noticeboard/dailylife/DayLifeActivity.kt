package com.beok.noticeboard.dailylife

import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beok.noticeboard.MyApplication
import com.beok.noticeboard.R
import com.beok.noticeboard.databinding.ActivityDayLifeBinding
import com.beok.noticeboard.wrapper.BeokGlide
import javax.inject.Inject

class DayLifeActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DayLifeViewModel::class.java]
    }

    private lateinit var binding: ActivityDayLifeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.dayLifeComponent()
            .create()
            .inject(this)
        super.onCreate(savedInstanceState)
        setupBinding()
        setupUi()
        setupObserver()
    }

    private fun setupObserver() {
        val owner = this@DayLifeActivity
        viewModel.run {
            onActivityResultConst.observe(owner, Observer {
                setResult(it)
                finish()
            })
            imageUriList.observe(owner, Observer {
                BeokGlide.showImageForCenterCrop(binding.ivDaylife, it[0])
            })
            isLoading.observe(owner, Observer {
                binding.pbLoading.isVisible = it
            })
        }
    }

    private fun setupUi() {
        binding.etDaylife.setRawInputType(InputType.TYPE_CLASS_TEXT)
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_day_life)
        binding.lifecycleOwner = this
        binding.vm = viewModel
    }
}
