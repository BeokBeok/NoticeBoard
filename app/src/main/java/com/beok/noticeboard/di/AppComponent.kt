package com.beok.noticeboard.di

import com.beok.noticeboard.dailylife.DayLifeComponent
import com.beok.noticeboard.login.LoginComponent
import com.beok.noticeboard.profile.ProfileComponent
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
    fun profileComponent(): ProfileComponent.Factory
    fun dayLifeComponent(): DayLifeComponent.Factory
}