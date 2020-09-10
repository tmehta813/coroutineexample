package com.test.coroutines.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.activity_parallel_jobs.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.system.measureTimeMillis

class JobsParallelActivity : AppCompatActivity() {

    private lateinit var parentJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parallel_jobs)

        mainButton.setOnClickListener {
            sqText.text = ""
            parallelApiRequestUsingJob()
        }

        mainButton1.setOnClickListener {
            sqText.text = ""
            parallelApiRequestUsingAsyncAwait()
        }
    }

    private fun parallelApiRequestUsingJob() {
        val startTime = System.currentTimeMillis()
        parentJob = CoroutineScope(IO).launch {

                val job1 = launch {
                    setTextOnMainThread("Launching Api1 in thread: ${Thread.currentThread().name}")
                    val result1 = getResult1FromApi()
                    setTextOnMainThread("Got $result1")
                }

                val job2 = launch {
                    setTextOnMainThread("Launching Api2 in thread: ${Thread.currentThread().name}")
                    val result2 = getResult2FromApi()
                    setTextOnMainThread("Got $result2")
                }
        }

        parentJob.invokeOnCompletion {
            setNewText("ElapsedTime: ${System.currentTimeMillis() - startTime}")
        }
    }

    private fun parallelApiRequestUsingAsyncAwait() {
        CoroutineScope(IO).launch {

            val elapsedTime = measureTimeMillis {

                val result1 = async {
                    setTextOnMainThread("Launching Api1 in thread: ${Thread.currentThread().name}")
                    getResult1FromApi()
                }

                val result2 = async {
                    setTextOnMainThread("Launching Api2 in thread: ${Thread.currentThread().name}")
                    getResult2FromApi()
                }

                setTextOnMainThread("Got ${result1.await()}")
                setTextOnMainThread("Got ${result2.await()}")
            }
            setTextOnMainThread("ElapsedTime:${elapsedTime}")
        }
    }

    private fun setNewText(text: String) {
        val newText = sqText.text.toString() + "\n$text"
        sqText.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1700)
        return "Result #2"
    }
}