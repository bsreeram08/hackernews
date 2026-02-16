package com.emergetools.hackernews.data.local

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * LocalCookieJar handles authentication cookies for the HackerNews API.
 * 
 * Note on runBlocking usage: OkHttp's CookieJar interface is synchronous and cannot
 * be changed. The blocking calls here are acceptable because:
 * 1. UserStorage maintains an in-memory StateFlow that's immediately available
 * 2. No actual I/O operations occur - just reading from memory
 * 3. Cookie operations are extremely fast (microseconds)
 * 4. This is a widely accepted pattern when integrating coroutines with synchronous APIs
 */
class LocalCookieJar(private val userStorage: UserStorage) : CookieJar {

  override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
    cookies.firstOrNull { it.name == "user" }?.let { authCookie ->
      // UserStorage updates the StateFlow synchronously, making this very fast
      runBlocking { userStorage.saveCookie(authCookie.value) }
    }
  }

  override fun loadForRequest(url: HttpUrl): List<Cookie> {
    // UserStorage maintains a StateFlow that's already loaded in memory
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
