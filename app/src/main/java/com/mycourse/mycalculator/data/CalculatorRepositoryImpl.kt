package com.mycourse.mycalculator.data

import com.mycourse.mycalculator.domain.operation.Operation
import com.mycourse.mycalculator.domain.repository.CalculatorRepository
import javax.inject.Inject

// Open/Closed Principle:
// Untuk menambah operasi baru, cukup tambahkan ke list ini
// tanpa mengubah logika yang sudah ada
class CalculatorRepositoryImpl @Inject constructor() : CalculatorRepository {
    override fun calculate(a: Double, b: Double, operation: Operation): Double {
        return operation(a, b)
    }
}