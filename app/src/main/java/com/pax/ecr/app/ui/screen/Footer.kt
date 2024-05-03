package com.pax.ecr.app.ui.screen

import SlideVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pax.ecr.app.R

@Composable
fun Footer(
    isOpen: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(44.dp),
    ) {
        SlideVertically(
            visible = true,
            delayMillis = 700,
            durationMillis = 500,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Box(modifier = Modifier.align(Alignment.CenterStart)) {
                    Column {
                        IconButton(onClick = onClick) {
                            Icon(
                                imageVector =
                                    if (isOpen) {
                                        Icons.Outlined.Close
                                    } else {
                                        Icons.Outlined.Menu
                                    },
                                contentDescription = "Footer menu",
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painterResource(R.drawable.swedbankpay_logo_no_text),
                        "Swedbank Pay logo without text",
                        modifier = Modifier.height(26.dp),
                    )
                }
            }
        }
    }
}
