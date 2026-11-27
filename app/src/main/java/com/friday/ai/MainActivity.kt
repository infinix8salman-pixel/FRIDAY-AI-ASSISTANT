
package com.friday.ai

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screen = LinearLayout(this)
        screen.orientation = LinearLayout.VERTICAL
        screen.gravity = Gravity.CENTER
        screen.setPadding(24, 24, 24, 24)
        screen.setBackgroundColor(Color.rgb(2, 8, 20))

        val title = TextView(this)
        title.text = "FRIDAY AI"
        title.textSize = 32f
        title.gravity = Gravity.CENTER
        title.setTextColor(Color.CYAN)

        val logo = TextView(this)
        logo.text = "◉\nF"
        logo.textSize = 65f
        logo.gravity = Gravity.CENTER
        logo.setTextColor(Color.rgb(100, 220, 255))

        val status = TextView(this)
        status.text = "YOUR PERSONAL AI ASSISTANT"
        status.textSize = 14f
        status.gravity = Gravity.CENTER
        status.setTextColor(Color.WHITE)

        screen.addView(title)
        screen.addView(logo)
        screen.addView(status)

        setContentView(screen)

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO), 100
            )
        }
    }
}
