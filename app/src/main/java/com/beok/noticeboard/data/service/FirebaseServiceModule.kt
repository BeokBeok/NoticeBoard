package com.beok.noticeboard.data.service

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ApplicationComponent::class)
@Module
class FirebaseServiceModule {

    @Provides
    fun provideFirebaseService(
        firebaseAuth: FirebaseAuth,
        firebaseStorage: FirebaseStorage,
        firebaseFirestore: FirebaseFirestore,
        firebaseAnalytics: FirebaseAnalytics
    ): FirebaseService =
        FirebaseService(firebaseAuth, firebaseStorage, firebaseFirestore, firebaseAnalytics)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideFirebaseFirestorage(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context)
}