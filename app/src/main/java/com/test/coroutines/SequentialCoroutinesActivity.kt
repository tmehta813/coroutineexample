package com.test.coroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.activity_parallel_jobs.button
import kotlinx.android.synthetic.main.activity_sequential_tasks.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.system.measureTimeMillis

class SequentialCoroutinesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sequential_tasks)

        button.setOnClickListener {
            setNewText("Clicked!!")

            sequentialRequestUsingDifferentCoroutines()
            // sequentialRequest()
        }
    }

    private fun sequentialRequestUsingDifferentCoroutines() {

        CoroutineScope(IO).launch {

            val elapsedTime = measureTimeMillis {

                val result1 = async {
                    Log.v("Tarun", "Launching job1 in: ${Thread.currentThread().name}")
                    getResult1FromApi()
                }.await()

                Log.v("Tarun", "Got result 1: $result1")

                val result2 = async {
                    Log.v("Tarun", "Launching job2 in: ${Thread.currentThread().name}")
                    getResult2FromApi(result1)
                }.await()

                Log.v("Tarun", "Got result 2: $result2")
            }
            Log.v("Tarun", "Elapsed time: $elapsedTime")
        }
    }

    private fun sequentialRequest() {
        CoroutineScope(IO).launch {

            val elapsedTime = measureTimeMillis {

                Log.v("Tarun", "Launching job1 in: ${Thread.currentThread().name}")
                val result1 = getResult1FromApi()
                Log.v("Tarun", "Got result 1: $result1")

                Log.v("Tarun", "Launching job2 in: ${Thread.currentThread().name}")
                val result2 = getResult2FromApi(result1)
                Log.v("Tarun", "Got result 2: $result2")
            }
            Log.v("Tarun", "Elapsed time: $elapsedTime")
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
        if (result1.equals("Result #1")) {
            return "Result #2"
        } else {
            throw CancellationException("Result 1 was incorrect...")
        }
    }
}