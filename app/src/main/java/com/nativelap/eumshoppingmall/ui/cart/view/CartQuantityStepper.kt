package com.nativelap.eumshoppingmall.ui.cart.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nativelap.eumshoppingmall.R
import com.nativelap.eumshoppingmall.ui.theme.EumRadius
import com.nativelap.eumshoppingmall.ui.theme.eumColors

@Composable
fun CartQuantityStepper(
    quantity: Int,
    onQuantityChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val safeQuantity = quantity.coerceAtLeast(1)
    Surface(
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(EumRadius.x8),
        color = if (enabled) MaterialTheme.eumColors.surface else MaterialTheme.eumColors.surfaceSecondary,
        border = BorderStroke(1.dp, MaterialTheme.eumColors.lineControl),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onQuantityChanged((safeQuantity - 1).coerceAtLeast(1)) },
                enabled = enabled && safeQuantity > 1,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    painterResource(R.drawable.ic_minus),
                    contentDescription = stringResource(R.string.catalog_quantity_decrease),
                    modifier = Modifier.size(16.dp),
                )
            }
            Text(safeQuantity.toString(), style = MaterialTheme.typography.labelMedium)
            IconButton(
                onClick = { onQuantityChanged(if (safeQuantity < Int.MAX_VALUE) safeQuantity + 1 else Int.MAX_VALUE) },
                enabled = enabled && safeQuantity < Int.MAX_VALUE,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    painterResource(R.drawable.ic_plus),
                    contentDescription = stringResource(R.string.catalog_quantity_increase),
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}
