package me.varoa.ugh.ui.ext

import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

private fun Fragment.createSnackbar(
    message: String,
    duration: Int
): Snackbar = Snackbar.make(requireView(), message, duration)

fun Fragment.snackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT
) {
    createSnackbar(message, duration).show()
}
fun toggleAppTheme(value: AppTheme) {
    AppCompatDelegate.setDefaultNightMode(
        when (value) {
            AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    )
}
