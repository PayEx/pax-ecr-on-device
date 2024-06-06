package com.pax.ecr.app.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.math.BigDecimal

@Composable
fun PurchaseScreen(
    modifier: Modifier = Modifier,
    onPaymentSelected: (BigDecimal) -> Unit,
) {
    var pizzaSelected by remember { mutableStateOf(false) }
    var burgerSelected by remember { mutableStateOf(false) }
    var sodaSelected by remember { mutableStateOf(false) }
    var beerSelected by remember { mutableStateOf(false) }

    val pizzaPrice = BigDecimal(200)
    val burgerPrice = BigDecimal(180)
    val sodaPrice = BigDecimal(50)
    val beerPrice = BigDecimal(70)

    val totalPrice =
        remember(pizzaSelected, burgerSelected, sodaSelected, beerSelected) {
            listOfNotNull(
                pizzaSelected.takeIf { it }?.let { pizzaPrice },
                burgerSelected.takeIf { it }?.let { burgerPrice },
                sodaSelected.takeIf { it }?.let { sodaPrice },
                beerSelected.takeIf { it }?.let { beerPrice },
            ).sumOf { it }
        }

    Column(
        modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Item("Pizza", pizzaPrice, pizzaSelected) { pizzaSelected = !pizzaSelected }
                Item("Burger", burgerPrice, burgerSelected) { burgerSelected = !burgerSelected }
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Item("Brus", sodaPrice, sodaSelected) { sodaSelected = !sodaSelected }
                Item("Øl", beerPrice, beerSelected) { beerSelected = !beerSelected }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sum: $totalPrice,-",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.End),
            )
        }

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { onPaymentSelected(totalPrice) },
                modifier = Modifier.width(150.dp).height(50.dp),
                enabled = totalPrice > BigDecimal.ZERO,
            ) {
                Text(text = "Betal $totalPrice,-")
            }
        }
    }
}

@Composable
fun Item(
    name: String,
    price: BigDecimal,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) Color.Black else Color.Gray
    val checkmark = if (isSelected) "✓" else ""

    Box(
        modifier =
            modifier
                .size(150.dp)
                .border(1.dp, borderColor)
                .clickable { onClick() }
                .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$name $checkmark")
            Text(text = "$price,-")
        }
    }
}
