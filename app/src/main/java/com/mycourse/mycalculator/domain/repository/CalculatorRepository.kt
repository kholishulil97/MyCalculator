package com.mycourse.mycalculator.domain.repository

import com.mycourse.mycalculator.domain.operation.Operation

// Dependency Inversion Principle:
// ViewModel bergantung pada abstraksi (interface), bukan implementasi konkret
interface CalculatorRepository {
    fun calculate(a: Double, b: Double, operation: Operation): Double
}