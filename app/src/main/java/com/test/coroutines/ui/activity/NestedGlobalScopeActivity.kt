package com.test.coroutines.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.activity_nested_global_scope.*
import kotlinx.android.synthetic.main.activity_parallel_jobs.*
import kotlinx.android.synthetic.main.all_scope.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class NestedGlobalScopeActivity : AppCompatActivity() {

    private lateinit var parentJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nested_global_scope)

        startNormalJob.setOnClickListener {
            textView.text = ""
            startNormalJob()
        }

        startGlobalScopeJob.setOnClickListener {
            textView.text = ""
            startGlobalScopeJob()
        }

        cancelJob.setOnClickListener {
            parentJob.cancel()
        }
    }

    private fun startNormalJob() {
        val startTime = System.currentTimeMillis()

        parentJob = CoroutineScope(Main).launch { // Parent job

            async { // Job 1
                doWork(1)
            }.await()

            async { // Job 2
                doWork(2)
            }.await()

        }

        parentJob.invokeOnCompletion {
            if (it != null) {
                setNewText("Cancelled job after ${System.currentTimeMillis() - startTime} ms")
            } else {
                setNewText("Completed job in ${System.currentTimeMillis() - startTime} ms")
            }
        }
    }

    private fun startGlobalScopeJob() {
        val startTime = System.currentTimeMillis()

        parentJob = GlobalScope.launch(Main) { // Parent job

            GlobalScope.async { // Global scope job
                doWork(1)
            }.await()

            GlobalScope.async { // Global scope job
                doWork(2)
            }.await()

        }

        parentJob.invokeOnCompletion {
            if (it != null) {
                setNewText("Cancelled job after ${System.currentTimeMillis() - startTime} ms")
            } else {
                setNewText("Completed job in ${System.currentTimeMillis() - startTime} ms")
            }
        }
    }

    private suspend fun doWork(i: Int) {
        delay(1500)
        withContext(Main) {
            setNewText("Work $i done!!")
        }
    }

    private fun setNewText(text: String) {
        val newText = textView.text.toString() + "\n$text"
        textView.text = newText
    }

}