package com.emergetools.hackernews.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class UserStorage(private val appContext: Context) {
  private val masterKey = MasterKey.Builder(appContext)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

  private val encryptedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
    appContext,
    "secure_user_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
  )

  private val cookieFlow = MutableStateFlow<String?>(encryptedPrefs.getString(COOKIE_KEY, null))

  suspend fun saveCookie(cookie: String) {
    withContext(Dispatchers.IO) {
      encryptedPrefs.edit().putString(COOKIE_KEY, cookie).apply()
      cookieFlow.value = cookie
    }
  }

  suspend fun clearCookie() {
    withContext(Dispatchers.IO) {
      encryptedPrefs.edit().remove(COOKIE_KEY).apply()
      cookieFlow.value = null
    }
  }

  fun getCookie(): Flow<String?> {
    return cookieFlow.asStateFlow()
  }

  companion object {
    private const val COOKIE_KEY = "auth_cookie"
  }
}
