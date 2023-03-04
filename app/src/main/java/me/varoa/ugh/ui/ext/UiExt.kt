package me.varoa.ugh.ui.ext

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
