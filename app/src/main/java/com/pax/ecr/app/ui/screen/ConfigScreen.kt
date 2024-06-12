package com.pax.ecr.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
            modifier =
                Modifier
                    .weight(.8f)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Configuration", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(10.dp))
            TextField(value = config.poiId.orEmpty(), singleLine = true, supportingText = {
                Text(text = "Changing the POI-ID will require re-enabling ECR on Device on the payment application")
            }, onValueChange = {
                onConfigChange(
                    config.copy(poiId = it),
                )
            }, label = {
                Text(text = "POI ID")
            })
            TextField(value = config.currencyCode.orEmpty(), singleLine = true, supportingText = {
                Text(text = "In ISO-4217 code format")
            }, onValueChange = {
                onConfigChange(config.copy(currencyCode = it.uppercase().filter { char -> char.isLetter() }))
            }, label = {
                Text(text = "Currency code")
            })
            TextField(value = config.saleId.orEmpty(), singleLine = true, supportingText = {
                Text(text = "No need to change this unless you know what you're doing")
            }, onValueChange = {
                onConfigChange(config.copy(saleId = it))
            }, label = {
                Text(text = "Sale ID")
            })
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Show responses explicit")
                Switch(
                    checked = config.responseScreenEnabled,
                    onCheckedChange = { onConfigChange(config.copy(responseScreenEnabled = it)) },
                )
            }
        }
        Box(modifier = Modifier.weight(.2f)) {
            Button(onClick = close, enabled = config.isValid()) {
                Text(text = "OK", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
