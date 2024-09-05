package com.pax.ecr.app.ui.screen

import SlideVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pax.ecr.app.Action
import com.pax.ecr.app.R
import com.pax.ecr.app.ui.component.ActionButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    slideIn: Boolean = true,
    actionHandler: (Action) -> Unit,
) = Box(
    modifier =
        Modifier
            .fillMaxSize()
            .padding(top = 36.dp, start = 24.dp, end = 24.dp),
) {
    val slideInDurationMillis = 1500
    val buttonSlideInDelay = 300
    val animatedSpaceBetweenButtons by animateFloatAsState(
        targetValue = if (slideIn) 8F else 60F,
        animationSpec =
            tween(
                delayMillis = buttonSlideInDelay + 200,
                durationMillis = slideInDurationMillis - 850,
            ),
        label = "Space between buttons",
    )
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SlideVertically(
            visible = slideIn,
            delayMillis = 0,
            durationMillis = slideInDurationMillis - 700,
            initialOffsetY = 160,
            fade = true,
            fadeDurationMillis = 600,
        ) {
            Text(
                text = "Welcome! Login in to get started!",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        SlideVertically(
            visible = slideIn,
            delayMillis = buttonSlideInDelay,
            durationMillis = slideInDurationMillis - 600,
        ) {
            ActionButton(
                modifier = modifier.padding(bottom = animatedSpaceBetweenButtons.dp),
                label = "Login",
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                iconSize = 64.dp,
                onClick = { actionHandler(Action.LOGIN) },
                buttonIcon = ImageVector.vectorResource(R.drawable.login_icon),
            )
        }
    }
}
