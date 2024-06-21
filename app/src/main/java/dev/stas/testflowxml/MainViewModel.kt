package dev.stas.testflowxml

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _results = MutableStateFlow<List<Pair<Int, List<Int>>>>(emptyList())
    val results: StateFlow<List<Pair<Int, List<Int>>>> = _results

    fun startSummation(n: Int) {
        viewModelScope.launch {
            val channel = Channel<Pair<Int, Int>>()
            combineFlowSummator(n)
                .onEach { number ->
                    channel.send(n to number)
                    delay(100L)
                }
                .launchIn(this)
            for (result in channel) {
                addResult(result)
            }
            channel.close()
        }
    }

    private fun addResult(result: Pair<Int, Int>) {
        val updatedResults = _results.value.toMutableList()
        val existing = updatedResults.find { it.first == result.first }
        if (existing == null) {
            updatedResults.add(result.first to listOf(result.second))
        } else {
            val index = updatedResults.indexOf(existing)
            val newValues = existing.second.toMutableList()
            newValues.add(result.second)
            updatedResults[index] = result.first to newValues
        }
        _results.value = updatedResults
    }

    private fun combineFlowSummator(n: Int): Flow<Int> {
        return flow {
            var currentSum = 0
            for (i in 1..n) {
                currentSum += i
                emit(currentSum)
                delay(100L)
            }
        }
    }
}