package com.beeper.escaperoom.db

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ExampleViewModel(application: Application): AndroidViewModel(application) {
    private val database = database(application)

    private val _isTransactionOpen = MutableStateFlow(false)
    val isTransactionOpen: StateFlow<Boolean> = _isTransactionOpen.asStateFlow()

    private val _flowableState = MutableStateFlow(Pair(false, false))
    val flowableState: StateFlow<Pair<Boolean, Boolean>> = _flowableState.asStateFlow()

    private val _suspendableState = MutableStateFlow(Pair(false, false))
    val suspendableState: StateFlow<Pair<Boolean, Boolean>> = _suspendableState.asStateFlow()

    private val _flowableExecutionTime = MutableStateFlow<Long?>(null)
    val flowableExecutionTime: StateFlow<Long?> = _flowableExecutionTime.asStateFlow()

    private val _suspendableExecutionTime = MutableStateFlow<Long?>(null)
    val suspendableExecutionTime: StateFlow<Long?> = _suspendableExecutionTime.asStateFlow()

    // Transaction control
    private var transactionCompletion: CompletableDeferred<Unit>? = null
    private var transactionJob: Job? = null

    init {
        // Initialize transaction state as false
        _isTransactionOpen.value = false

        flowableState.onEach { Log.d("ExampleViewModel", "Flowable state changed to $it") }.launchIn(viewModelScope)
        suspendableState.onEach { Log.d("ExampleViewModel", "Suspendable state changed to $it") }.launchIn(viewModelScope)
        isTransactionOpen.onEach { Log.d("ExampleViewModel", "Transaction state changed to $it") }.launchIn(viewModelScope)
    }
    
    fun setTransactionState(isOpen: Boolean) {
        Log.d("ExampleViewModel", "Setting transaction state to $isOpen")
        viewModelScope.launch(Dispatchers.IO) {
            if (isOpen && !_isTransactionOpen.value) {
                // Start a new transaction that will wait for completion signal
                transactionCompletion = CompletableDeferred()
                _isTransactionOpen.value = true

                transactionJob = launch(Dispatchers.IO) {
                    try {
                        database.runInTransaction {
                            Log.d("ExampleViewModel", "Transaction started, waiting for completion signal...")
                            // This will suspend the transaction until we complete the deferred
                            runBlocking { transactionCompletion?.await() }
                            Log.d("ExampleViewModel", "Transaction completing...")
                        }
                        Log.d("ExampleViewModel", "Transaction completed successfully")
                    } catch (e: Exception) {
                        Log.e("ExampleViewModel", "Transaction failed", e)
                    } finally {
                        _isTransactionOpen.value = false
                        transactionCompletion = null
                    }
                }
            } else if (!isOpen && _isTransactionOpen.value) {
                Log.d("ExampleViewModel", "Signaling transaction to complete")
                transactionCompletion?.complete(Unit)
            }
        }
    }

    fun requestSuspendableValue() {
        _suspendableState.value = Pair(false, false)
        _suspendableExecutionTime.value = null

        viewModelScope.launch(Dispatchers.IO) {
            _suspendableState.value = Pair(true, false)

            val startTime = System.currentTimeMillis()

            database.exampleDao().getExample()

            val executionTime = System.currentTimeMillis() - startTime
            _suspendableExecutionTime.value = executionTime
            _suspendableState.value = Pair(true, true)
        }
    }

    fun requestFlowableValue() {
        _flowableState.value = Pair(false, false)
        _flowableExecutionTime.value = null

        viewModelScope.launch(Dispatchers.IO) {
            _flowableState.value = Pair(true, false)

            val startTime = System.currentTimeMillis()

            database.exampleDao().getExampleFlow().first()

            val executionTime = System.currentTimeMillis() - startTime
            _flowableExecutionTime.value = executionTime
            _flowableState.value = Pair(true, true)
        }
    }

    override fun onCleared() {
        if (_isTransactionOpen.value) {
            transactionCompletion?.complete(Unit)
            transactionJob?.cancel()
        }
        super.onCleared()
    }
}