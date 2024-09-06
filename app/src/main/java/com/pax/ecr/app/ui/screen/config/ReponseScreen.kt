package com.pax.ecr.app.ui.screen.config

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResponseScreen(
    response: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(modifier.fillMaxSize().padding(24.dp)) {
        Box(modifier = Modifier.weight(.7f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = response)
        }
        Box(modifier = Modifier.weight(.3f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Button(onClick = onClick) {
                Text(text = "OK")
            }
        }
    }
}
