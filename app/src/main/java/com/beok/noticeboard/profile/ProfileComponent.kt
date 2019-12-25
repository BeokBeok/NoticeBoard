package com.beok.noticeboard.profile

import dagger.Subcomponent

@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ProfileComponent
    }

    fun inject(activity: ProfileActivity)
}