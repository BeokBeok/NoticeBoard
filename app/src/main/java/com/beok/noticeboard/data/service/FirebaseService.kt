package com.beok.noticeboard.data.service

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

data class FirebaseService(
    val firebaseAuth: FirebaseAuth,
    val firebaseStorage: FirebaseStorage,
    val firebaseFirestore: FirebaseFirestore,
    val firebaseAnalytics: FirebaseAnalytics
)