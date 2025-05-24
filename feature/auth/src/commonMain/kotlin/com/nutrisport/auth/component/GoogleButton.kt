package com.nutrisport.auth.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.Gray
import com.nutrisport.shared.GrayDarker
import com.nutrisport.shared.IconSecondary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.TextPrimary
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    primaryText: String = "Sign in With Google",
    secondaryText: String = "Please Wait...",
    icon: DrawableResource = Resources.Image.GoogleLogo,
    shape: Shape = RoundedCornerShape(99.dp),
    backgroundColor: Color = Gray,
    borderColor: Color = GrayDarker,
    progressIndicatorColor: Color = IconSecondary,
    onClick: () -> Unit
) {
    var buttonText by remember { mutableStateOf(primaryText) }

    LaunchedEffect(loading) {
        buttonText = if (loading) secondaryText else primaryText
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = shape
            )
            .clickable(enabled = !loading) { onClick() },
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(all = 20.dp)
                .animateContentSize(
                    animationSpec = tween(durationMillis = 200)
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = !loading) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Google Logo",
                    tint = Color.Unspecified
                )
            }
            AnimatedVisibility(visible = loading) {
                CircularProgressIndicator(
                    modifier = modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
            Spacer(modifier = modifier.width(12.dp))
            Text(
                text = buttonText,
                color = TextPrimary,
                fontSize = FontSize.REGULAR
            )

        }

    }
}