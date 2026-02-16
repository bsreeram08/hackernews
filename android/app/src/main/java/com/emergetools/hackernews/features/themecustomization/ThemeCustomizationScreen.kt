package com.emergetools.hackernews.features.themecustomization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emergetools.hackernews.features.settings.components.SettingsCard
import com.emergetools.hackernews.features.settings.components.SettingsSectionLabel
import com.emergetools.hackernews.ui.theme.CornerRadiusPreset
import com.emergetools.hackernews.ui.theme.FontSizePreset
import com.emergetools.hackernews.ui.theme.HackerNewsTheme
import com.emergetools.hackernews.ui.theme.ThemePreset

@Composable
fun ThemeCustomizationScreen(
  state: ThemeCustomizationState,
  actions: (ThemeCustomizationAction) -> Unit,
  navigation: (ThemeCustomizationNavigation) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(color = MaterialTheme.colorScheme.background),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Theme Customization",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
      )
      TextButton(onClick = { navigation(ThemeCustomizationNavigation.GoBack) }) {
        Text("Done")
      }
    }

    // Scrollable content
    Column(
      modifier = Modifier
        .verticalScroll(state = rememberScrollState())
        .padding(horizontal = 8.dp),
      verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      // Theme Presets Section
      SettingsSectionLabel("Theme Preset")
      ThemePresetSelector(
        selectedPreset = state.selectedPreset,
        onPresetSelected = { actions(ThemeCustomizationAction.PresetSelected(it)) }
      )
      
      Spacer(modifier = Modifier.height(8.dp))

      // Font Size Section
      SettingsSectionLabel("Font Size")
      FontSizeSelector(
        selectedPreset = state.fontSizePreset,
        onPresetSelected = { actions(ThemeCustomizationAction.FontSizeChanged(it)) }
      )

      Spacer(modifier = Modifier.height(8.dp))

      // Corner Radius Section
      SettingsSectionLabel("Corner Style")
      CornerRadiusSelector(
        selectedPreset = state.cornerRadiusPreset,
        onPresetSelected = { actions(ThemeCustomizationAction.CornerRadiusChanged(it)) }
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Action Buttons
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        TextButton(onClick = { actions(ThemeCustomizationAction.ResetToDefaults) }) {
          Text("Reset to Defaults")
        }
        Button(onClick = { actions(ThemeCustomizationAction.SaveChanges) }) {
          Text("Save Changes")
        }
      }

      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ThemePresetSelector(
  selectedPreset: ThemePreset,
  onPresetSelected: (ThemePreset) -> Unit
) {
  FlowRow(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    ThemePreset.values().filter { it != ThemePreset.CUSTOM }.forEach { preset ->
      ThemePresetCard(
        preset = preset,
        isSelected = preset == selectedPreset,
        onClick = { onPresetSelected(preset) }
      )
    }
  }
}

@Composable
private fun ThemePresetCard(
  preset: ThemePreset,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  SettingsCard(
    leadingIcon = {
      Box(
        modifier = Modifier
          .size(24.dp)
          .clip(CircleShape)
          .background(preset.primaryColor)
          .border(
            width = 2.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
            shape = CircleShape
          ),
        contentAlignment = Alignment.Center
      ) {
        if (isSelected) {
          Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = "Selected",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
        }
      }
    },
    label = preset.displayName,
    onClick = onClick
  )
}

@Composable
private fun FontSizeSelector(
  selectedPreset: FontSizePreset,
  onPresetSelected: (FontSizePreset) -> Unit
) {
  Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
    FontSizePreset.values().forEach { preset ->
      SettingsCard(
        leadingIcon = {
          if (preset == selectedPreset) {
            Icon(
              imageVector = Icons.Rounded.Check,
              contentDescription = "Selected",
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
          } else {
            Spacer(modifier = Modifier.width(20.dp))
          }
        },
        label = preset.displayName,
        onClick = { onPresetSelected(preset) }
      )
    }
  }
}

@Composable
private fun CornerRadiusSelector(
  selectedPreset: CornerRadiusPreset,
  onPresetSelected: (CornerRadiusPreset) -> Unit
) {
  Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
    CornerRadiusPreset.values().forEach { preset ->
      SettingsCard(
        leadingIcon = {
          Box(
            modifier = Modifier
              .size(24.dp)
              .clip(RoundedCornerShape(preset.radius.dp))
              .background(MaterialTheme.colorScheme.primary)
              .border(
                width = if (preset == selectedPreset) 2.dp else 0.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(preset.radius.dp)
              )
          )
        },
        label = preset.displayName,
        onClick = { onPresetSelected(preset) }
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun ThemeCustomizationScreenPreview() {
  HackerNewsTheme {
    ThemeCustomizationScreen(
      state = ThemeCustomizationState(isLoading = false),
      actions = {},
      navigation = {}
    )
  }
}
