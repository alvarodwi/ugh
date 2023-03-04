package me.varoa.ugh

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import logcat.AndroidLogcatLogger
import logcat.LogPriority

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // logcat CAN I HAZ LOG
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }
}
