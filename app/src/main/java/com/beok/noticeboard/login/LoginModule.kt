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
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap
import javax.inject.Named

@InstallIn(ActivityComponent::class)
@Module(includes = [LoginModule.BindModules::class])
class LoginModule {

    @Provides
    fun provideGoogleSignInClient(@Named("loginContext") context: Context): GoogleSignInClient =
        GoogleSignIn.getClient(
            context,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

    @InstallIn(ActivityComponent::class)
    @Module
    interface BindModules {
        @Binds
        @IntoMap
        @ViewModelKey(LoginViewModel::class)
        fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
    }
}