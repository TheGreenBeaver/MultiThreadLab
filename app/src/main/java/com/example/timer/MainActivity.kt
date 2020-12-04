package com.example.timer

import android.os.AsyncTask
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private lateinit var timerTask: BackgroundTimerTask
    private var shouldCount: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
    }

    override fun onPause() {
        super.onPause()
        shouldCount = false
    }

    override fun onResume() {
        super.onResume()
        shouldCount = true
        timerTask = BackgroundTimerTask()
        timerTask.execute(secondsElapsed)
    }

    override fun onStop() {
        super.onStop()
        timerTask.cancel(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(STATE_SECONDS, secondsElapsed)
        }

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.run {
            secondsElapsed = getInt(STATE_SECONDS)
        }

        super.onRestoreInstanceState(savedInstanceState)
    }

    companion object {
        const val STATE_SECONDS = "spentSeconds"
    }

    inner class BackgroundTimerTask : AsyncTask<Int?, Int?, Void?>() {

        override fun doInBackground(vararg params: Int?): Void? {
            var initialTime = params[0]!!
            while (!isCancelled && !Thread.interrupted()) try {
                if (shouldCount) {
                    Thread.sleep(1000)
                    publishProgress(initialTime++)
                }
            } catch (e: InterruptedException) {
                return null
            }
            return null
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            secondsElapsed = values[0]!!
            textSecondsElapsed.text = "Seconds elapsed: ${values[0]}"
        }
    }
}