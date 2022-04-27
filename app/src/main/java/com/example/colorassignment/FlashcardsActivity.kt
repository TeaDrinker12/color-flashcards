package com.example.colorassignment

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

const val DEBUG_MODE = false

class FlashcardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards)

        val colors = resources.getStringArray(R.array.color_names).toList()
        val textView = findViewById<TextView>(R.id.color_name_text)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val buttons = listOf<Button>(
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4)
        )
        val picturesForColors = mapOf(*colors.zip(colors.map { color ->
            val resId = resources.getIdentifier(color, "array", packageName)
            val array = resources.getStringArray(resId)
            array.map { it.toString() }
        }).toTypedArray())

        var score = 0

        var currentColor = colors.random()
        var usedColors = listOf<String>()

        val mediaPlayerCorrect = MediaPlayer.create(this, R.raw.correct)
        val mediaPlayerWrong = MediaPlayer.create(this, R.raw.wrong)

        if (DEBUG_MODE) {
            textView.visibility = View.VISIBLE
        }

        val resetColor = {
            usedColors += currentColor
            val colorPool = colors - usedColors
            if (colorPool.size > 0) {
                currentColor = colorPool.random()
            } else {
                usedColors = listOf<String>()
                currentColor = colors.random()
            }

            textView.setText(currentColor)

            val resourceName = picturesForColors[currentColor]?.random()
            val imageResource = resources.getIdentifier(resourceName, "drawable", packageName)
            imageView.setImageResource(imageResource)

            val restOfColors = (colors - currentColor).shuffled().subList(0, 3)
            val withActualColor = (restOfColors + currentColor).shuffled()

            withActualColor.zip(buttons).forEach {
                val colorName = it.first
                val button = it.second
                button.setText(colorName)
            }
        }
        resetColor()

        val onCorrectAnswer = {
            if (!mediaPlayerCorrect.isPlaying()) {
                mediaPlayerCorrect.start()
            } else {
                mediaPlayerCorrect.seekTo(0)
            }
            resetColor()
            score += 1
            if (DEBUG_MODE) {
                Snackbar.make(window.decorView.rootView, "Score: $score", Snackbar.LENGTH_SHORT).show()
            }
        }
        val onWrongAnswer = {
            mediaPlayerWrong.start()
            val intent = Intent(this, ScoreActivity::class.java)
            intent.putExtra("score", score)
            startActivity(intent)

            Handler(Looper.getMainLooper()).postDelayed({
                resetColor()
                score = 0
            }, 1000)
        }


        buttons.forEach { button ->
            button.setOnClickListener {
                if (button.text == currentColor) {
                    onCorrectAnswer()
                } else {
                    onWrongAnswer()
                }
            }
        }

    }
}