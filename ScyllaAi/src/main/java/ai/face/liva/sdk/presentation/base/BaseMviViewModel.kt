package ai.face.liva.sdk.presentation.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMviViewModel<I : BaseIntent, S : BaseStateI> : ViewModel() {

    abstract val initialState: S
    private val mutableState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }
    val state: StateFlow<S> get() =  mutableState.asStateFlow()


    private val _intent = Channel<I>(Channel.UNLIMITED)
    protected val intent = _intent.receiveAsFlow()

    fun processState(state:S){

        mutableState.update { state }
    }


    fun processIntent(intent: I) {
        launchOnViewModel {
            _intent.send(intent)
        }
    }

    inline fun launchOnViewModel(crossinline block: suspend () -> Unit) {
        viewModelScope.launch { block() }
    }

    inline fun launchOnIO(crossinline block: suspend () -> Unit) {
        viewModelScope.launch(context = Dispatchers.IO) { block() }
    }

    @CallSuper
    open fun resetState() {
        processState(initialState)
    }
}