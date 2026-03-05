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
            if (state.isEditingFirstNumber) {
                val current = if (state.firstNumber == "0") "" else state.firstNumber
                state.copy(firstNumber = current + digit)
            } else {
                val current = if (state.secondNumber == "0") "" else state.secondNumber
                state.copy(secondNumber = current + digit)
            }
        }
    }

    // Dipanggil saat user memilih operator (+, -, x, ÷)
    fun onOperationSelected() {
        _uiState.update { state ->
            state.copy(
                isEditingFirstNumber = false
            )
        }
    }
}