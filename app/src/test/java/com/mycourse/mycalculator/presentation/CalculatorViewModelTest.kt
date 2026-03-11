package com.mycourse.mycalculator.presentation

import app.cash.turbine.test
import com.mycourse.mycalculator.domain.operation.Divide
import com.mycourse.mycalculator.domain.operation.Plus
import com.mycourse.mycalculator.domain.repository.CalculatorRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CalculatorViewModelTest {
    // Mock repository agar ViewModel bisa ditest secara terisolasi
    private val repository: CalculatorRepository = mockk()
    private lateinit var viewModel: CalculatorViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = CalculatorViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ─── Initial State ──────────────────────────────────────────────────

    @Test
    fun `initial state should have default values`() = runTest {
        val state = viewModel.uiState.value

        assertEquals("0", state.firstNumber)
        assertEquals("0", state.secondNumber)
        assertNull(state.selectedOperation)
        assertEquals("0", state.result)
        assertNull(state.errorMessage)
        assertEquals(true, state.isEditingFirstNumber)
    }

    // ─── onNumberInput ──────────────────────────────────────────────────

    @Test
    fun `onNumberInput while editing first number should update firstNumber`() = runTest {
        viewModel.onNumberInput("5")

        assertEquals("5", viewModel.uiState.value.firstNumber)
    }

    @Test
    fun `onNumberInput multiple digits should concatenate correctly`() = runTest {
        viewModel.onNumberInput("1")
        viewModel.onNumberInput("2")
        viewModel.onNumberInput("3")

        assertEquals("123", viewModel.uiState.value.firstNumber)
    }

    @Test
    fun `onNumberInput after operation selected should update firstNumber`() = runTest {
        viewModel.onOperationSelected(Plus())
        viewModel.onNumberInput("7")

        assertEquals("7", viewModel.uiState.value.firstNumber)
    }

    @Test
    fun `onNumberInput when current value is zero should replace with digit`() = runTest {
        viewModel.onNumberInput("5")

        // "0" diganti menjadi "5", bukan "05"
        assertEquals("5", viewModel.uiState.value.firstNumber)
    }

    // ─── onDecimalInput ─────────────────────────────────────────────────

    @Test
    fun `onDecimalInput should append decimal point to firstNumber`() = runTest {
        viewModel.onNumberInput("3")
        viewModel.onDecimalInput()
        viewModel.onNumberInput("5")

        assertEquals("3.5", viewModel.uiState.value.firstNumber)
    }

    @Test
    fun `onDecimalInput should not append second decimal point`() = runTest {
        viewModel.onNumberInput("3")
        viewModel.onDecimalInput()
        viewModel.onDecimalInput() // duplikat, harus diabaikan

        assertEquals("3.", viewModel.uiState.value.firstNumber)
    }

    // ─── onOperationSelected ────────────────────────────────────────────

    @Test
    fun `onOperationSelected should update selectedOperation`() = runTest {
        viewModel.onNumberInput("6")
        val plus = Plus()
        viewModel.onOperationSelected(plus)

        assertEquals(plus, viewModel.uiState.value.selectedOperation)
    }

    @Test
    fun `onOperationSelected should switch editing to second number`() = runTest {
        viewModel.onNumberInput("7")
        viewModel.onOperationSelected(Plus())

        assertEquals(false, viewModel.uiState.value.isEditingFirstNumber)
    }

    @Test
    fun `onOperationSelected should clear error message`() = runTest {
        // Simulasikan error terlebih dahulu dengan divide by zero
        every { repository.calculate(any(), any(), any()) } throws ArithmeticException("Cannot divide by zero")
        viewModel.onOperationSelected(Divide())
        viewModel.onNumberInput("0")
        viewModel.onCalculate()

        // Pilih operasi baru, error harus hilang
        viewModel.onOperationSelected(Plus())
        assertNull(viewModel.uiState.value.errorMessage)
    }

    // ─── onCalculate ────────────────────────────────────────────────────

    @Test
    fun `onCalculate should call repository calculate with correct params`() = runTest {
        val plus = Plus()
        every { repository.calculate(3.0, 2.0, plus) } returns 5.0

        viewModel.onNumberInput("3")
        viewModel.onOperationSelected(plus)
        viewModel.onNumberInput("2")
        viewModel.onCalculate()

        verify { repository.calculate(3.0, 2.0, plus) }
    }

    @Test
    fun `onCalculate should update result state with formatted value`() = runTest {
        val plus = Plus()
        every { repository.calculate(3.0, 2.0, plus) } returns 5.0

        viewModel.onNumberInput("3")
        viewModel.onOperationSelected(plus)
        viewModel.onNumberInput("2")
        viewModel.onCalculate()

        assertEquals("5", viewModel.uiState.value.result)
    }

    @Test
    fun `onCalculate with decimal result should format correctly`() = runTest {
        val divide = Divide()
        every { repository.calculate(1.0, 3.0, divide) } returns 0.3333333333333333

        viewModel.onNumberInput("1")
        viewModel.onOperationSelected(divide)
        viewModel.onNumberInput("3")
        viewModel.onCalculate()

        // Hasil tidak boleh tampil sebagai "0.3333333333333333" mentah
        val result = viewModel.uiState.value.result
        assertEquals(true, result.startsWith("0.333"))
    }

    @Test
    fun `onCalculate divide by zero should set errorMessage`() = runTest {
        val divide = Divide()
        every { repository.calculate(5.0, 0.0, divide) } throws ArithmeticException("Cannot divide by zero")

        viewModel.onNumberInput("5")
        viewModel.onOperationSelected(divide)
        viewModel.onNumberInput("0")
        viewModel.onCalculate()

        assertEquals("Cannot divide by zero", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `onCalculate without selected operation should not update result`() = runTest {
        viewModel.onNumberInput("5")
        viewModel.onCalculate() // tidak ada operasi dipilih

        // Result tetap default
        assertEquals("0", viewModel.uiState.value.result)
    }

    // ─── onClear ────────────────────────────────────────────────────────

    @Test
    fun `onClear should reset state to default`() = runTest {
        val plus = Plus()
        every { repository.calculate(any(), any(), any()) } returns 5.0

        viewModel.onNumberInput("3")
        viewModel.onOperationSelected(plus)
        viewModel.onNumberInput("2")
        viewModel.onCalculate()
        viewModel.onClear()

        val state = viewModel.uiState.value
        assertEquals("0", state.firstNumber)
        assertEquals("0", state.secondNumber)
        assertNull(state.selectedOperation)
        assertEquals("0", state.result)
        assertNull(state.errorMessage)
        assertEquals(true, state.isEditingFirstNumber)
    }

    // ─── onBackspace ────────────────────────────────────────────────────

    @Test
    fun `onBackspace should remove last digit from firstNumber`() = runTest {
        viewModel.onNumberInput("1")
        viewModel.onNumberInput("2")
        viewModel.onNumberInput("3")
        viewModel.onBackspace()

        assertEquals("12", viewModel.uiState.value.firstNumber)
    }

    @Test
    fun `onBackspace on single digit should reset to zero`() = runTest {
        viewModel.onNumberInput("5")
        viewModel.onBackspace()

        assertEquals("0", viewModel.uiState.value.firstNumber)
    }

    @Test
    fun `onBackspace while editing second number should affect secondNumber`() = runTest {
        viewModel.onNumberInput("9")
        viewModel.onOperationSelected(Plus())
        viewModel.onNumberInput("8")
        viewModel.onBackspace()

        assertEquals("0", viewModel.uiState.value.secondNumber)
    }

    // ─── State stream (Turbine) ─────────────────────────────────────────

    @Test
    fun `uiState should emit correct sequence of states`() = runTest {
        val plus = Plus()
        every { repository.calculate(2.0, 3.0, plus) } returns 5.0

        viewModel.uiState.test {
            // initial
            awaitItem().also { assertEquals("0", it.firstNumber) }

            viewModel.onNumberInput("2")
            awaitItem().also { assertEquals("2", it.firstNumber) }

            viewModel.onOperationSelected(plus)
            awaitItem().also { assertEquals(plus, it.selectedOperation) }

            viewModel.onNumberInput("3")
            awaitItem().also { assertEquals("3", it.secondNumber) }

            viewModel.onCalculate()
            awaitItem().also { assertEquals("5", it.result) }

            cancelAndIgnoreRemainingEvents()
        }
    }

}