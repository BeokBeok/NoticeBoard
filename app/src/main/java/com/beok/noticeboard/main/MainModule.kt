package com.beok.noticeboard.main

import androidx.lifecycle.ViewModel
import com.beok.noticeboard.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap

@InstallIn(ActivityComponent::class)
@Module
abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindProfileViewModel(viewModel: MainViewModel): ViewModel
}