package com.mycourse.mycalculator.di

import com.mycourse.mycalculator.data.CalculatorRepositoryImpl
import com.mycourse.mycalculator.domain.repository.CalculatorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// Dependency Inversion Principle:
// Module ini mengikat abstraksi (interface) ke implementasi konkretnya
// ViewModel tidak perlu tahu tentang CalculatorRepositoryImpl
@Module
@InstallIn(SingletonComponent::class)
abstract class CalculatorModule {

    @Binds
    abstract fun bindCalculatorRepository(
        calculatorRepositoryImpl: CalculatorRepositoryImpl
    ): CalculatorRepository

}