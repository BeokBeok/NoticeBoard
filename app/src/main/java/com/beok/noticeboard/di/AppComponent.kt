package com.beok.noticeboard.di

import com.beok.noticeboard.login.LoginComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubComponent::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(): AppComponent
    }

    fun loginComponent(): LoginComponent.Factory
}