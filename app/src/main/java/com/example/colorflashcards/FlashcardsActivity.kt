package com.example.colorflashcards

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class FlashcardsActivity : CFActivity() {
    val colors by lazy { resources.getStringArray(R.array.color_names).toList() }
    val colorTextView by lazy { findViewById<TextView>(R.id.color_name_text) }
    val timerTextView by lazy { findViewById<TextView>(R.id.timer_text) }
    val imageView by lazy { findViewById<ImageView>(R.id.imageView) }
    val buttons by lazy {
        listOf<Button>(
        findViewById(R.id.button1),
        findViewById(R.id.button2),
        findViewById(R.id.button3),
        findViewById(R.id.button4)
    )
    }
    val picturesForColors by lazy {
        mapOf(*colors.zip(colors.map { color ->
        val resId = resources.getIdentifier(color, "array", packageName)
        val array = resources.getStringArray(resId)
        array.map { it.toString() }
    }).toTypedArray())
    }

    val stringsForColors by lazy {
        mapOf(*colors.zip(colors.map { color ->
            val stringId = color.lowercase()
            val resId = resources.getIdentifier(stringId, "string", packageName)
            val string = resources.getString(resId)
            string.toString()
        }).toTypedArray())
    }

    var score = 0

    lateinit var currentColor: String
    var usedColors = listOf<String>()

    val mediaPlayerCorrect by lazy { MediaPlayer.create(this, R.raw.correct) }
    val mediaPlayerWrong by lazy { MediaPlayer.create(this, R.raw.wrong) }

    lateinit var countdown: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards)

        if (DEBUG_MODE) {
            colorTextView.visibility = View.VISIBLE
        }

        currentColor = colors.random()

        buttons.forEach { button ->
            button.setOnClickListener {
                if (button.text == stringsForColors[currentColor]) {
                    onCorrectAnswer()
                } else {
                    onLose()
                }
            }
        }

        countdown = object: CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.setText(localizedNumber(millisUntilFinished / 1000 + 1))
            }

            override fun onFinish() {
                timerTextView.setText(localizedNumber(0))
                onLose()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resetColor()
        this.score = 0
        this.countdown.start()
    }

    override fun onPause() {
        super.onPause()
        this.countdown.cancel()
    }

    fun onCorrectAnswer() {
        if (!mediaPlayerCorrect.isPlaying()) {
            mediaPlayerCorrect.start()
        } else {
            mediaPlayerCorrect.seekTo(0)
        }

        resetCountdown()
        resetColor()
        score += 1
        if (DEBUG_MODE) {
            Snackbar.make(window.decorView.rootView, "Score: $score", Snackbar.LENGTH_SHORT).show()
        }
    }

    fun resetCountdown() {
        this.countdown.cancel()
        this.countdown.start()
    }

    fun onLose() {
        mediaPlayerWrong.start()
        val intent = Intent(this, ScoreActivity::class.java)
        intent.putExtra("score", score)
        startActivity(intent)
    }

    fun resetColor() {
        usedColors += currentColor
        val colorPool = colors - usedColors
        if (colorPool.isNotEmpty()) {
            currentColor = colorPool.random()
        } else {
            usedColors = listOf<String>()
            currentColor = colors.random()
        }

        colorTextView.setText(currentColor)

        val resourceName = picturesForColors[currentColor]?.random()
        val imageResource = resources.getIdentifier(resourceName, "drawable", packageName)
        imageView.setImageResource(imageResource)

        val restOfColors = (colors - currentColor).shuffled().subList(0, 3)
        val withActualColor = (restOfColors + currentColor).shuffled()

        withActualColor.zip(buttons).forEach {
            val colorName = it.first
            val button = it.second
            button.setText(stringsForColors[colorName])
        }
    }
}