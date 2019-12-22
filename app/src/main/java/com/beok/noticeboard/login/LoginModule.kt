package com.beok.noticeboard.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.beok.noticeboard.R
import com.beok.noticeboard.di.ViewModelKey
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module(includes = [LoginModule.BindModules::class])
class LoginModule {

    @Provides
    fun provideGoogleSignInClient(context: Context): GoogleSignInClient =
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

    @Module
    interface BindModules {
        @Binds
        @IntoMap
        @ViewModelKey(LoginViewModel::class)
        fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
    }
}