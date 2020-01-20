package com.beok.noticeboard.di

import android.content.Context
import com.beok.noticeboard.dailylife.DayLifeComponent
import com.beok.noticeboard.data.FirebaseModule
import com.beok.noticeboard.login.LoginComponent
import com.beok.noticeboard.main.MainComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubComponent::class, FirebaseModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @Named("applicationContext")
            context: Context
        ): AppComponent
    }

    fun loginComponent(): LoginComponent.Factory
    fun profileComponent(): MainComponent.Factory
    fun dayLifeComponent(): DayLifeComponent.Factory
}