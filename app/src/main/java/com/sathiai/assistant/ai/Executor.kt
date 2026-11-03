package com.sathiai.assistant.ai

import android.content.Context
import android.content.Intent
import com.sathiai.assistant.security.AppPolicy
import com.sathiai.assistant.speech.TtsOutput

class Executor(
    private val context: Context,
    private val ttsOutput: TtsOutput
) {
    fun execute(action: Action) {
        when (action) {
            is Action.Speak -> {
                // অ্যাপ কথা বলবে
                ttsOutput.speak(action.text)
            }
            is Action.OpenApp -> {
                // নিরাপত্তা চেক: অ্যাপটি খোলার অনুমতি আছে কিনা (পেমেন্ট/ব্যাংকিং অ্যাপ ব্লকড কিনা)
                if (AppPolicy.canLaunch(action.packageName)) {
                    val intent = context.packageManager.getLaunchIntentForPackage(action.packageName)
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    } else {
                        ttsOutput.speak("দুঃখিত, অ্যাপটি খুঁজে পাওয়া যায়নি।")
                    }
                } else {
                    // ব্লকড অ্যাপের ক্ষেত্রে
                    ttsOutput.speak("নিরাপত্তার কারণে এই অ্যাপটি খোলা যাবে না।")
                }
            }
            is Action.Error -> {
                // কোনো সমস্যা হলে
                ttsOutput.speak("দুঃখিত, একটি সমস্যা হয়েছে: ${action.message}")
            }
            else -> {
                // অন্য একশনগুলো আপাতত বাকি আছে
                ttsOutput.speak("এই কাজটি এখনো শেখা হয়নি।")
            }
        }
    }
}
