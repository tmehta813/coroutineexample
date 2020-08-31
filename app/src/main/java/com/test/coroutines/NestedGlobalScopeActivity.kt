package com.test.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.activity_nested_global_scope.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class NestedGlobalScopeActivity : AppCompatActivity() {

    private lateinit var parentJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_global_scope)

        testingGlobalScope()
        //jobCancelFeature()

        mainButton.setOnClickListener {
            parentJob.cancel()
        }
    }

    private fun jobCancelFeature() {
        val startTime = System.currentTimeMillis()

        parentJob = CoroutineScope(Main).launch { // Parent job

            launch { // Job 1
                doWork(1)
            }

            launch { // Job 2
                doWork(2)
            }

        }

        parentJob.invokeOnCompletion {
            if (it != null) {
                Log.v("Tarun", "Cancelled job after ${System.currentTimeMillis() - startTime} ms")
            } else {
                Log.v("Tarun", "Completed job in ${System.currentTimeMillis() - startTime} ms")
            }
        }
    }

    private fun testingGlobalScope() {
        val startTime = System.currentTimeMillis()

        parentJob = CoroutineScope(Main).launch { // Parent job

            GlobalScope.launch { // Global scope job
                doWork(1)
            }

            GlobalScope.launch { // Global scope job
                doWork(2)
            }

            delay(4000)

        }

        parentJob.invokeOnCompletion {
            if (it != null) {
                Log.v("Tarun", "Cancelled job after ${System.currentTimeMillis() - startTime} ms")
            } else {
                Log.v("Tarun", "Completed job in ${System.currentTimeMillis() - startTime} ms")
            }
        }
    }

    private suspend fun doWork(i: Int) {
        delay(3000)
        Log.v("Tarun", "Work $i done on: ${Thread.currentThread().name}")
    }
}