package com.nutrisport.cart.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.nutrisport.shared.BorderIdle
import com.nutrisport.shared.FontSize
import com.nutrisport.shared.IconPrimary
import com.nutrisport.shared.Resources
import com.nutrisport.shared.RobotoCondensedFont
import com.nutrisport.shared.Surface
import com.nutrisport.shared.SurfaceLighter
import com.nutrisport.shared.TextPrimary
import com.nutrisport.shared.TextSecondary
import com.nutrisport.shared.component.QuantityCounter
import com.nutrisport.shared.domain.CartItem
import com.nutrisport.shared.domain.Product
import com.nutrisport.shared.domain.QuantityCounterSize
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    product: Product?,
    cartItem: CartItem,
    onPlusClick: (Int) -> Unit,
    onMinusClick: (Int) -> Unit,
    onDeleteClick: () -> Unit
) {
    val totalProductPrice = (product?.price?.times(cartItem.quantity))
    val roundedTotalPrice = (totalProductPrice?.times(100))?.roundToInt()?.div(100.0)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(size = 12.dp))
            .background(SurfaceLighter)
    ) {
        AsyncImage(
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(size = 12.dp))
                .border(
                    width = 1.dp,
                    color = BorderIdle,
                    shape = RoundedCornerShape(size = 12.dp)
                ),
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(product?.thumbnail)
                .crossfade(enable = true)
                .build(),
            contentDescription = "Product thumbnail image",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = product?.title ?: "",
                    fontFamily = RobotoCondensedFont(),
                    color = TextPrimary,
                    fontSize = FontSize.MEDIUM,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(24.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Surface)
                        .clickable { onDeleteClick() }
                        .border(
                            width = 1.dp,
                            color = BorderIdle,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(all = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(14.dp),
                        painter = painterResource(Resources.Icon.Delete),
                        contentDescription = "Delete icon",
                        tint = IconPrimary
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$${roundedTotalPrice}",
                    color = TextSecondary,
                    fontSize = FontSize.EXTRA_REGULAR,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                QuantityCounter(
                    size = QuantityCounterSize.Small,
                    value = cartItem.quantity,
                    onMinusClick = onMinusClick,
                    onPlusClick = onPlusClick,
                    onMinusDeleteClick = onDeleteClick
                )
            }
        }
    }
}