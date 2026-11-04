package com.sathiai.assistant.trading

object DeepValidator {

    private const val MANDATORY_DISCLAIMER = "This is not investment advice."

    /**
     * অ্যানালাইসিস রিপোর্টটি ভ্যালিডেট করা
     */
    fun validateReport(report: String): String {
        var validatedReport = report

        // ১. নিশ্চিত করা যে ডিসক্লেইমারটি আছে
        if (!validatedReport.contains(MANDATORY_DISCLAIMER)) {
            validatedReport += "\n\n$MANDATORY_DISCLAIMER"
        }

        // ২. নিশ্চিত করা যে এটি সরাসরি ট্রেড এক্সিকিউট করার নির্দেশ দেয় না
        // (এটি শুধু টেক্সট ফিল্টার, প্রকৃত ট্রেড এক্সিকিউশন Executor-এ ব্লকড থাকে)
        if (validatedReport.contains("Execute trade", ignoreCase = true) || 
            validatedReport.contains("Place order", ignoreCase = true)) {
            validatedReport = validatedReport.replace("Execute trade", "Consider a trade")
                .replace("Place order", "Consider an order")
        }

        return validatedReport
    }
}
