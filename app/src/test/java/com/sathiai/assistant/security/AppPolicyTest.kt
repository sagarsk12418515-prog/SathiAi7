package com.sathiai.assistant.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppPolicyTest {

    @Test
    fun testPaymentAppsAreBlocked() {
        // bKash ব্লকড কিনা
        assertEquals(AppClassification.BLOCKED, AppPolicy.classify("com.bkash.customerapp"))
        assertFalse(AppPolicy.canLaunch("com.bkash.customerapp"))

        // Google Pay ব্লকড কিনা
        assertEquals(AppClassification.BLOCKED, AppPolicy.classify("com.google.android.apps.nbu.paisa.user"))
        assertFalse(AppPolicy.canLaunch("com.google.android.apps.nbu.paisa.user"))
    }

    @Test
    fun testTradingAppsRequireConfirmation() {
        // TradingView কনফার্মেশন লাগবে
        assertEquals(AppClassification.CONFIRM_REQUIRED, AppPolicy.classify("com.tradingview.tradingviewapp"))
    }

    @Test
    fun testNormalAppsAreAllowed() {
        // সাধারণ অ্যাপ অ্যালাউড
        assertEquals(AppClassification.ALLOWED, AppPolicy.classify("com.whatsapp"))
        assertTrue(AppPolicy.canLaunch("com.whatsapp"))
    }
}
