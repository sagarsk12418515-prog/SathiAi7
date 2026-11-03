package com.sathiai.assistant.ai

sealed class Action {
    data class Speak(val text: String) : Action()
    data class OpenApp(val packageName: String) : Action()
    data class Tap(val x: Float, val y: Float) : Action()
    data class Scroll(val forward: Boolean) : Action()
    data class TypeText(val text: String) : Action()
    data class Error(val message: String) : Action()
}
