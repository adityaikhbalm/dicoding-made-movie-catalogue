package com.dicoding.made.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.made.utils.AppPreferences
import com.google.android.material.snackbar.Snackbar
import java.util.*

abstract class BaseActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setView())
        activityCreated(savedInstanceState)
    }

    fun showSnackBar(message: Int) =
        Snackbar.make(
            findViewById(android.R.id.content),
            getString(message),Snackbar.LENGTH_LONG
        ).show()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    private fun updateBaseContextLocale(context: Context?): Context? {
        AppPreferences.init(context)
        val language = AppPreferences.language
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context?.resources?.configuration
        config?.setLocale(locale)

        if (config != null) return context.createConfigurationContext(config)
        return context
    }

    abstract fun setView(): Int
    abstract fun activityCreated(savedInstance: Bundle?)
}