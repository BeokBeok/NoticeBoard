package com.beok.noticeboard.login

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [LoginModule::class])
interface LoginComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance
            @Named("loginContext")
            context: Context
        ): LoginComponent
    }

    fun inject(activity: LoginActivity)
}