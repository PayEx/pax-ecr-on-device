package com.pax.ecr.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PrintReceiptScreen(
    onPrintReceipt: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color(0xFF2C2C2C))
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Transaction Complete",
            style =
                MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                ),
            modifier = Modifier.padding(bottom = 32.dp),
        )
        Button(
            onClick = { onPrintReceipt() },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEE7600),
                    contentColor = Color.White,
                ),
            shape = RoundedCornerShape(12.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(bottom = 16.dp),
        ) {
            Text(text = "Print Receipt", fontSize = 20.sp)
        }
        Button(
            onClick = { onContinue() },
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White,
                ),
            shape = RoundedCornerShape(12.dp),
            modifier =
                Modifier.fillMaxWidth()
                    .height(65.dp),
        ) {
            Text(text = "Continue", fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrintReceiptScreenPreview() {
    PrintReceiptScreen(
        onPrintReceipt = { },
        onContinue = { },
    )
}
