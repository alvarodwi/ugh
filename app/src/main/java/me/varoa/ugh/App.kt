package me.varoa.ugh

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // logcat CAN I HAZ LOG
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
        // material3 dynamic colors
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
