package com.beok.noticeboard.data.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class FirebaseServiceModule {

    @Provides
    fun provideFirebaseService(
        firebaseAuth: FirebaseAuth,
        firebaseStorage: FirebaseStorage,
        firebaseFirestore: FirebaseFirestore
    ): FirebaseService = FirebaseService(firebaseAuth, firebaseStorage, firebaseFirestore)

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun provideFirebaseFirestorage(): FirebaseFirestore = FirebaseFirestore.getInstance()
}