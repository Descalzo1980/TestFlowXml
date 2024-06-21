package dev.stas.testflowxml

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.UUID


class MainViewModel : ViewModel() {

    private val _results = MutableStateFlow<List<CalculationResult>>(emptyList())
    val results: StateFlow<List<CalculationResult>> = _results

    fun startSummation(n: Int) {
        viewModelScope.launch {
            val resultList = mutableListOf<Int>()
            val id = UUID.randomUUID()
            combineFlowSummator(n)
                .collect { currentSum ->
                    resultList.add(currentSum)
                    addResult(id, n, resultList.toList())
                }
        }
    }

    private fun addResult(id: UUID, inputValue: Int, resultList: List<Int>) {
        val currentResults = _results.value.toMutableList()
        currentResults.removeAll { it.id == id }
        currentResults.add(CalculationResult(id, inputValue, resultList))
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
    val id: UUID = UUID.randomUUID(),
    val inputValue: Int,
    val resultList: List<Int>
)