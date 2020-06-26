package com.beok.noticeboard.di

import androidx.lifecycle.ViewModelProvider
import com.beok.noticeboard.dailylife.DayLifeComponent
import com.beok.noticeboard.login.LoginComponent
import com.beok.noticeboard.main.MainComponent
import com.beok.noticeboard.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module(
    subcomponents = [
        LoginComponent::class,
        MainComponent::class,
        DayLifeComponent::class
    ]
)
abstract class AppSubComponent {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}