package com.sathiai.assistant.trading

object DeepValidator {
    private const val MANDATORY_DISCLAIMER = "This is not investment advice."

    fun validateReport(report: String): String {
        var validatedReport = report
        if (!validatedReport.contains(MANDATORY_DISCLAIMER)) {
            validatedReport += "\n\n$MANDATORY_DISCLAIMER"
        }
        if (validatedReport.contains("Execute trade", ignoreCase = true) || 
            validatedReport.contains("Place order", ignoreCase = true)) {
            validatedReport = validatedReport.replace("Execute trade", "Consider a trade")
                .replace("Place order", "Consider an order")
        }
        return validatedReport
    }
}
