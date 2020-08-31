package com.test.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.all_scope.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class AllScopesActivity : AppCompatActivity() {

    private lateinit var viewModel: ScopeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.all_scope)

        viewModel = ViewModelProvider(this).get(ScopeViewModel::class.java)
        setListeners()

        mainButton.setOnClickListener {
            //customScope()
            //lifeCycleScope()
            globalScope()
          //  finish()
            viewModel.getString()
        }
    }

    private fun setListeners() {
        viewModel.liveData.observe(this, Observer {
            Log.v("Tarun", "Result is: $it")
        })
    }

    private fun customScope() {
        CoroutineScope(IO).launch {
            doWork()
        }
    }

    private fun lifeCycleScope() {
        lifecycleScope.launch {
            doWork()
        }
    }

    private fun globalScope() {
        GlobalScope.launch {
            doWork()
        }
    }

    private suspend fun doWork() {
        Log.v("Tarun", "Job Started")
        delay(5000)
        Log.v("Tarun", "Job Ended")
    }
}