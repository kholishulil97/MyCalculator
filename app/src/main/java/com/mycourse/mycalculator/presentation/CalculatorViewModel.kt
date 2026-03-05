package com.mycourse.mycalculator.presentation

import androidx.lifecycle.ViewModel
import com.mycourse.mycalculator.domain.model.CalculatorUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    // Dipanggil saat user mengetik angka
    fun onNumberInput(digit: String) {
        _uiState.update { state ->
            val current = if (state.firstNumber == "0") "" else state.firstNumber
            state.copy(firstNumber = current + digit)
        }
    }
}