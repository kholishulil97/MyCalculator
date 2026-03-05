package com.mycourse.mycalculator.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mycourse.mycalculator.domain.model.CalculatorUiState

@Preview
@Composable
fun CalculatorScreen(
    modifier: Modifier = Modifier,
    vm: CalculatorViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1C1C1E))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalculatorDisplay(uiState = uiState)

        Spacer(modifier = Modifier.height(24.dp))

        CalculatorKeypad(
            onNumberClick = vm::onNumberInput
        )
    }
}

@Composable
fun CalculatorDisplay(
    uiState: CalculatorUiState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2E), RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TextView angka pertama
            Text(
                text = uiState.firstNumber,
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.width(12.dp))

            // TextView operator
            Text(
                text = "+",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9F0A),
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            //TextView angka kedua
            Text(
                text = "456",
                fontSize = 32.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                textAlign = TextAlign.End
            )
        }

        HorizontalDivider(
            color = Color(0xFF48484A),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // TextView hasil
        Text(
            text = "= 789",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun CalculatorKeypad(
    onNumberClick: (String) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Baris 1: C, ⌫, dan 2 operator pertama (÷, ×)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CalcButton(text = "C", color = Color(0xFFFF453A), modifier = Modifier.weight(1f))
            CalcButton(text = "⌫", color = Color(0xFFFF9F0A), modifier = Modifier.weight(1f))
            CalcButton(text = "÷", color = Color(0xFFFF9F0A), modifier = Modifier.weight(1f))
            CalcButton(text = "x", color = Color(0xFFFF9F0A), modifier = Modifier.weight(1f))
        }

        // Baris 2: 7, 8, 9, -
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("7", "8", "9").forEach { num ->
                CalcButton(text = num, modifier = Modifier.weight(1f), onClick = { onNumberClick(num) })
             }
            CalcButton(text = "-", color = Color(0xFFFF9F0A), modifier = Modifier.weight(1f))
        }

        // Baris 3: 4, 5, 6, +
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("4", "5", "6").forEach { num ->
                CalcButton(text = num, modifier = Modifier.weight(1f), onClick = { onNumberClick(num) })
            }
            CalcButton(text = "+", color = Color(0xFFFF9F0A), modifier = Modifier.weight(1f))
        }

        // Baris 4: 1, 2, 3, =
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            listOf("1", "2", "3").forEach { num ->
                CalcButton(text = num, modifier = Modifier.weight(1f), onClick = { onNumberClick(num) })
            }
            CalcButton(text = "=", color = Color(0xFF30D158), modifier = Modifier.weight(1f))
        }

        // Baris 5: 0 (lebar 2x), ., =
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CalcButton(text = "0", modifier = Modifier.weight(2f), onClick = { onNumberClick("0") })
            CalcButton(text = ".", color = Color(0xFFFF9F0A), modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun CalcButton(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF3A3A3C),
    textColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Light,
            color = textColor
        )
    }
}