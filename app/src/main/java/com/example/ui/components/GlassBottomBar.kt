package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class Screen(val route: String, val title: String, val icon: ImageVector, val selectedIcon: ImageVector) {
  object Home : Screen("home", "Home", Icons.Outlined.Home, Icons.Filled.Home)
  object Search : Screen("search", "Search", Icons.Outlined.Search, Icons.Filled.Search)
  object Library : Screen("library", "Library", Icons.Outlined.LibraryMusic, Icons.Filled.LibraryMusic)
  object Settings : Screen("settings", "Settings", Icons.Outlined.Settings, Icons.Filled.Settings)
}

@Composable
fun GlassBottomBar(
  currentRoute: String,
  onItemSelected: (Screen) -> Unit
) {
  val items = listOf(Screen.Home, Screen.Search, Screen.Library, Screen.Settings)

  GlassCard(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .navigationBarsPadding(),
    cornerRadius = 28.dp,
    elevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(64.dp)
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      items.forEach { screen ->
        val selected = currentRoute == screen.route
        val backgroundColor by animateColorAsState(
          targetValue = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f) else Color.Transparent,
          animationSpec = spring(),
          label = "nav_bg"
        )
        val tintColor by animateColorAsState(
          targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
          animationSpec = spring(),
          label = "nav_tint"
        )

        Row(
          modifier = Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onItemSelected(screen) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = if (selected) screen.selectedIcon else screen.icon,
            contentDescription = screen.title,
            tint = tintColor
          )
          AnimatedVisibility(
            visible = selected,
            enter = fadeIn(),
            exit = fadeOut()
          ) {
            Text(
              text = screen.title,
              color = tintColor,
              style = MaterialTheme.typography.labelSmall,
              fontSize = 13.sp
            )
          }
        }
      }
    }
  }
}
