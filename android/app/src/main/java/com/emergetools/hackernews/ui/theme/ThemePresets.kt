package com.emergetools.hackernews.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Predefined theme presets for quick selection
 */
enum class ThemePreset(
  val displayName: String,
  val primaryColor: Color,
  val backgroundLight: Color,
  val backgroundDark: Color,
  val surfaceLight: Color,
  val surfaceDark: Color
) {
  DEFAULT(
    displayName = "Hacker Orange",
    primaryColor = HackerOrange,
    backgroundLight = BackgroundLight,
    backgroundDark = BackgroundDark,
    surfaceLight = SurfaceLight,
    surfaceDark = SurfaceDark
  ),
  BLUE(
    displayName = "Cool Blue",
    primaryColor = HackerBlue,
    backgroundLight = Color(0xFFF0F7FF),
    backgroundDark = Color(0xFF0A1929),
    surfaceLight = Color(0xFFE3F2FD),
    surfaceDark = Color(0xFF132F4C)
  ),
  PURPLE(
    displayName = "Purple Haze",
    primaryColor = HackerPurple,
    backgroundLight = Color(0xFFF8F4FF),
    backgroundDark = Color(0xFF1A0D29),
    surfaceLight = Color(0xFFF3E5F5),
    surfaceDark = Color(0xFF2A1A3D)
  ),
  GREEN(
    displayName = "Forest Green",
    primaryColor = HackerGreen,
    backgroundLight = Color(0xFFF1F8F4),
    backgroundDark = Color(0xFF0A1F14),
    surfaceLight = Color(0xFFE8F5E9),
    surfaceDark = Color(0xFF1B3326)
  ),
  RED(
    displayName = "Ruby Red",
    primaryColor = HackerRed,
    backgroundLight = Color(0xFFFFF5F5),
    backgroundDark = Color(0xFF2D0A0F),
    surfaceLight = Color(0xFFFFEBEE),
    surfaceDark = Color(0xFF4A1319)
  ),
  MIDNIGHT(
    displayName = "Midnight",
    primaryColor = Color(0xFF64B5F6),
    backgroundLight = Color(0xFFF5F5F5),
    backgroundDark = DeepSpaceBlue,
    surfaceLight = Color(0xFFEEEEEE),
    surfaceDark = MidnightBlue
  ),
  CUSTOM(
    displayName = "Custom",
    primaryColor = HackerOrange,
    backgroundLight = BackgroundLight,
    backgroundDark = BackgroundDark,
    surfaceLight = SurfaceLight,
    surfaceDark = SurfaceDark
  );

  companion object {
    fun fromString(name: String?): ThemePreset {
      return values().find { it.name == name } ?: DEFAULT
    }
  }
}

/**
 * Font size presets
 */
enum class FontSizePreset(
  val displayName: String,
  val scale: Float
) {
  SMALL("Small", 0.875f),
  MEDIUM("Medium", 1.0f),
  LARGE("Large", 1.125f),
  EXTRA_LARGE("Extra Large", 1.25f);

  companion object {
    fun fromScale(scale: Float): FontSizePreset {
      return values().minByOrNull { kotlin.math.abs(it.scale - scale) } ?: MEDIUM
    }
  }
}

/**
 * Corner radius presets
 */
enum class CornerRadiusPreset(
  val displayName: String,
  val radius: Float
) {
  BOXY("Boxy", 4f),
  SLIGHTLY_ROUNDED("Slightly Rounded", 8f),
  ROUNDED("Rounded", 12f),
  VERY_ROUNDED("Very Rounded", 20f);

  companion object {
    fun fromRadius(radius: Float): CornerRadiusPreset {
      return values().minByOrNull { kotlin.math.abs(it.radius - radius) } ?: ROUNDED
    }
  }
}
