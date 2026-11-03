package com.sathiai.assistant.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class SathiAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    fun tap(x: Float, y: Float): Boolean {
        val path = Path()
        path.moveTo(x, y)
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(path, 0, 100))
        return dispatchGesture(gestureBuilder.build(), null, null)
    }

    fun scroll(forward: Boolean): Boolean {
        val rootNode = rootInActiveWindow ?: return false
        return rootNode.performAction(
            if (forward) AccessibilityNodeInfo.ACTION_SCROLL_FORWARD 
            else AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
        )
    }

    fun typeText(text: String): Boolean {
        val focusedNode = findFocus(AccessibilityNodeInfo.FOCUS_INPUT) ?: return false
        if (focusedNode.isPassword) {
            Toast.makeText(this, "Security Alert: Cannot type in password fields.", Toast.LENGTH_SHORT).show()
            return false
        }
        val arguments = Bundle()
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
        return focusedNode.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
    }
}
