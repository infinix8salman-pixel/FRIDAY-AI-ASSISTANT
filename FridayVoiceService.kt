package com.friday.ai

import android.app.*
import android.content.*
import android.os.*
import android.speech.*
import android.speech.tts.TextToSpeech
import java.util.Locale

class FridayVoiceService : Service() {

    private var recognizer: SpeechRecognizer? = null
    private var tts: TextToSpeech? = null
    private val handler = Handler(Looper.getMainLooper())
    private var sleeping = false

    override fun onCreate() {
        super.onCreate()

        tts = TextToSpeech(this) {
            tts?.language = Locale.US
        }

        val channel = NotificationChannel(
            "friday_voice",
            "FRIDAY Voice Assistant",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)

        val notification = Notification.Builder(this, "friday_voice")
            .setContentTitle("FRIDAY AI is listening")
            .setContentText("Voice assistant is active")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build()

        startForeground(1, notification)

        startListening()
    }

    private fun speak(message: String) {
        tts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "friday")
    }

    private fun startListening() {
        if (sleeping) return

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            speak("Voice recognition is unavailable")
            return
        }

        recognizer?.destroy()
        recognizer = SpeechRecognizer.createSpeechRecognizer(this)

        recognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                handler.postDelayed({ startListening() }, 1200)
            }

            override fun onResults(results: Bundle?) {
                val words = results?.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )?.firstOrNull()?.lowercase(Locale.US).orEmpty()

                handleCommand(words)
                handler.postDelayed({ startListening() }, 1000)
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")

        recognizer?.startListening(intent)
    }

    private fun handleCommand(command: String) {

        if (command.contains("hey friday")) {
            speak("Yes, I am online")
            return
        }

        if (command.contains("friday sleep")) {
            sleeping = true
            speak("Going to sleep")
            recognizer?.cancel()
            stopSelf()
            return
        }

        val features = listOf(
            "AI Voice Assistant",
            "Phone Control",
            "Global 3D Earth Maps",
            "App Scanner and Builder",
            "Photo and Video Studio",
            "Coding and Development",
            "Ethical Cybersecurity",
            "Smart Tools",
            "Futuristic Home Screen",
            "Communication and Social",
            "Internet and Research",
            "Device and Computer Control",
            "Advanced AI Agents",
            "Security and Privacy",
            "Entertainment and Gaming",
            "Smart World and Live Tracking"
        )

        val found = features.firstOrNull {
            command.contains(it.lowercase(Locale.US))
        }

        if (command.contains("show features")) {
            speak("Your features are available in FRIDAY")
        } else if (found != null) {
            speak("Opening $found")
        }
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int = START_STICKY

    override fun onDestroy() {
        recognizer?.destroy()
        tts?.shutdown()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
