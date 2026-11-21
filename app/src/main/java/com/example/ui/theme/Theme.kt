package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = AccentOrange,
    secondary = CoralPink,
    background = DarkBase,
    surface = DarkSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextLight,
    onSurface = TextLight,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextMuted
  )

private val LightColorScheme =
  lightColorScheme(
    primary = AccentOrange,
    secondary = CoralPink,
    background = LightBase,
    surface = LightSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextDark,
    onSurface = TextDark,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = TextDarkMuted
  )

@Composable
fun MeuMusicTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MeuMusicTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
