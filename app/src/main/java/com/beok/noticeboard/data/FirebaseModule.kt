package com.beok.noticeboard.data

import com.beok.noticeboard.data.service.FirebaseService
import com.beok.noticeboard.data.service.FirebaseServiceModule
import dagger.Module
import dagger.Provides

@Module(includes = [FirebaseServiceModule::class])
class FirebaseModule {

    @Provides
    fun provideFirebaseRepository(service: FirebaseService): FirebaseRepository =
        FirebaseRepositoryImpl(service)
}