package com.test.coroutines

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listOf(button1, button2, button3, button4, button5, button6, button7, button8).forEach {
            it.setOnClickListener(this)
        }

    }

    override fun onClick(v: View?) {
        var intent: Intent? = null
        v.let {
            when (it!!.id) {
                R.id.button1 -> intent =
                    Intent(this@MainActivity, BasicCoroutinesTestActivity::class.java)
                R.id.button2 -> intent =
                    Intent(this@MainActivity, AllScopesActivity::class.java)
                R.id.button3 -> intent =
                    Intent(this@MainActivity, NestedGlobalScopeActivity::class.java)
                R.id.button4 -> intent =
                    Intent(this@MainActivity, SequentialCoroutinesActivity::class.java)
                R.id.button5 -> intent =
                    Intent(this@MainActivity, JobsParallelActivity::class.java)
                R.id.button6 -> intent =
                    Intent(this@MainActivity, CoroutinesJobActivity::class.java)
                R.id.button7 -> intent =
                    Intent(this@MainActivity, ApiTimeoutActivity::class.java)
                R.id.button8 -> intent =
                    Intent(this@MainActivity, HandlingExceptionActivity::class.java)
            }

        startActivity(intent)

        }


    }
}