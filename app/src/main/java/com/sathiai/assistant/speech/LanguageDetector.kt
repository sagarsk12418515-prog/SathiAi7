package com.sathiai.assistant.speech

class LanguageDetector {

    /**
     * টেক্সটে বাংলা অক্ষর আছে কিনা চেক করে
     * (বাংলা ইউনিকোড ব্লক: U+0980 থেকে U+09FF)
     */
    fun isBengali(text: String): Boolean {
        val bengaliRegex = Regex("[\\u0980-\\u09FF]")
        return bengaliRegex.containsMatchIn(text)
    }

    /**
     * টেক্সটের প্রধান ভাষা শনাক্ত করে
     * রিটার্ন করে: "bn-BD" (বাংলা), "hi-IN" (হিন্দি), অথবা ডিফল্ট "en-US" (ইংরেজি)
     */
    fun detectLanguage(text: String): String {
        return when {
            isBengali(text) -> "bn-BD"
            // খুব সাধারণ হিন্দি ডিটেকশন (দেবনাগরী লিপি)
            Regex("[\\u0900-\\u097F]").containsMatchIn(text) -> "hi-IN"
            else -> "en-US" // ডিফল্ট ইংরেজি
        }
    }

    /**
     * ইউজার "থামো" বা "STOP" বলেছে কিনা চেক করে
     */
    fun isStopCommand(text: String): Boolean {
        val lowerText = text.trim().lowercase()
        return lowerText == "থামো" || 
               lowerText == "থাম" || 
               lowerText.contains("stop") || 
               lowerText.contains("বন্ধ করো")
    }
}
