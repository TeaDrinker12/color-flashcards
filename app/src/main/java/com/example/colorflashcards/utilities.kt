package com.example.colorflashcards

import java.text.NumberFormat

// Debug mode
const val DEBUG_MODE = false

// Localization
private var localNumberFormat = NumberFormat.getIntegerInstance()
fun localizedNumber(num: Number) : String {
    return localNumberFormat.format(num.toInt())
}
fun updateLocale() {
    localNumberFormat = NumberFormat.getIntegerInstance()
}