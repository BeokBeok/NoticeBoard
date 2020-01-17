package com.beok.noticeboard.common

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

abstract class BaseActivity<VDB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes
    private val layoutRes: Int
) : AppCompatActivity() {

    protected lateinit var binding: VDB

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        setupInjection()
        super.onCreate(savedInstanceState)
        setupBinding()
        setupViewModel()
        setupObserver()
    }

    protected abstract fun setupInjection()

    protected abstract fun setupViewModel()

    protected abstract fun setupObserver()

    protected fun showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this
    }
}