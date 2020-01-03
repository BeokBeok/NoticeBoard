package com.beok.noticeboard.ext

import android.content.Intent
import androidx.databinding.BindingAdapter
import com.beok.noticeboard.dailylife.DayLifeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("onStartDayLifeActivity")
fun FloatingActionButton.onStartDayLifeActivity(dayLifeActivityStart: (Intent) -> Unit) {
    setOnClickListener {
        val intent = Intent(it.context, DayLifeActivity::class.java)
        dayLifeActivityStart.invoke(intent)
    }
}