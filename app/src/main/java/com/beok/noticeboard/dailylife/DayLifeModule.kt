package com.beok.noticeboard.dailylife

import androidx.lifecycle.ViewModel
import com.beok.noticeboard.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap

@InstallIn(ActivityComponent::class)
@Module
abstract class DayLifeModule {

    @Binds
    @IntoMap
    @ViewModelKey(DayLifeViewModel::class)
    abstract fun bindDayLifeViewModel(viewModel: DayLifeViewModel): ViewModel
}