// IT21171338 - TENNAKOON T. M. T. C.-  TOKEN MANAGER

package com.example.trove_mobile.utils


import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "app_prefs"
    private const val KEY_JWT_TOKEN = "jwt_token"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_JWT_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_JWT_TOKEN, null)
    }

    fun clearToken() {
        prefs.edit().remove(KEY_JWT_TOKEN).apply()
    }
}
