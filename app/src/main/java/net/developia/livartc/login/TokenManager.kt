package net.developia.livartc.login

import android.content.Context

object TokenManager {

    private const val PREF_NAME = "MyAppPreferences"
    private const val KEY_TOKEN = "Authorization"

    fun saveToken(context: Context, token: String) {
        val editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TOKEN, null)
    }
}