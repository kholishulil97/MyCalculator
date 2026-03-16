package com.mycourse.mycalculator

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.mycourse.mycalculator.presentation.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CalculatorScreenTest {
    // Rule 1: Hilt harus diinisialisasi sebelum Compose rule
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    // Rule 2: Compose test rule — gunakan Activity asli agar Hilt bekerja
    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    // ─── Display ───────────────────────────────────────────────────────

    @Test
    fun calculatorScreen_initialState_displaysZero() {
        composeRule
            .onNodeWithTag("display_first_number")
            .assertTextEquals("0")

        composeRule
            .onNodeWithTag("display_result")
            .assertTextEquals("0")
    }

    // ─── Number Input ──────────────────────────────────────────────────

    @Test
    fun calculatorScreen_clickNumber_updatesFirstNumber() {
        composeRule.onNodeWithTag("btn_5").performClick()

        composeRule
            .onNodeWithTag("display_first_number")
            .assertTextEquals("5")
    }

    @Test
    fun calculatorScreen_clickMultipleNumbers_concatenatesCorrectly() {
        composeRule.onNodeWithTag("btn_1").performClick()
        composeRule.onNodeWithTag("btn_2").performClick()
        composeRule.onNodeWithTag("btn_3").performClick()

        composeRule
            .onNodeWithTag("display_first_number")
            .assertTextEquals("123")
    }

    // ─── Operation ─────────────────────────────────────────────────────

    @Test
    fun calculatorScreen_clickPlus_displaysOperatorSymbol() {
        composeRule.onNodeWithTag("btn_2").performClick()
        composeRule.onNodeWithTag("btn_plus").performClick()

        composeRule
            .onNodeWithTag("display_operator")
            .assertTextEquals("+")
    }

    @Test
    fun calculatorScreen_clickPlus_switchesToSecondNumberInput() {
        composeRule.onNodeWithTag("btn_3").performClick()
        composeRule.onNodeWithTag("btn_plus").performClick()
        composeRule.onNodeWithTag("btn_4").performClick()

        composeRule
            .onNodeWithTag("display_second_number")
            .assertTextEquals("4")
    }

    // ─── Calculate ─────────────────────────────────────────────────────

    @Test
    fun calculatorScreen_addTwoNumbers_displaysCorrectResult() {
        composeRule.onNodeWithTag("btn_3").performClick()
        composeRule.onNodeWithTag("btn_plus").performClick()
        composeRule.onNodeWithTag("btn_4").performClick()
        composeRule.onNodeWithTag("btn_equals").performClick()

        composeRule
            .onNodeWithTag("display_result")
            .assertTextEquals("7")
    }

    @Test
    fun calculatorScreen_divideByZero_displaysErrorMessage() {
        composeRule.onNodeWithTag("btn_5").performClick()
        composeRule.onNodeWithTag("btn_divide").performClick()
        composeRule.onNodeWithTag("btn_0").performClick()
        composeRule.onNodeWithTag("btn_equals").performClick()

        composeRule
            .onNodeWithTag("display_error")
            .assertIsDisplayed()
            .assertTextEquals("Cannot divide by zero")
    }

    // ─── Clear & Backspace ─────────────────────────────────────────────

    @Test
    fun calculatorScreen_clickClear_resetsToInitialState() {
        composeRule.onNodeWithTag("btn_9").performClick()
        composeRule.onNodeWithTag("btn_clear").performClick()

        composeRule
            .onNodeWithTag("display_first_number")
            .assertTextEquals("0")
    }

    @Test
    fun calculatorScreen_clickBackspace_removesLastDigit() {
        composeRule.onNodeWithTag("btn_1").performClick()
        composeRule.onNodeWithTag("btn_2").performClick()
        composeRule.onNodeWithTag("btn_backspace").performClick()

        composeRule
            .onNodeWithTag("display_first_number")
            .assertTextEquals("1")
    }
}