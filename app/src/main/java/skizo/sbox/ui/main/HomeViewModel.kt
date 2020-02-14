package io.comico.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {



    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text


/*
    var content: MutableLiveData<HomeModel> = MutableLiveData<HomeModel>()
    open fun getContent(): LiveData<HomeModel> {

        val job = Job()
        val scope = CoroutineScope(Dispatchers.Default + job)
        scope.launch {
            withContext(Dispatchers.IO) {
                Api.mockService.getHome().send({
                    content.value = it
                })
            }
        }
        return content
    }
    */
}