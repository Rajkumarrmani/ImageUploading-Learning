package com.raj.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raj.sample.repository.MainRepository
import com.raj.sample.util.DispatcherProvider
import com.raj.sample.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _setupEvent = MutableSharedFlow<String>()
    val setupEvent: SharedFlow<String> = _setupEvent

    fun test(file: File) {
        viewModelScope.launch(dispatcher.io) {
            val result = repository.uploadImage(file)

            if (result is Resource.Success) {
                _setupEvent.emit("done")
            } else {
                _setupEvent.emit("not_done")
            }
        }
    }
}