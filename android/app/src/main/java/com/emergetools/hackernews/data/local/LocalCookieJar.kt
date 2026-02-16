package com.emergetools.hackernews.data.local

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * LocalCookieJar handles authentication cookies for the HackerNews API.
 * Note: Uses runBlocking as OkHttp's CookieJar interface is synchronous.
 * This is acceptable for the limited cookie operations performed here.
 */
class LocalCookieJar(private val userStorage: UserStorage) : CookieJar {

  override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
    cookies.firstOrNull { it.name == "user" }?.let { authCookie ->
      // Using runBlocking here as CookieJar interface is synchronous
      // Alternative would be to use a blocking wrapper around EncryptedSharedPreferences
      runBlocking { userStorage.saveCookie(authCookie.value) }
    }
  }

  override fun loadForRequest(url: HttpUrl): List<Cookie> {
    // Using runBlocking here as CookieJar interface is synchronous
    val authCookie = runBlocking { userStorage.getCookie().first() }
    return if (authCookie != null) {
      val cookie = Cookie.Builder()
        .name("user")
        .value(authCookie)
        .domain("news.ycombinator.com")
        .build()
      listOf(cookie)
    } else {
      emptyList()
    }
  }
}
