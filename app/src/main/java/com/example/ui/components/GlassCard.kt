package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
  modifier: Modifier = Modifier,
  cornerRadius: Dp = 20.dp,
  backgroundColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.65f),
  borderColor: Color = Color.White.copy(alpha = 0.12f),
  elevation: Dp = 4.dp,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit
) {
  val shape = RoundedCornerShape(cornerRadius)
  val baseModifier = modifier
    .shadow(elevation, shape, clip = false)
    .clip(shape)
    .background(
      brush = Brush.verticalGradient(
        colors = listOf(
          backgroundColor,
          backgroundColor.copy(alpha = backgroundColor.alpha * 0.7f)
        )
      )
    )
    .border(width = 1.dp, brush = Brush.verticalGradient(listOf(borderColor, borderColor.copy(alpha = 0.03f))), shape = shape)

  val finalModifier = if (onClick != null) {
    baseModifier.clickable(onClick = onClick)
  } else {
    baseModifier
  }

  Box(modifier = finalModifier, content = content)
}
