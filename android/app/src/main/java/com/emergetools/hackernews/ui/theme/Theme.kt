package com.emergetools.hackernews.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.emergetools.hackernews.themeStorage

private val LightColorScheme = lightColorScheme(
  primary = HackerOrange,
  primaryContainer = HackerOrangeLight,
  background = BackgroundLight,
  surface = SurfaceLight,
  surfaceContainer = SurfaceLight,
  onBackground = OnBackgroundLight,
  onSurface = OnSurfaceLight
)

private val DarkColorScheme = darkColorScheme(
  primary = HackerOrange,
  primaryContainer = HackerOrangeLight,
  background = BackgroundDark,
  surface = SurfaceDark,
  surfaceContainer = SurfaceDark,
  onBackground = OnBackgroundDark,
  onSurface = OnSurfaceDark
)


@Composable
fun HackerNewsTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val context = LocalContext.current
  val themeStorage = context.themeStorage()
  
  // Collect theme preferences
  val themePresetName by themeStorage.getThemePreset().collectAsState(initial = null)
  val customPrimaryColor by themeStorage.getCustomPrimaryColor().collectAsState(initial = null)
  val customBackgroundColor by themeStorage.getCustomBackgroundColor().collectAsState(initial = null)
  val customSurfaceColor by themeStorage.getCustomSurfaceColor().collectAsState(initial = null)
  
  // Determine which preset to use
  val themePreset = ThemePreset.fromString(themePresetName)
  
  // Get colors from preset or custom values
  val primaryColor = customPrimaryColor?.let { Color(it) } ?: themePreset.primaryColor
  val backgroundColor = if (darkTheme) {
    customBackgroundColor?.let { Color(it) } ?: themePreset.backgroundDark
  } else {
    customBackgroundColor?.let { Color(it) } ?: themePreset.backgroundLight
  }
  val surfaceColor = if (darkTheme) {
    customSurfaceColor?.let { Color(it) } ?: themePreset.surfaceDark
  } else {
    customSurfaceColor?.let { Color(it) } ?: themePreset.surfaceLight
  }
  
  val colorScheme = when {
    dynamicColor -> {
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme.copy(
      primary = primaryColor,
      background = backgroundColor,
      surface = surfaceColor,
      surfaceContainer = surfaceColor
    )
    else -> LightColorScheme.copy(
      primary = primaryColor,
      background = backgroundColor,
      surface = surfaceColor,
      surfaceContainer = surfaceColor
    )
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

