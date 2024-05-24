package com.pax.ecr.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pax.ecr.app.Config

@Composable
fun ConfigScreen(
    config: Config,
    close: () -> Unit,
    modifier: Modifier = Modifier,
    onConfigChange: (Config) -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(.7f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            TextField(value = config.poiId.orEmpty(), singleLine = true, onValueChange = {
                onConfigChange(
                    config.copy(poiId = it),
                )
            }, label = {
                Text(text = "POI ID")
            })
            TextField(value = config.currencyCode.orEmpty(), singleLine = true, onValueChange = {
                onConfigChange(config.copy(currencyCode = it.uppercase()))
            }, label = {
                Text(text = "Currency code")
            })
            TextField(value = config.saleId.orEmpty(), singleLine = true, onValueChange = {
                onConfigChange(config.copy(saleId = it))
            }, label = {
                Text(text = "Sale ID")
            })
        }
        Box(modifier = Modifier.weight(.3f)) {
            Button(onClick = close, enabled = config.isValid()) {
                Text(text = "OK")
            }
        }
    }
}
