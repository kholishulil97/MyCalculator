package com.mycourse.mycalculator.domain.model

data class CalculatorUiState(
    val firstNumber: String = "0",
    val secondNumber: String = "0",
    val isEditingFirstNumber: Boolean = true
)
