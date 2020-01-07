package com.dicoding.made.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.dicoding.made.R
import com.dicoding.made.ui.base.BaseActivity
import com.dicoding.made.ui.main.MainActivity
import com.dicoding.made.ui.notification.AlarmReceiver
import com.dicoding.made.utils.Constant
import org.koin.android.ext.android.inject

class SettingsActivity : BaseActivity() {

    override fun setView(): Int = R.layout.activity_settings

    override fun activityCreated(savedInstance: Bundle?) {
        supportActionBar?.title = getString(R.string.settings_menu)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

        private val alarm: AlarmReceiver by inject()

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.setting_preferences, rootKey)
            retainInstance = true
            initSettings()
        }

        private fun initSettings() {
            findPreference<ListPreference>(getString(R.string.setting_language))?.onPreferenceChangeListener = this
            findPreference<SwitchPreferenceCompat>(getString(R.string.daily_reminder_key))?.onPreferenceChangeListener = this
            findPreference<SwitchPreferenceCompat>(getString(R.string.new_release_key))?.onPreferenceChangeListener = this
        }

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            if (preference is Preference && newValue is String) {
                if (preference.key == getString(R.string.setting_language)) {
                    val intent = Intent(context,MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
            }
            else if (preference is Preference && newValue is Boolean) {
                when (preference.key) {
                    getString(R.string.daily_reminder_key) -> activateAlarm(newValue,Constant.ID_DAILY_REMINDER,Constant.REMINDER_HOUR)
                    getString(R.string.new_release_key) -> activateAlarm(newValue,Constant.ID_NEW_RELEASE,Constant.RELEASE_HOUR)
                }
            }
            return true
        }

        private fun activateAlarm(isActive: Boolean,id: Int,hour: Int) {
            if (isActive) alarm.createAlarm(id,hour)
            else alarm.cancelAlarm(id)
        }
    }
}