package com.pax.ecr.app.ui.screen.retail

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.ui.text.style.TextOverflow
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
        Product("SWP T-Shirt", "Comfy cotton", BigDecimal(189), R.drawable.swedbank_tshirt),
        Product("PayEx T-Shirt", "Stylish modern in several sized", BigDecimal(199), R.drawable.payex_tshirt),
        Product("SWP Hoodie", "Warm with style", BigDecimal(299), R.drawable.swedbank_hoodie),
        Product("PayEx Sweatshirt", "Warm and light", BigDecimal(259), R.drawable.payex_sweat),
    )

private var price by mutableStateOf(BigDecimal.ZERO)
private var selected by mutableIntStateOf(0)

private data class Product(
    val title: String,
    val description: String,
    val price: BigDecimal,
    @DrawableRes val image: Int,
    val amountSelected: MutableIntState = mutableIntStateOf(0),
)

@Composable
fun RetailerScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onCheckout: (BigDecimal) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize().background(Color.White)) {
        Header(onBack)
        Column(modifier = Modifier.align(Alignment.TopCenter).padding(top = 60.dp)) {
            ItemList()
        }
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            Checkout(onCheckout)
        }
    }
}

@Composable
private fun ItemList() {
    LazyVerticalGrid(
        modifier = Modifier.padding(15.dp),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(items, { it.title }) {
            Column(
                modifier =
                    Modifier.height(250.dp).clickable {
                        it.amountSelected.intValue++
                        selected++
                        price += it.price
                        receiptElements[it.title] = receiptElements.getOrDefault(it.title, 0) + 1
                    },
            ) {
                Image(
                    painter = painterResource(id = it.image),
                    contentDescription = it.title,
                    modifier = Modifier.background(Color.LightGray).height(180.dp).fillMaxWidth(),
                    contentScale = ContentScale.FillHeight,
                )
                Row {
                    if (it.amountSelected.intValue > 0) Text("${it.amountSelected.intValue}x ", fontSize = 16.sp)
                    Text(it.title, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
                Text(it.description, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(formatAmountInNOK(it.price))
            }
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
private fun Checkout(onCheckout: (BigDecimal) -> Unit) {
    Row(
        modifier =
            Modifier.fillMaxWidth().height(60.dp).background(Color.LightGray).clickable {
                onCheckout(price)
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
                    ).width(35.dp).height(35.dp),
            ) {
                Text(selected.toString(), color = Color.LightGray)
            }
            Text("Click here to order", fontWeight = FontWeight.Medium)
        }
        Text(formatAmountInNOK(price), modifier = Modifier.padding(10.dp))
    }
}

@Composable
private fun Header(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth().height(60.dp),
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back",
            )
        }
        Text(
            text = "PayEx Wear & Gear",
            modifier = Modifier.align(Alignment.Center),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}
