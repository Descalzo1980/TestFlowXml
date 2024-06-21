package dev.stas.testflowxml

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _results = MutableLiveData<List<Pair<Int, List<Int>>>>()
    val results: LiveData<List<Pair<Int, List<Int>>>> = _results

    fun startSummation(n: Int) {
        val resultList = mutableListOf<Int>()
        val currentResults = _results.value?.toMutableList() ?: mutableListOf()
        currentResults.add(n to resultList)
        _results.value = currentResults

        viewModelScope.launch {
            combineFlowSummator(n).collect { number ->
                resultList.add(number)
                _results.value = _results.value?.map {
                    if (it.first == n) n to resultList else it
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
                delay(100L)
            }
        }
    }
}