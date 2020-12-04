package com.example.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var secondsElapsed: Int = 0

    private lateinit var counterCoroutine: Job

    private suspend fun countSeconds() {
        while (true) {
            delay(1000)
            textSecondsElapsed.text = "Seconds elapsed: " + secondsElapsed++
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPause() {
        super.onPause()
        counterCoroutine.cancel()
    }

    override fun onResume() {
        super.onResume()

        counterCoroutine = GlobalScope.launch(Dispatchers.Main) {
            countSeconds()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(STATE_SECONDS, secondsElapsed)
        }

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.run {
            secondsElapsed = getInt(STATE_SECONDS)
        }
    }

    companion object {
        const val STATE_SECONDS = "spentSeconds"
    }
}