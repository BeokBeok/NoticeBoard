package com.beok.noticeboard.main

import androidx.lifecycle.ViewModel
import com.beok.noticeboard.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindProfileViewModel(viewModel: MainViewModel): ViewModel
}