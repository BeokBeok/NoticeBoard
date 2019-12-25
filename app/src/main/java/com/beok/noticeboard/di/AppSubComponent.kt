package com.beok.noticeboard.di

import androidx.lifecycle.ViewModelProvider
import com.beok.noticeboard.login.LoginComponent
import com.beok.noticeboard.profile.ProfileComponent
import com.beok.noticeboard.utils.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module(subcomponents = [LoginComponent::class, ProfileComponent::class])
abstract class AppSubComponent {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}