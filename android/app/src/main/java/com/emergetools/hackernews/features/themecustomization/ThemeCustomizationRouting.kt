package com.emergetools.hackernews.features.themecustomization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emergetools.hackernews.themeStorage

@Composable
fun ThemeCustomizationRoute(
  onNavigateBack: () -> Unit
) {
  val context = LocalContext.current
  val viewModel: ThemeCustomizationViewModel = viewModel(
    factory = ThemeCustomizationViewModel.Factory(context.themeStorage())
  )
  val state by viewModel.state.collectAsState()

  ThemeCustomizationScreen(
    state = state,
    actions = { action ->
      viewModel.actions(action)
      if (action is ThemeCustomizationAction.SaveChanges) {
        // Optionally navigate back after saving
      }
    },
    navigation = { nav ->
      when (nav) {
        ThemeCustomizationNavigation.GoBack -> onNavigateBack()
      }
    }
  )
}
