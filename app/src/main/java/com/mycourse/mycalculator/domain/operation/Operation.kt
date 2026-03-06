package com.mycourse.mycalculator.domain.operation

// Interface segregation & Dependency Inversion Principle
// Setiap operasi baru cukup implement interface ini tanpa mengubah kode yang sudah ada
interface Operation {
    val symbol: String
    operator fun invoke(a: Double, b: Double): Double
}