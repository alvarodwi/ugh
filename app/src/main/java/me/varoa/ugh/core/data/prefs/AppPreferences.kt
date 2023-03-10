package me.varoa.ugh.core.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("prefs")

@Singleton
class AppPreferences(
    appContext: Context
) {
    private val prefsDataStore = appContext.dataStore

    private val THEME_KEY = booleanPreferencesKey("theme")

    fun isDarkMode(): Flow<Boolean> = prefsDataStore.data.map { prefs -> prefs[THEME_KEY] ?: false }
    suspend fun setDarkMode(flag: Boolean) {
        prefsDataStore.edit { prefs -> prefs[THEME_KEY] = flag }
    }
}
