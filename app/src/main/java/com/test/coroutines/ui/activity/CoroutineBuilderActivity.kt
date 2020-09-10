package com.test.coroutines.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.activity_coroutine_builder.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class CoroutineBuilderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_builder)

        launchButton.setOnClickListener { startCoroutineUsingLaunch() }

        asyncButton.setOnClickListener {
            CoroutineScope(Main).launch { startCoroutineUsingAsync() }
        }

        runBlockingButton.setOnClickListener { startCoroutineUsingRunBlocking() }
    }

    private fun startCoroutineUsingLaunch() {
        val job: Job = CoroutineScope(IO).launch {
            setText("Launch ${getStringResult()}")
        }
    }

    private suspend fun startCoroutineUsingAsync() {
        val deferred: Deferred<String> = CoroutineScope(IO).async {
            return@async getStringResult()
        }

        setText("Await ${deferred.await()}")
    }

    private fun startCoroutineUsingRunBlocking() {
        val result = runBlocking {
            getStringResult()
        }

        CoroutineScope(IO).launch { setText("RunBlocking $result") }
    }

    private suspend fun setText(text: String) {
        withContext(Main) {
            coroutineText.text = text
        }
    }

    private suspend fun getStringResult(): String {
        delay(1000)
        return "Hello Paytm"
    }
}