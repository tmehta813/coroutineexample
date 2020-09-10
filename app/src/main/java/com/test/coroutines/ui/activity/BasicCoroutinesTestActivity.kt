package com.test.coroutines.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.basic_coroutine.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class BasicCoroutinesTestActivity : AppCompatActivity() {

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.basic_coroutine)

        mainButton.setOnClickListener {
            if (counter == 5) {
                blockThread()
            }
            setText()
        }

        mainButton1.setOnClickListener {
            if (counter == 5) {
                doNotBlockMainThread()
            }
            setText()
        }

        mainButton2.setOnClickListener {
            if (counter == 5) {
                blockMainThreadUsingCoroutine()
            }
            setText()
        }

        mainButton3.setOnClickListener {
            counter = 0
            setText()
        }
    }

    /**
     * Launched new Coroutine using Coroutine Builder "launch"
     */
    private fun doNotBlockMainThread() {
         CoroutineScope(Dispatchers.IO).launch {
            Log.v("Paytm",Thread.currentThread().name)
            Log.v("Paytm", "Job started")
            delay(4000)
            Log.v("Paytm", "Job ended")
        }

    }

    /**
     * Used For Testing Purposes
     */
    private fun blockMainThreadUsingCoroutine() {
        runBlocking {
            Log.v("Paytm", "Job started")
            delay(4000)
            Log.v("Paytm", "Job ended")
        }
    }

    private fun blockThread() {
        Thread.sleep(4000)
    }

    private fun setText() {
        mainText.text = (++counter).toString()
    }
}