package com.example.colorassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ScoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        val score = intent.extras?.getInt("score")
        findViewById<TextView>(R.id.score_text).setText(score.toString())

        findViewById<Button>(R.id.play_again).setOnClickListener {
            finish()
        }
    }
}