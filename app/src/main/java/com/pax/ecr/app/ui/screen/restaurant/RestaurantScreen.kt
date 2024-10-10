package com.pax.ecr.app.ui.screen.restaurant

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pax.ecr.app.R
import com.pax.ecr.app.receiptElements
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val items =
    mutableStateListOf(
        FoodItem("PAX Margarita", "Simple. Elegant. Timeless. Tomato and mozzarella.", BigDecimal(119), R.drawable.margarita),
        FoodItem("PosPay Pepperoni", "The original with spicy pepperoni.", BigDecimal(139), R.drawable.pepperoni),
        FoodItem("VAS Parma", "Ham and cheese with a great Mediterranean feeling.", BigDecimal(149), R.drawable.parma),
        FoodItem("Raymond's Favourite", "If you're feeling like a boss, eat like a boss.", BigDecimal(159), R.drawable.meat),
        FoodItem("Golden Days", "24K Golden Duck. Cheesy in more ways than one.", BigDecimal(399), R.drawable.golden),
    )

private var price by mutableStateOf(BigDecimal.ZERO)
private var selected by mutableIntStateOf(0)

@Composable
fun RestaurantScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onCheckout: (BigDecimal) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize().background(Color.DarkGray)) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Banner(onBack)
            Spacer(Modifier.height(30.dp))
            ItemList()
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 15.dp)) {
            Checkout(onCheckout)
        }
    }
}

private data class FoodItem(
    val title: String,
    val description: String,
    val price: BigDecimal,
    @DrawableRes val image: Int,
    val amountSelected: MutableIntState = mutableIntStateOf(0),
)

@Composable
private fun Banner(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(270.dp)) {
        Image(
            painter = painterResource(R.drawable.pizza),
            contentDescription = "Restaurant banner image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.align(Alignment.TopCenter).height(175.dp),
        )
        Box(
            modifier =
                Modifier.align(Alignment.TopStart).padding(start = 10.dp, top = 10.dp).size(40.dp)
                    .clip(CircleShape)
                    .background(Color(114, 157, 225)),
            contentAlignment = Alignment.Center,
        ) {
            IconButton(
                onClick = onBack,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }
        Box(
            modifier =
                Modifier.width(100.dp).height(160.dp).padding(bottom = 30.dp)
                    .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier.width(100.dp).height(100.dp)
                        .clip(shape = RoundedCornerShape(5.dp)).background(color = Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.swedbankpay_logo_no_text),
                    contentDescription = "Restaurant logo",
                    modifier = Modifier.height(80.dp).width(80.dp),
                )
            }
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            Text("Swedbank Pay Pizzeria", fontSize = 28.sp, fontWeight = FontWeight.Medium, color = Color.White)
        }
    }
}

@Composable
private fun ItemList() {
    LazyColumn {
        items(items, key = { it.title }) {
            Row(
                modifier =
                    Modifier.fillMaxWidth().height(110.dp).padding(vertical = 5.dp, horizontal = 12.dp).clickable {
                        it.amountSelected.intValue++
                        selected++
                        price += it.price
                        receiptElements[it.title] = receiptElements.getOrDefault(it.title, 0) + 1
                    },
            ) {
                Column(modifier = Modifier.weight(2f)) {
                    Row {
                        if (it.amountSelected.intValue > 0) {
                            Text(
                                "${it.amountSelected.intValue}x ",
                                color = Color(114, 157, 225),
                                fontSize = 16.sp,
                            )
                        }
                        Text(it.title, color = Color.White, fontSize = 16.sp)
                    }
                    Text(it.description, color = Color.LightGray)
                    Text(
                        formatAmountInNOK(it.price),
                        color = Color(114, 157, 225),
                    )
                }
                Image(
                    painter = painterResource(id = it.image),
                    contentDescription = it.title,
                    modifier =
                        Modifier.weight(
                            1f,
                        ).clip(shape = RoundedCornerShape(5.dp)).background(Color.White.copy(alpha = .85f)).height(100.dp),
                    contentScale = ContentScale.Crop,
                )
            }
            HorizontalDivider(color = Color.Gray, thickness = 2.dp)
        }
        item(key = "spacer-at-end") {
            Spacer(Modifier.height(100.dp))
        }
    }
}

private fun formatAmountInNOK(amount: BigDecimal): String {
    val symbols =
        DecimalFormatSymbols(Locale("no", "NO")).apply {
            currencySymbol = "" // Remove the currency symbol to append it manually
        }
    val decimalFormat = DecimalFormat("#,##0.00", symbols)
    val formattedAmount = decimalFormat.format(amount)
    return "$formattedAmount kr"
}

@Composable
private fun Checkout(onPaymentSelect: (BigDecimal) -> Unit) {
    Row(
        modifier =
            Modifier.fillMaxWidth(.9f).height(60.dp).clip(
                RoundedCornerShape(5.dp),
            ).background(Color(114, 157, 225)).clickable {
                onPaymentSelect(price)
                price = BigDecimal.ZERO
                selected = 0
                items.forEach {
                    it.amountSelected.intValue = 0
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier.clip(CircleShape).background(
                        Color.DarkGray,
                    ).size(35.dp),
            ) {
                Text(selected.toString(), color = Color(114, 157, 225))
            }
            Text("Click here to order", fontWeight = FontWeight.Medium)
        }
        Text(formatAmountInNOK(price), modifier = Modifier.padding(10.dp))
    }
}
