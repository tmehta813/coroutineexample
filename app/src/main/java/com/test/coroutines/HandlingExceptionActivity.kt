package com.test.coroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutineexamples.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class HandlingExceptionActivity : AppCompatActivity() {

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handling_exception)

        //startJob()
        startSupervisorJob()
    }

    /**
     * CASE 1: Exception in job2, everything gets cancelled that's already running in that parent job
     * and even parent job gets cancelled.
     *
     * CASE 2: Calling cancel() inside a coroutine
     *
     * CASE 3: Calling cancel() on of the child job objects
     *
     * CASE 4: Throwing CancellationException instead of other exceptions
     */
    @InternalCoroutinesApi
    private fun startJob() {
        val handler = CoroutineExceptionHandler { _, throwable ->
            Log.v("Tarun", "Exception in one of the children: $throwable")
        }

        val parentJob = CoroutineScope(IO).launch(handler) {

            //------------JOB 1 -------------
            val job1 = launch {
                Log.v("Tarun", "${doWork(1)}")
            }.invokeOnCompletion {
                if (it != null) {
                    Log.v("Tarun", "Error getting result 1: ${it.message}")
                }
            }

            //------------JOB 2 -------------
            val job2 = launch {
                Log.v("Tarun", "${doWork(2)}")
            }
            //job2.cancel()
            job2.invokeOnCompletion {
                if (it != null) {
                    Log.v("Tarun", "Error getting result 2: ${it.message}")
                }
            }

            //------------JOB 3 -------------
            val job3 = launch {
                Log.v("Tarun", "${doWork(3)}")
            }.invokeOnCompletion {
                if (it != null) {
                    Log.v("Tarun", "Error getting result 3: ${it.message}")
                }
            }
        }

        parentJob.invokeOnCompletion {
            if (it != null) {
                Log.v("Tarun", "Parent job failed: ${it.message}")
            } else {
                Log.v("Tarun", "Parent job Success")
            }
        }
    }

    @InternalCoroutinesApi
    private fun startSupervisorJob() {
        val handler = CoroutineExceptionHandler { _, throwable ->
            Log.v("Tarun", "Exception in one of the children: $throwable")
        }

        val parentJob = CoroutineScope(IO).launch {

            supervisorScope {

                //------------JOB 1 -------------
                val job1 = launch {
                    Log.v("Tarun", "${doWork(1)}")
                }

                //------------JOB 2 -------------
                val job2 = launch(handler) {
                    Log.v("Tarun", "${doWork(2)}")
                }

                //------------JOB 3 -------------
                val job3 = launch {
                    Log.v("Tarun", "${doWork(3)}")
                }
            }
        }

        parentJob.invokeOnCompletion {
            if (it != null) {
                Log.v("Tarun", "Parent job failed: ${it.message}")
            } else {
                Log.v("Tarun", "Parent job Success")
            }
        }
    }

    @InternalCoroutinesApi
    private suspend fun doWork(num: Int): Int {
        delay(num * 500L)
        if (num == 2) {
            throw Exception("Wrong argument: $num")
        }
        return num
    }
}