package com.emergetools.hackernews.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.emergetools.hackernews.themeDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Storage for theme preferences including preset themes and custom colors
 */
class ThemeStorage(private val appContext: Context) {
  
  private val themePresetKey = stringPreferencesKey("theme_preset")
  private val customPrimaryColorKey = intPreferencesKey("custom_primary_color")
  private val customBackgroundColorKey = intPreferencesKey("custom_background_color")
  private val customSurfaceColorKey = intPreferencesKey("custom_surface_color")
  private val fontScaleKey = floatPreferencesKey("font_scale")
  private val cornerRadiusKey = floatPreferencesKey("corner_radius")

  suspend fun saveThemePreset(preset: String) {
    appContext.themeDataStore.edit { prefs ->
      prefs[themePresetKey] = preset
    }
  }

  suspend fun saveCustomColors(primary: Int, background: Int, surface: Int) {
    appContext.themeDataStore.edit { prefs ->
      prefs[customPrimaryColorKey] = primary
      prefs[customBackgroundColorKey] = background
      prefs[customSurfaceColorKey] = surface
    }
  }

  suspend fun saveFontScale(scale: Float) {
    appContext.themeDataStore.edit { prefs ->
      prefs[fontScaleKey] = scale
    }
  }

  suspend fun saveCornerRadius(radius: Float) {
    appContext.themeDataStore.edit { prefs ->
      prefs[cornerRadiusKey] = radius
    }
  }

  fun getThemePreset(): Flow<String?> {
    return appContext.themeDataStore.data.map { it[themePresetKey] }
  }

  fun getCustomPrimaryColor(): Flow<Int?> {
    return appContext.themeDataStore.data.map { it[customPrimaryColorKey] }
  }

  fun getCustomBackgroundColor(): Flow<Int?> {
    return appContext.themeDataStore.data.map { it[customBackgroundColorKey] }
  }

  fun getCustomSurfaceColor(): Flow<Int?> {
    return appContext.themeDataStore.data.map { it[customSurfaceColorKey] }
  }

  fun getFontScale(): Flow<Float> {
    return appContext.themeDataStore.data.map { it[fontScaleKey] ?: 1.0f }
  }

  fun getCornerRadius(): Flow<Float> {
    return appContext.themeDataStore.data.map { it[cornerRadiusKey] ?: 12f }
  }
}
