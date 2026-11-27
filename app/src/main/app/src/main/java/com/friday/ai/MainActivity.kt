package com.friday.ai

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setBackgroundColor(Color.rgb(2, 8, 20))

        val title = TextView(this)
        title.text = "FRIDAY AI"
        title.textSize = 32f
        title.setTextColor(Color.CYAN)
        title.gravity = Gravity.CENTER

        val logo = TextView(this)
        logo.text = "◉\nF"
        logo.textSize = 65f
        logo.setTextColor(Color.CYAN)
        logo.gravity = Gravity.CENTER

        val status = TextView(this)
        status.text = "FRIDAY SYSTEM ONLINE"
        status.textSize = 16f
        status.setTextColor(Color.WHITE)
        status.gravity = Gravity.CENTER

        layout.addView(title)
        layout.addView(logo)
        layout.addView(status)

        setContentView(layout)

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO), 100
            )
        }
    }
}
