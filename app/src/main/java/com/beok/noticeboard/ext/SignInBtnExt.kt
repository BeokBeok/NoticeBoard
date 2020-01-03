package com.beok.noticeboard.ext

import androidx.databinding.BindingAdapter
import com.google.android.gms.common.SignInButton

@BindingAdapter("onClickSignIn")
fun SignInButton.onClickSignIn(signIn: () -> Unit) {
    setOnClickListener { signIn.invoke() }
}