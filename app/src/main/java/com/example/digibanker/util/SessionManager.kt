package com.example.digibanker.util
import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences
    private val editor: SharedPreferences.Editor

    companion object {
        private const val PREFS_NAME = "DigiBankerPrefs"
        private const val KEY_USER_ID = "active_user_id"
        private const val NO_USER = -1L
    }

    init {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    fun saveUserSession(userId: Long) {
        editor.putLong(KEY_USER_ID, userId)
        editor.apply()
    }

    fun getActiveUserId(): Long? {
        val userId = prefs.getLong(KEY_USER_ID, NO_USER)
        return if (userId == NO_USER) {
            null
        } else {
            userId
        }
    }
    fun clearSession() {
        editor.remove(KEY_USER_ID)
        editor.apply()
    }
}