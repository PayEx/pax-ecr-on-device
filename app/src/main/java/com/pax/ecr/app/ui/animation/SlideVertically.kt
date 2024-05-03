import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable

@Composable
fun SlideVertically(
    visible: Boolean = false,
    initialOffsetY: Int? = null,
    targetOffsetY: Int? = null,
    delayMillis: Int = 0,
    durationMillis: Int = 300,
    fade: Boolean = false,
    fadeDurationMillis: Int = 300,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        label = "Slide vertically Animation",
        enter =
            slideInVertically(
                animationSpec =
                    tween(
                        durationMillis = durationMillis,
                        delayMillis = delayMillis,
                    ),
                initialOffsetY = { initialOffsetY ?: it },
            ).plus(
                fadeIn(
                    initialAlpha = if (fade) 0f else 1f,
                    animationSpec =
                        tween(
                            durationMillis = fadeDurationMillis,
                        ),
                ),
            ),
        exit =
            slideOutVertically(
                animationSpec =
                    tween(
                        durationMillis = durationMillis,
                        delayMillis = delayMillis,
                    ),
                targetOffsetY = { targetOffsetY ?: it },
            ).plus(
                fadeOut(
                    targetAlpha = if (fade) 0f else 1f,
                    animationSpec =
                        tween(
                            durationMillis = fadeDurationMillis,
                        ),
                ),
            ),
    ) { content() }
}
