package com.test.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.example.coroutineexamples.R
import kotlinx.android.synthetic.main.activity_jobs.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

const val PROGRESS_MAX = 100
const val PROGRESS_START = 0

const val JOB_TIME = 4000 //ms

class CoroutinesJobActivity : AppCompatActivity() {

    private lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jobs)

        job_button.setOnClickListener {
            if(!::job.isInitialized) {
                initJob()
            }
            job_progress_bar.startJobOrCancel(job)
        }
    }

    /**
     * Starts job if not started and cancels job if started
     */
    private fun ProgressBar.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            Log.v("Tarun", "$job is already active. Cancelling...")
            resetJob()
        } else {
            job_button.setText("Cancel Job #1")
            CoroutineScope(job + IO).launch {
                Log.v("Tarun", "coroutine $this is activated with $job")
                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay(JOB_TIME / PROGRESS_MAX.toLong())
                    this@startJobOrCancel.progress = i
                }
                updateJobCompleteTextView("Job is Complete")
            }
        }
    }

    private fun updateJobCompleteTextView(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            job_complete_text.text = text
        }
    }

    private fun resetJob() {
        if (job.isActive || job.isCompleted) {
            job.cancel(CancellationException("Resetting job"))
        }
        initJob()
    }

    private fun initJob() {
        job_button.setText("Start job #1")
        updateJobCompleteTextView("")
        job = Job()
        job.invokeOnCompletion {
            it?.message.run {
                var msg = this
                if(msg.isNullOrBlank()) {
                    msg = "Unknown Cancellation Error"
                }
                Log.v("Tarun", "$job was cancelled due to: $msg")
                showToast(msg)
            }
        }
        job_progress_bar.max = PROGRESS_MAX
        job_progress_bar.progress = PROGRESS_START
    }

    private fun showToast(text: String) {
        GlobalScope.launch(Dispatchers.Main) {
            Toast.makeText(this@CoroutinesJobActivity, text, Toast.LENGTH_SHORT).show()
        }
    }
}