package com.example.colorflashcards

import androidx.appcompat.app.AppCompatActivity

// The superclass for all other activities in this application
// This class has shared lifecycle code
open class CFActivity : AppCompatActivity() {
    override fun onStart() {
        super.onStart()
        updateLocale()
    }
}