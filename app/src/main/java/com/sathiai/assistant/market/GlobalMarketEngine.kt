package com.sathiai.assistant.market

data class MarketData(
    val symbol: String,
    val price: Double,
    val change: Double,
    val lastUpdated: String
)

class GlobalMarketEngine {

    fun getMarketData(): List<MarketData> {
        return listOf(
            MarketData("XAUUSD (Gold)", 2350.50, 0.75, "Just now"),
            MarketData("XAGUSD (Silver)", 28.40, -0.15, "Just now"),
            MarketData("US10Y (Bond)", 4.25, 0.02, "Just now"),
            MarketData("DXY (Dollar Index)", 104.50, -0.10, "Just now"),
            MarketData("USOIL (Crude Oil)", 82.30, 1.20, "Just now"),
            MarketData("NATGAS (Natural Gas)", 1.85, -2.10, "Just now")
        )
    }

    fun isDataStale(lastUpdated: String): Boolean {
        return lastUpdated != "Just now"
    }
}
