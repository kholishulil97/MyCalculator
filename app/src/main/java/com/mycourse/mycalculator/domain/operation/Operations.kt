package com.mycourse.mycalculator.domain.operation

// Model
// Single Responsibility Principle:
// Setiap kelas hanya bertanggung jawab atas satu operasi aritmatika
class Plus : Operation {
    override val symbol: String = "+"
}

class Minus : Operation {
    override val symbol: String = "-"
}

class Multiply : Operation {
    override val symbol: String = "x"
}

class Divide : Operation {
    override val symbol: String = "÷"
}