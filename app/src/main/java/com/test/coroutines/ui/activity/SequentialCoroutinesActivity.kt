package com.test.coroutines.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.activity_sequential_tasks.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.system.measureTimeMillis

class SequentialCoroutinesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sequential_tasks)

        mainButton.setOnClickListener {
            sqText.text = ""

            sequentialRequest()
        }

        mainButton1.setOnClickListener {
            sqText.text = ""

            sequentialRequestUsingDifferentCoroutines()
        }
    }

    private fun sequentialRequestUsingDifferentCoroutines() {

        CoroutineScope(IO).launch {

            val elapsedTime = measureTimeMillis {

                val result1 = async {
                    setTextOnMainThread("Calling API1 in: ${Thread.currentThread().name}")
                    getResult1FromApi()
                }.await()

                setTextOnMainThread("Got result 1: $result1")

                val result2 = async {
                    setTextOnMainThread("Calling API2 in: ${Thread.currentThread().name}")
                    getResult2FromApi(result1)
                }.await()

                setTextOnMainThread("Got result 2: $result2")
            }
            setTextOnMainThread("Elapsed time: $elapsedTime")
        }
    }

    private fun sequentialRequest() {
        CoroutineScope(IO).launch {

            val elapsedTime = measureTimeMillis {

                setTextOnMainThread("Calling API1 in: ${Thread.currentThread().name}")
                val result1 = getResult1FromApi()
                setTextOnMainThread("Got result 1: $result1")

                setTextOnMainThread("Calling API2 in: ${Thread.currentThread().name}")
                val result2 = getResult2FromApi(result1)
                setTextOnMainThread("Got result 2: $result2")
            }
            setTextOnMainThread("Elapsed time: $elapsedTime")
        }
    }

    private fun setNewText(text: String) {
        val newText = sqText.text.toString() + "\n$text"
        sqText.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Dispatchers.Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(result1: String): String {
        delay(1700)
        if (result1 == "Result #1") {
            return "Result #2"
        } else {
            throw CancellationException("Result 1 was incorrect...")
        }
    }
}