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

    private val _results = MutableStateFlow<List<CalculationResult>>(emptyList())
    val results: StateFlow<List<CalculationResult>> = _results

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
        val currentResults = _results.value.toMutableList()
        val existingIndex = currentResults.indexOfFirst { it.inputValue == result.first }
        if (existingIndex == -1) {
            currentResults.add(CalculationResult(result.first, listOf(result.second)))
        } else {
            val existingResult = currentResults[existingIndex]
            val updatedList = existingResult.resultList.toMutableList()
            updatedList.add(result.second)
            currentResults[existingIndex] = existingResult.copy(resultList = updatedList)
        }
        _results.value = currentResults.toList()
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

data class CalculationResult(
    val inputValue: Int,
    val resultList: List<Int>
)