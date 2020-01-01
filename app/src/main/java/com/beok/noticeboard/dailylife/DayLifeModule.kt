package com.beok.noticeboard.dailylife

import androidx.lifecycle.ViewModel
import com.beok.noticeboard.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DayLifeModule {

    @Binds
    @IntoMap
    @ViewModelKey(DayLifeViewModel::class)
    abstract fun bindDayLifeViewModel(viewModel: DayLifeViewModel): ViewModel
}