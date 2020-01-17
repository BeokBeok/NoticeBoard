package com.beok.noticeboard.dailylife

import android.os.Bundle
import android.text.InputType
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beok.noticeboard.MyApplication
import com.beok.noticeboard.R
import com.beok.noticeboard.common.BaseActivity
import com.beok.noticeboard.databinding.ActivityDayLifeBinding
import com.beok.noticeboard.wrapper.BeokGlide

class DayLifeActivity :
    BaseActivity<ActivityDayLifeBinding, DayLifeViewModel>(R.layout.activity_day_life) {

    override val viewModel: DayLifeViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DayLifeViewModel::class.java]
    }

    override fun setupInjection() =
        (application as MyApplication).appComponent.dayLifeComponent()
            .create()
            .inject(this)

    override fun setupViewModel() {
        binding.vm = viewModel
    }

    override fun setupObserver() {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        binding.etDaylife.setRawInputType(InputType.TYPE_CLASS_TEXT)
    }
}
