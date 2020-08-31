package com.test.coroutines

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
        parallelApiRequestUsingJob()

        button.setOnClickListener {
            setNewText("Clicked!!")

          //  parallelApiRequestUsingJob()
         //  parallelApiRequestUsingAsyncAwait()
            parentJob.cancel()
        }
    }

    private fun parallelApiRequestUsingJob() {
        val startTime = System.currentTimeMillis()
        parentJob = CoroutineScope(IO).launch {

                val job1 = launch {
                    Log.v("Tarun", "Launching job1 in thread; ${Thread.currentThread().name}")
                    val result1 = getResult1FromApi()
                    setTextOnMainThread("Got $result1")
                }

                val job2 = launch {
                    Log.v("Tarun", "Launching job2 in thread; ${Thread.currentThread().name}")
                    val result2 = getResult2FromApi()
                    setTextOnMainThread("Got $result2")
                }
        }

        parentJob.invokeOnCompletion {
            Log.v("Tarun", "ElapsedTime: ${System.currentTimeMillis() - startTime}")
        }
    }

    private fun parallelApiRequestUsingAsyncAwait() {
        CoroutineScope(IO).launch {
            val elapsedTime = measureTimeMillis {
                val result1 = async {
                    Log.v("Tarun", "Launching job1 in thread; ${Thread.currentThread().name}")
                    getResult1FromApi()
                }

                val result2 = async {
                    Log.v("Tarun", "Launching job2 in thread; ${Thread.currentThread().name}")
                    getResult2FromApi()
                }

                setTextOnMainThread("Got ${result1.await()}")
                setTextOnMainThread("Got ${result2.await()}")
            }
            Log.v("Tarun", "ElapsedTime:${elapsedTime}")
        }
    }

    private fun setNewText(text: String) {
        val newText = textView.text.toString() + "\n$text"
        textView.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(4000)
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1700)
        return "Result #2"
    }
}