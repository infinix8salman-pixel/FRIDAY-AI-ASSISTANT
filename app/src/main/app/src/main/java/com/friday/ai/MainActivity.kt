
package com.friday.ai

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.graphics.Color
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

        val status = TextView(this)
        status.text = "YOUR PERSONAL AI ASSISTANT"
        status.textSize = 16f
        status.gravity = Gravity.CENTER
        status.setTextColor(Color.WHITE)

        screen.addView(title)
        screen.addView(status)
        setContentView(screen)

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                arrayOf(Manifest.permission.RECORD_AUDIO),
                100
            )
        } else {
            startFridayService()
        }
    }

    private fun startFridayService() {
        val intent = Intent(this, FridayVoiceService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
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
            startFridayService()
        }
    }
}
