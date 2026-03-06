package com.mycourse.mycalculator.presentation

import androidx.lifecycle.ViewModel
import com.mycourse.mycalculator.domain.model.CalculatorUiState
import com.mycourse.mycalculator.domain.operation.Operation
import com.mycourse.mycalculator.domain.repository.CalculatorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// Dependency Inversion Principle:
// ViewModel bergantung pada CalculatorRepository (abstraksi), bukan implementasi konkret
@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val repository: CalculatorRepository
): ViewModel() {

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
    fun onOperationSelected(operation: Operation) {
        _uiState.update { state ->
            if (state.firstNumber == "0") {
                state.copy(
                    isEditingFirstNumber = true,
                    selectedOperation = null)
            } else {
                state.copy(
                    isEditingFirstNumber = false,
                    selectedOperation = operation)
            }

        }
    }

    // Dipanggil saat user menghapus satu karakter terakhir
    fun onBackspace() {
        _uiState.update { state ->
            if (state.isEditingFirstNumber) {
                state.copy(firstNumber = state.firstNumber.dropLast(1).ifEmpty { "0" })
            } else {
                val updated = state.secondNumber.dropLast(1).ifEmpty { "0" }
                state.copy(
                    secondNumber = updated,
                    isEditingFirstNumber = updated == "0"
                )
            }
        }
    }

    // Dipanggil saat user menekan tombol clear
    fun onClear() {
        _uiState.update { CalculatorUiState() }
    }

    // Dipanggil saat user mengetik titik desimal
    fun onDecimalInput() {
        _uiState.update { state ->
            if (state.isEditingFirstNumber) {
                if (!state.firstNumber.contains("."))
                    state.copy(firstNumber = state.firstNumber + ".")
                else state
            } else {
                if (!state.secondNumber.contains("."))
                    state.copy(secondNumber = state.secondNumber + ".")
                else state
            }
        }
    }

    // Dipanggil saat user menekan tombol "="
    fun onCalculate() {
        _uiState.update { state ->
            val operation = state.selectedOperation ?: return@update state
            val a = state.firstNumber.toDoubleOrNull() ?: return@update state
            val b = state.secondNumber.toDoubleOrNull() ?: return@update state

            try {
                val result = repository.calculate(a, b, operation)
                state.copy(
                    result = formatResult(result),
                    errorMessage = null

                )
            } catch (e: Exception) {
                state.copy(
                    errorMessage = e.message
                )
            }
        }
    }

    private fun formatResult(value: Double): String {
        return if (value % 1 == 0.0) value.toLong().toString()
        else value.toBigDecimal().stripTrailingZeros().toPlainString()
    }
}