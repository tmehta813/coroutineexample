package com.test.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.api_timeout.*
import kotlinx.coroutines.*

class ApiTimeoutActivity : AppCompatActivity() {

    private val RESULT1 = "Result #1"

    private val RESULT2 = "Result #2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.api_timeout)

        button.setOnClickListener {
            // Used to create new coroutine
            CoroutineScope(Dispatchers.IO).launch {
                callFakeApi()
            }
        }
    }

    private suspend fun callFakeApi() {
        val job = withTimeoutOrNull(2100L) {
            fakeApiRequest()
        }

        if(job == null) {
            setTextOnMainThread("Cancelling job...took longer than 2100 ms")
        }
    }

    /**
     * Switching coroutine context instead of creating new one
     */
    private suspend fun setTextOnMainThread(input: String) {
        withContext(Dispatchers.Main) {
            val newText = text.text.toString() + "\n${input}"
            text.text = newText
        }
    }

    private suspend fun fakeApiRequest() {
        val result1 = getResult1FromApi()
        Log.v("Tarun", "${result1}")
        setTextOnMainThread(result1)

        val result2 = getResult2FromApi(result1)
        Log.v("Tarun", "${result2}")
        setTextOnMainThread(result2)
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000) // Suspends this coroutine and not current thread
        return RESULT1
    }

    private suspend fun getResult2FromApi(result1: String): String {
        logThread("getResult2FromApi")
        delay(2000) // Suspends this coroutine and not current thread
        return result1 + RESULT2
    }

    private fun logThread(methodName: String) {
        Log.v("Tarun", "${methodName}: Called From ${Thread.currentThread().name}")
    }
}