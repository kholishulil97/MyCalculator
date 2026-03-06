package com.mycourse.mycalculator.domain.operation

// Model
// Single Responsibility Principle:
// Setiap kelas hanya bertanggung jawab atas satu operasi aritmatika
class Plus : Operation {
    override val symbol: String = "+"
    override operator fun invoke(a: Double, b: Double): Double = a + b
}

class Minus : Operation {
    override val symbol: String = "-"
    override operator fun invoke(a: Double, b: Double): Double = a - b
}

class Multiply : Operation {
    override val symbol: String = "x"
    override operator fun invoke(a: Double, b: Double): Double = a * b
}

class Divide : Operation {
    override val symbol: String = "÷"
    override operator fun invoke(a: Double, b: Double): Double {
        if (b == 0.0) throw ArithmeticException("Cannot divide by zero")
        return a / b
    }
}