package com.example.timer


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var secondsElapsed: Int = 0
    var appIsOnScreen: Boolean = false

    private var backgroundThread = Thread {
        while (!Thread.interrupted()) try {
            if (appIsOnScreen) {
                Thread.sleep(1000)
                textSecondsElapsed.post {
                    textSecondsElapsed.text = "Seconds elapsed: ${secondsElapsed++}"
                }
            }
        } catch (e: InterruptedException) {
            return@Thread
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        backgroundThread.start()
    }

    override fun onPause() {
        super.onPause()
        appIsOnScreen = false
    }

    override fun onResume() {
        super.onResume()
        appIsOnScreen = true
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

    override fun onDestroy() {
        super.onDestroy()
        backgroundThread.interrupt()
    }

    companion object {
        const val STATE_SECONDS = "spentSeconds"
    }
}