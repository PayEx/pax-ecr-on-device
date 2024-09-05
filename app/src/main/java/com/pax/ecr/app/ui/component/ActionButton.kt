package com.pax.ecr.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(
    label: String,
    modifier: Modifier = Modifier,
    buttonIcon: ImageVector? = null,
    fullSize: Boolean = false,
    iconSize: Dp = 32.dp,
    labelStyle: TextStyle = MaterialTheme.typography.titleMedium,
    labelAlign: TextAlign = TextAlign.Left,
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(8.dp),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    onClick: () -> Unit,
) = Box(modifier = modifier.width(if (fullSize) 312.dp else 130.dp)) {
    Column(
        modifier =
            modifier
                .height(130.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
    ) {
        if (buttonIcon != null) {
            Icon(
                imageVector = buttonIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(iconSize),
            )
        }
        Text(
            text = label,
            textAlign = labelAlign,
            style = labelStyle,
        )
    }
}
