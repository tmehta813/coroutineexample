package com.test.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScopeViewModel : ViewModel() {

    private val mutableLiveData = MutableLiveData<String>()
    val liveData: LiveData<String> = mutableLiveData

    fun getString() {
        viewModelScope.launch(IO) {

            delay(4000)

            val result = repositoryFunction()
            withContext(Main) {
                mutableLiveData.value = result
            }
        }
    }

    private suspend fun repositoryFunction(): String {
        return "Hello"
    }
}
