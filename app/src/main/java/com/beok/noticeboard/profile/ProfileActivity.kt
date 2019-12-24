package com.beok.noticeboard.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beok.noticeboard.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    companion object {
        fun startActivity(context: Context?) {
            context?.startActivity(Intent(context, ProfileActivity::class.java))
        }
    }
}
