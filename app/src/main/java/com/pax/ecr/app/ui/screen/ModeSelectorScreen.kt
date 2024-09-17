package com.pax.ecr.app.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pax.ecr.app.Mode
import com.pax.ecr.app.R

@Composable
fun ModeSelectorScreen(
    modifier: Modifier = Modifier,
    modeSelector: (Mode) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        ModeButton(
            label = "Restaurant",
            image = R.drawable.pizza,
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) { modeSelector(Mode.RESTAURANT) }

        ButtonSpacer()

        ModeButton(
            label = "Retail",
            image = R.drawable.retail,
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) { modeSelector(Mode.RETAIL) }

        ButtonSpacer()

        ModeButton(
            label = "Payment Application",
            image = R.drawable.pax,
            modifier = Modifier.fillMaxWidth().weight(1f),
        ) { modeSelector(Mode.PAYMENT_APPLICATION) }
    }
}

@Composable
private fun ButtonSpacer() =
    Spacer(
        modifier =
            Modifier
                .height(4.dp)
                .fillMaxWidth()
                .background(Color.Black),
    )

@Composable
private fun ModeButton(
    label: String,
    @DrawableRes image: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier.clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Mode selector background",
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier.background(color = Color.White.copy(alpha = .75f)).fillMaxWidth().padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = label, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
