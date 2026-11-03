package com.sathiai.assistant.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class TtsOutput(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var onDoneCallback: (() -> Unit)? = null

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            // ডিফল্টভাবে বাংলা সেট করা হলো, তবে LanguageDetector পরে এটি চেঞ্জ করতে পারবে
            tts?.language = Locale("bn", "BD") 
            
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {}
                override fun onDone(utteranceId: String?) {
                    onDoneCallback?.invoke()
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {}
            })
        }
    }

    /**
     * অ্যাপকে কথা বলানোর ফাংশন
     */
    fun speak(text: String, languageTag: String = "bn-BD", onDone: (() -> Unit)? = null) {
        if (!isInitialized) return
        
        this.onDoneCallback = onDone
        
        val locale = when (languageTag) {
            "bn-BD" -> Locale("bn", "BD")
            "hi-IN" -> Locale("hi", "IN")
            else -> Locale.US
        }
        
        tts?.language = locale
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "SathiAI_Utterance")
    }

    /**
     * কথা বলা বন্ধ করা (STOP কমান্ডের জন্য)
     */
    fun stop() {
        tts?.stop()
    }

    /**
     * মেমোরি ক্লিয়ার করা
     */
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
