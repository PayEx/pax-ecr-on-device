package com.pax.ecr.app.ui.screen.payment

import SlideVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun TransactionSelectScreen(
    modifier: Modifier = Modifier,
    slideIn: Boolean = true,
    actionHandler: (Action) -> Unit,
) = Box(
    modifier =
        Modifier
            .fillMaxWidth()
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
    Column(modifier = modifier) {
        SlideVertically(
            visible = slideIn,
            delayMillis = 0,
            durationMillis = slideInDurationMillis - 700,
            initialOffsetY = 160,
            fade = true,
            fadeDurationMillis = 600,
        ) {
            Text(
                text = "Choose your transaction type",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        SlideVertically(
            visible = slideIn,
            delayMillis = buttonSlideInDelay,
            durationMillis = slideInDurationMillis - 600,
        ) {
            FlowRow(
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                ActionButton(
                    modifier = modifier.padding(bottom = animatedSpaceBetweenButtons.dp),
                    label = "Purchase",
                    onClick = { actionHandler(Action.PURCHASE) },
                    buttonIcon = ImageVector.vectorResource(R.drawable.purchase_icon),
                )
                ActionButton(
                    modifier = modifier.padding(bottom = animatedSpaceBetweenButtons.dp),
                    label = "Purchase with cashback",
                    onClick = { actionHandler(Action.PURCHASE_W_CASHBACK) },
                    buttonIcon = ImageVector.vectorResource(R.drawable.cash_withdraw_icon),
                )
                ActionButton(
                    modifier = modifier.padding(bottom = animatedSpaceBetweenButtons.dp),
                    label = "Refund",
                    onClick = { actionHandler(Action.REFUND) },
                    buttonIcon = ImageVector.vectorResource(R.drawable.refund_icon),
                )
                ActionButton(
                    modifier = modifier.padding(bottom = animatedSpaceBetweenButtons.dp),
                    label = "Reversal",
                    onClick = { actionHandler(Action.REVERSAL) },
                    buttonIcon = ImageVector.vectorResource(R.drawable.reversal_icon),
                )
            }
        }
        SlideVertically(
            visible = slideIn,
            delayMillis = 0,
            durationMillis = slideInDurationMillis - 700,
            initialOffsetY = 160,
            fade = true,
            fadeDurationMillis = 600,
        ) {
            Text(
                text = "Admin actions",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        SlideVertically(
            visible = slideIn,
            delayMillis = buttonSlideInDelay,
            durationMillis = slideInDurationMillis - 600,
        ) {
            FlowRow(
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                ActionButton(
                    modifier = modifier.padding(bottom = animatedSpaceBetweenButtons.dp),
                    label = "Login",
                    onClick = { actionHandler(Action.LOGIN) },
                    buttonIcon = ImageVector.vectorResource(R.drawable.login_icon),
                )
                ActionButton(
                    modifier = modifier.padding(bottom = animatedSpaceBetweenButtons.dp),
                    label = "Logout",
                    onClick = { actionHandler(Action.LOGOUT) },
                    buttonIcon = ImageVector.vectorResource(R.drawable.logout_icon),
                )
            }
        }
    }
}
