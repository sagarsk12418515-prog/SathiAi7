package com.sathiai.assistant.speech

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log

class StopWordListener(
    private val context: Context,
    private val ttsOutput: TtsOutput,
    private val onStopTriggered: () -> Unit
) {
    private val languageDetector = LanguageDetector()
    private var isListening = false

    /**
     * গ্লোবাল STOP কমান্ড লিসেনার চালু করা
     */
    fun startListening() {
        isListening = true
        Log.d("StopWordListener", "Stop word listener started.")
        // এখানে ভবিষ্যতে কন্টিনিউয়াস অডিও স্ট্রিম লিসেনিং যুক্ত করা যাবে
    }

    /**
     * গ্লোবাল STOP কমান্ড লিসেনার বন্ধ করা
     */
    fun stopListening() {
        isListening = false
        Log.d("StopWordListener", "Stop word listener stopped.")
    }

    /**
     * ইউজার যখন কিছু বলে, তখন এই ফাংশনটি কল করতে হবে।
     * এটি চেক করবে ইউজার "থামো" বা "STOP" বলেছে কিনা।
     */
    fun processText(text: String) {
        if (isListening && languageDetector.isStopCommand(text)) {
            Log.d("StopWordListener", "STOP command detected: $text")
            triggerStop()
        }
    }

    private fun triggerStop() {
        // ১. সাথে সাথে TTS (কথা বলা) বন্ধ করে দেবে
        ttsOutput.stop()

        // ২. মেইন থ্রেডে কলব্যাক পাঠাবে যাতে অন্য সব কাজ বন্ধ হয়ে যায়
        Handler(Looper.getMainLooper()).post {
            onStopTriggered()
        }
    }
}
