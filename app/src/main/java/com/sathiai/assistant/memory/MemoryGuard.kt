package com.sathiai.assistant.memory

import com.sathiai.assistant.speech.TtsOutput

class MemoryGuard(
    private val mistakeMatcher: MistakeMatcher,
    private val ttsOutput: TtsOutput
) {
    /**
     * ট্রেড নেওয়ার আগে রিস্ক চেক করা
     * 
     * @param symbol ট্রেডের সিম্বল (যেমন: XAUUSD)
     * @param riskPercentage ইউজার কত পার্সেন্ট রিস্ক নিচ্ছে
     * @param hasStopLoss ইউজার স্টপ লস সেট করেছে কিনা
     */
    suspend fun checkTradeRisk(symbol: String, riskPercentage: Double, hasStopLoss: Boolean) {
        // ১. স্টপ লস ছাড়া ট্রেড চেক
        if (!hasStopLoss) {
            val warning = "সতর্কতা: আপনি স্টপ লস ছাড়া ট্রেড করতে যাচ্ছেন। এটি একটি বড় ঝুঁকি।"
            ttsOutput.speak(warning)
            // ভুলটি ডেটাবেসে সেভ করা
            mistakeMatcher.recordMistake("Traded without stop loss on $symbol")
            return // এখানেই থেমে যাবে, কারণ এটি হাই রিস্ক
        }

        // ২. নির্ধারিত রিস্কের চেয়ে বেশি রিস্ক নিলে চেক (ধরি ২% এর বেশি)
        if (riskPercentage > 2.0) {
            val warning = "সতর্কতা: আপনার রিস্ক ${riskPercentage}%, যা আপনার নির্ধারিত ২% সীমার চেয়ে বেশি।"
            ttsOutput.speak(warning)
            return
        }

        // ৩. অতীতের ভুলের সাথে মিলে যাচ্ছে কিনা চেক
        val similarMistake = mistakeMatcher.findSimilarMistake("Trading $symbol")
        if (similarMistake != null) {
            ttsOutput.speak("সতর্কতা: আপনি আগে এই ধরনের ভুল করেছিলেন। ${similarMistake.description}")
        } else {
            // সব ঠিক থাকলে
            ttsOutput.speak("রিস্ক ম্যানেজমেন্ট চেক সম্পন্ন।")
        }
    }
}
