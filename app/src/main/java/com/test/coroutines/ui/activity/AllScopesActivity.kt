package com.test.coroutines.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.coroutineexamples.R
import com.test.coroutines.ScopeViewModel
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

        customScope.setOnClickListener {
            customScope()
            finish()
        }

        lifeCycleScope.setOnClickListener {
            lifeCycleScope()
            finish()
        }

        globalScope.setOnClickListener {
            globalScope()
            finish()
        }

        viewModelScope.setOnClickListener {
            viewModel.getString()
        }
    }

    private fun setListeners() {
        viewModel.liveData.observe(this, Observer {
            scopeText.text = "Result is: $it"
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
        Log.v("Paytm", "Work Started")
        delay(5000)
        Log.v("Paytm", "Work Ended")
    }
}