package me.varoa.ugh.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import me.varoa.ugh.ui.base.BaseEvent.ShowErrorMessage

abstract class BaseViewModel : ViewModel() {
    private val eventChannel = Channel<BaseEvent>(Channel.BUFFERED)
    val events: Flow<BaseEvent> = eventChannel.receiveAsFlow()

    protected suspend fun sendNewEvent(event: BaseEvent) {
        eventChannel.send(event)
    }

    protected suspend fun setErrorMessage(message: String?) {
        eventChannel.send(ShowErrorMessage(message ?: ""))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
