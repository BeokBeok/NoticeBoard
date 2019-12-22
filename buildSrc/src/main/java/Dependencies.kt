object Version {
    const val KOTLIN = "1.3.61"
    const val MATERIAL = "1.0.0"
    const val CORE_KTX = "1.1.0"
    const val CONSTRAINT_LAYOUT = "1.1.3"

    const val JUNIT = "1.1.1"
    const val ESPRESSO_CORE = "3.2.0"

    const val FIREBASE_ANALYTICS = "17.2.1"
    const val FIREBASE_AUTH = "19.2.0"
    const val PLAY_SERVICES_AUTH = "17.0.0"

    const val LIFECYCLE = "2.1.0"

    const val DAGGER = "2.25.2"
}

object Libraries {
    const val KOTLIN_STDLIB_JDK7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.KOTLIN}"
    const val MATERIAL = "com.google.android.material:material:${Version.MATERIAL}"
    const val CORE_KTX = "androidx.core:core-ktx:${Version.CORE_KTX}"
    const val CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINT_LAYOUT}"

    const val JUNIT = "androidx.test.ext:junit:${Version.JUNIT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Version.ESPRESSO_CORE}"

    const val FIREBASE_ANALYTICS =
        "com.google.firebase:firebase-analytics:${Version.FIREBASE_ANALYTICS}"
    const val FIREBASE_AUTH = "com.google.firebase:firebase-auth:${Version.FIREBASE_AUTH}"
    const val PLAY_SERVICES_AUTH =
        "com.google.android.gms:play-services-auth:${Version.PLAY_SERVICES_AUTH}"

    const val LIFECYCLE_VM = "androidx.lifecycle:lifecycle-viewmodel:${Version.LIFECYCLE}"
    const val LIFECYCLE_EXT = "androidx.lifecycle:lifecycle-extensions:${Version.LIFECYCLE}"

    const val DAGGER = "com.google.dagger:dagger:${Version.DAGGER}"
    const val DAGGER_COMPILER = "com.google.dagger:dagger-compiler:${Version.DAGGER}"
}