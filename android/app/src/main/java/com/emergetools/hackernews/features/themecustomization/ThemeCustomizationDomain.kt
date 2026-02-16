package com.emergetools.hackernews.features.themecustomization

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.emergetools.hackernews.data.local.ThemeStorage
import com.emergetools.hackernews.ui.theme.CornerRadiusPreset
import com.emergetools.hackernews.ui.theme.FontSizePreset
import com.emergetools.hackernews.ui.theme.ThemePreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ThemeCustomizationState(
  val selectedPreset: ThemePreset = ThemePreset.DEFAULT,
  val customPrimaryColor: Color = ThemePreset.DEFAULT.primaryColor,
  val customBackgroundColor: Color = ThemePreset.DEFAULT.backgroundLight,
  val customSurfaceColor: Color = ThemePreset.DEFAULT.surfaceLight,
  val fontSizePreset: FontSizePreset = FontSizePreset.MEDIUM,
  val cornerRadiusPreset: CornerRadiusPreset = CornerRadiusPreset.ROUNDED,
  val isLoading: Boolean = true
)

sealed interface ThemeCustomizationAction {
  data class PresetSelected(val preset: ThemePreset) : ThemeCustomizationAction
  data class CustomPrimaryColorChanged(val color: Color) : ThemeCustomizationAction
  data class CustomBackgroundColorChanged(val color: Color) : ThemeCustomizationAction
  data class CustomSurfaceColorChanged(val color: Color) : ThemeCustomizationAction
  data class FontSizeChanged(val preset: FontSizePreset) : ThemeCustomizationAction
  data class CornerRadiusChanged(val preset: CornerRadiusPreset) : ThemeCustomizationAction
  data object SaveChanges : ThemeCustomizationAction
  data object ResetToDefaults : ThemeCustomizationAction
}

sealed interface ThemeCustomizationNavigation {
  data object GoBack : ThemeCustomizationNavigation
}

class ThemeCustomizationViewModel(
  private val themeStorage: ThemeStorage
) : ViewModel() {
  
  private val internalState = MutableStateFlow(ThemeCustomizationState())
  val state = internalState.asStateFlow()

  init {
    loadThemePreferences()
  }

  private fun loadThemePreferences() {
    viewModelScope.launch {
      combine(
        themeStorage.getThemePreset(),
        themeStorage.getCustomPrimaryColor(),
        themeStorage.getCustomBackgroundColor(),
        themeStorage.getCustomSurfaceColor(),
        themeStorage.getFontScale(),
        themeStorage.getCornerRadius()
      ) { preset, primary, background, surface, fontScale, cornerRadius ->
        val themePreset = ThemePreset.fromString(preset)
        ThemeCustomizationState(
          selectedPreset = themePreset,
          customPrimaryColor = primary?.let { Color(it) } ?: themePreset.primaryColor,
          customBackgroundColor = background?.let { Color(it) } ?: themePreset.backgroundLight,
          customSurfaceColor = surface?.let { Color(it) } ?: themePreset.surfaceLight,
          fontSizePreset = FontSizePreset.fromScale(fontScale),
          cornerRadiusPreset = CornerRadiusPreset.fromRadius(cornerRadius),
          isLoading = false
        )
      }.collect { loadedState ->
        internalState.value = loadedState
      }
    }
  }

  fun actions(action: ThemeCustomizationAction) {
    when (action) {
      is ThemeCustomizationAction.PresetSelected -> {
        internalState.update { current ->
          current.copy(
            selectedPreset = action.preset,
            customPrimaryColor = action.preset.primaryColor,
            customBackgroundColor = action.preset.backgroundLight,
            customSurfaceColor = action.preset.surfaceLight
          )
        }
      }
      is ThemeCustomizationAction.CustomPrimaryColorChanged -> {
        internalState.update { current ->
          current.copy(
            selectedPreset = ThemePreset.CUSTOM,
            customPrimaryColor = action.color
          )
        }
      }
      is ThemeCustomizationAction.CustomBackgroundColorChanged -> {
        internalState.update { current ->
          current.copy(
            selectedPreset = ThemePreset.CUSTOM,
            customBackgroundColor = action.color
          )
        }
      }
      is ThemeCustomizationAction.CustomSurfaceColorChanged -> {
        internalState.update { current ->
          current.copy(
            selectedPreset = ThemePreset.CUSTOM,
            customSurfaceColor = action.color
          )
        }
      }
      is ThemeCustomizationAction.FontSizeChanged -> {
        internalState.update { current ->
          current.copy(fontSizePreset = action.preset)
        }
      }
      is ThemeCustomizationAction.CornerRadiusChanged -> {
        internalState.update { current ->
          current.copy(cornerRadiusPreset = action.preset)
        }
      }
      ThemeCustomizationAction.SaveChanges -> {
        saveThemePreferences()
      }
      ThemeCustomizationAction.ResetToDefaults -> {
        internalState.update {
          ThemeCustomizationState(isLoading = false)
        }
        saveThemePreferences()
      }
    }
  }

  private fun saveThemePreferences() {
    viewModelScope.launch {
      val current = internalState.value
      themeStorage.saveThemePreset(current.selectedPreset.name)
      themeStorage.saveCustomColors(
        primary = current.customPrimaryColor.toArgb(),
        background = current.customBackgroundColor.toArgb(),
        surface = current.customSurfaceColor.toArgb()
      )
      themeStorage.saveFontScale(current.fontSizePreset.scale)
      themeStorage.saveCornerRadius(current.cornerRadiusPreset.radius)
    }
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(private val themeStorage: ThemeStorage) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return ThemeCustomizationViewModel(themeStorage) as T
    }
  }
}
