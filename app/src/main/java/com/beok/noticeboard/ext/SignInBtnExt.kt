package com.beok.noticeboard.ext

import androidx.databinding.BindingAdapter
import com.google.android.gms.common.SignInButton

@BindingAdapter("onClick")
fun SignInButton.onClick(signIn: () -> Unit) {
    setOnClickListener { signIn.invoke() }
}