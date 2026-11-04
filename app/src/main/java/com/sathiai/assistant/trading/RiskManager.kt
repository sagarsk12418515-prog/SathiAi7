package com.sathiai.assistant.trading

import kotlin.math.abs

object RiskManager {

    /**
     * পজিশন সাইজ (লট সাইজ) ক্যালকুলেট করা
     */
    fun calculatePositionSize(accountBalance: Double, riskPercentage: Double, entryPrice: Double, stopLossPrice: Double): Double {
        if (entryPrice == stopLossPrice) return 0.0
        val riskAmount = accountBalance * (riskPercentage / 100.0)
        val stopLossDistance = abs(entryPrice - stopLossPrice)
        return riskAmount / stopLossDistance
    }

    /**
     * রিস্ক এবং রিওয়ার্ড রেশিও (R:R) বের করা
     */
    fun calculateRiskReward(entryPrice: Double, stopLossPrice: Double, takeProfitPrice: Double): String {
        val risk = abs(entryPrice - stopLossPrice)
        val reward = abs(takeProfitPrice - entryPrice)
        
        if (risk == 0.0) return "N/A"
        
        val ratio = reward / risk
        return String.format("1:%.2f", ratio)
    }
}
