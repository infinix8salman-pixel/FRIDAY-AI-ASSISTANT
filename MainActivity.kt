package com.friday.ai

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.graphics.Color
import android.view.Gravity

class MainActivity : Activity() {

    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screen = android.widget.LinearLayout(this)
        screen.orientation = 1
        screen.gravity = Gravity.CENTER
        screen.setBackgroundColor(Color.rgb(2, 8, 20))

        val title = TextView(this)
        title.text = "FRIDAY AI"
        title.textSize = 30f
        title.setTextColor(Color.CYAN)
        title.gravity = Gravity.CENTER

        val logo = TextView(this)
        logo.text = "◉\nF"
        logo.textSize = 65f
        logo.setTextColor(Color.rgb(90, 210, 255))
        logo.gravity = Gravity.CENTER

        status = TextView(this)
        status.text = "FRIDAY SYSTEM ONLINE"
        status.textSize = 15f
        status.setTextColor(Color.CYAN)
        status.gravity = Gravity.CENTER

        screen.addView(title)
        screen.addView(logo)
        screen.addView(status)
        setContentView(screen)

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED) {
            startFriday()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO), 100
            )
        }
    }

    private fun startFriday() {
        val intent = Intent(this, FridayVoiceService::class.java)
        startForegroundService(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode, permissions, grantResults
        )
        if (requestCode == 100 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startFriday()
        } else {
            status.text = "Microphone permission required"
        }
    }
}
