package com.mycourse.mycalculator.domain.model

import com.mycourse.mycalculator.domain.operation.Operation

data class CalculatorUiState(
    val firstNumber: String = "0",
    val secondNumber: String = "0",
    val selectedOperation: Operation? = null,
    val isEditingFirstNumber: Boolean = true
)
