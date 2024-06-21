package dev.stas.testflowxml

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _results = MutableStateFlow<List<Pair<Int, List<Int>>>>(emptyList())
    val results: StateFlow<List<Pair<Int, List<Int>>>> = _results

    fun startSummation(n: Int) {
        viewModelScope.launch {
            val resultList = mutableListOf<Int>()
            combineFlowSummator(n)
                .collect { number ->
                    resultList.add(number)
                    _results.value = _results.value.toMutableList().apply {
                        add(n to resultList.toList())
                    }
                }
        }
    }

    private fun combineFlowSummator(n: Int): Flow<Int> {
        return flow {
            var currentSum = 0
            for (i in 1..n) {
                currentSum += i
                emit(currentSum)
                kotlinx.coroutines.delay(100L)
            }
        }
    }
}