
package com.friday.ai

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import java.util.Locale

class FridayVoiceService : Service(), RecognitionListener,
    TextToSpeech.OnInitListener {

    private var recognizer: SpeechRecognizer? = null
    private var tts: TextToSpeech? = null
    private val handler = Handler(Looper.getMainLooper())
    private var active = false
    private var ready = false

    override fun onCreate() {
        super.onCreate()

        tts = TextToSpeech(this, this)

        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            recognizer = SpeechRecognizer.createSpeechRecognizer(this)
            recognizer?.setRecognitionListener(this)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            ready = true
        }
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        if (intent?.action == "STOP") {
            sleep()
        } else {
            active = true
            speak("Yes, I am online")
            handler.postDelayed({ listen() }, 1500)
        }

        return START_NOT_STICKY
    }

    private fun listen() {
        if (!active || !ready) return

        val speechIntent = Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        ).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "en-PK"
            )
        }

        try {
            recognizer?.startListening(speechIntent)
        } catch (e: Exception) {
            handler.postDelayed({ listen() }, 1500)
        }
    }

    override fun onResults(results: Bundle?) {
        val words = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.firstOrNull()
            ?.lowercase(Locale.ROOT)
            ?: ""

        when {
            words.contains("friday sleep") -> sleep()

            words.contains("hello") ||
            words.contains("hi friday") ->
                speak("Hello, how can I help you?")

            else ->
                speak("I heard you. Please tell me what to do.")
        }

        if (active) {
            handler.postDelayed({ listen() }, 1800)
        }
    }

    private fun speak(message: String) {
        if (ready) {
            tts?.speak(
                message,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "FRIDAY_RESPONSE"
            )
        }
    }

    private fun sleep() {
        active = false
        recognizer?.stopListening()
        speak("Going to sleep")
        handler.postDelayed({ stopSelf() }, 1200)
    }

    override fun onError(error: Int) {
        if (active) {
            handler.postDelayed({ listen() }, 1500)
        }
    }

    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}
    override fun onPartialResults(partialResults: Bundle?) {}
    override fun onEvent(eventType: Int, params: Bundle?) {}

    override fun onDestroy() {
        active = false
        handler.removeCallbacksAndMessages(null)
        recognizer?.destroy()
        tts?.shutdown()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
