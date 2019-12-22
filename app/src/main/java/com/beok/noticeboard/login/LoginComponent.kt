package com.beok.noticeboard.login

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [LoginModule::class])
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): LoginComponent
    }

    fun inject(activity: LoginActivity)
}