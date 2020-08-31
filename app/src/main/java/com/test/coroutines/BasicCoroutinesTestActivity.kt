package com.test.coroutines

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
                //blockThread()
                doNotBlockMainThread()
                //blockMainThreadUsingCoroutine()
            }
            mainText.text = (++counter).toString()
            Log.v("counter",counter.toString())
        }
    }

    private fun doNotBlockMainThread() {
         CoroutineScope(IO).launch {
            Log.v("Tarun",Thread.currentThread().name)
            Log.v("Tarun", "Job started")
            delay(4000)
            Log.v("Tarun", "Job ended")
        }

    }

    private fun blockMainThreadUsingCoroutine() {
        runBlocking {
            Log.v("Tarun", "Job started")
            delay(4000)
            Log.v("Tarun", "Job ended")
        }
    }

    private fun blockThread() {
        Thread.sleep(4000)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}