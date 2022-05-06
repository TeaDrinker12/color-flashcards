package com.example.colorflashcards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ScoreActivity : CFActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
    }

    override fun onResume() {
        super.onResume()
        val score = intent.extras?.getInt("score") ?: 0
        findViewById<TextView>(R.id.score_text).setText(localizedNumber(score))

        findViewById<Button>(R.id.play_again).setOnClickListener {
            finish()
        }
    }
}