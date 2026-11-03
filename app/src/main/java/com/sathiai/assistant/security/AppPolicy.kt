package com.sathiai.assistant.security

object AppPolicy {
    private val HARD_BLOCKED_PACKAGES = setOf(
        "com.google.android.apps.walletnfcrel",
        "com.google.android.apps.nbu.paisa.user",
        "net.one97.paytm",
        "com.paypal.android.p2pmobile",
        "com.bkash.customerapp",
        "com.konasl.nagad",
        "com.dbbl.mbs"
    )

    private val CONFIRM_REQUIRED_PACKAGES = setOf(
        "com.tradingview.tradingviewapp",
        "net.metaquotes.metatrader5"
    )

    private val userBlockedPackages = mutableSetOf<String>()

    fun addUserBlockedPackage(packageName: String) {
        userBlockedPackages.add(packageName)
    }

    fun classify(packageName: String): AppClassification {
        return when {
            HARD_BLOCKED_PACKAGES.contains(packageName) || userBlockedPackages.contains(packageName) -> AppClassification.BLOCKED
            CONFIRM_REQUIRED_PACKAGES.contains(packageName) -> AppClassification.CONFIRM_REQUIRED
            else -> AppClassification.ALLOWED
        }
    }

    fun canLaunch(packageName: String): Boolean {
        return classify(packageName) == AppClassification.ALLOWED
    }
}
