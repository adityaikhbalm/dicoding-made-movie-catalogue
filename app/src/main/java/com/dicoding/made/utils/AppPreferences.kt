package com.dicoding.made.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object AppPreferences {
    private lateinit var preferences: SharedPreferences

    fun init(context: Context?) {
        if (context != null) preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var firstRun: Boolean
        get() = preferences.getBoolean(Constant.const_splash_screen, false)
        set(value) = preferences.edit {
            it.putBoolean(Constant.const_splash_screen, value)
        }

    var language: String
        get() = preferences.getString(Constant.const_language, Constant.const_default_language) ?: "en"
        set(value) = preferences.edit {
            it.putString(Constant.const_language, value)
        }
}