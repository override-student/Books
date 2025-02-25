package org.book.ui.components.pags

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.swipe_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24
import org.jetbrains.compose.resources.painterResource

@Composable
fun Tutorial() {
    val density = LocalDensity.current
    val offsetDp = 10.dp
    val offsetPx = remember { with(density) { offsetDp.toPx() } }

    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffset = infiniteTransition.animateFloat(
        initialValue = -offsetPx,
        targetValue = offsetPx,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Atras",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(Res.drawable.swipe_24dp_E8EAED_FILL0_wght400_GRAD0_opsz24),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(70.dp)
                    .graphicsLayer {
                        translationX = animatedOffset.value
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Siguiente",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}