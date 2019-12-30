package com.beok.noticeboard.dailylife

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.beok.noticeboard.R
import com.beok.noticeboard.databinding.ActivityDayLifeBinding

class DayLifeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDayLifeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_day_life)
    }
}
