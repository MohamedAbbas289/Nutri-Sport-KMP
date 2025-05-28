package com.nutrisport.profile.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nutrisport.shared.Alpha
import com.nutrisport.shared.BorderError
import com.nutrisport.shared.BorderIdle
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary

@Composable
fun CustomTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String? = null,
    enabled: Boolean = true,
    error: Boolean = false,
    expanded: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text
    )
) {
    val borderColor by animateColorAsState(
        targetValue = if (error) BorderError else BorderIdle
    )

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(6.dp)
            ),
        shape = RoundedCornerShape(6.dp),
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        placeholder = {
            placeHolder?.let {
                Text(
                    modifier = modifier.alpha(Alpha.HALF),
                    text = placeHolder,
                    fontSize = FontSize.REGULAR
                )
            }
        },
        keyboardOptions = keyboardOptions,
        singleLine = !expanded,
        colors = TextFieldDefaults.textFieldColors(
            textColor = TextPrimary,
            disabledTextColor = TextPrimary.copy(alpha = Alpha.DISABLED),
            backgroundColor = SurfaceLighter
        )
    )
}