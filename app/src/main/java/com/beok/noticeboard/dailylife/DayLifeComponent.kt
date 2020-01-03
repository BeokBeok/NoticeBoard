package com.beok.noticeboard.dailylife

import dagger.Subcomponent

@Subcomponent(modules = [DayLifeModule::class])
interface DayLifeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DayLifeComponent
    }

    fun inject(activity: DayLifeActivity)
}