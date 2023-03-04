package me.varoa.ugh.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import coil.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    @Inject protected lateinit var imageLoader: ImageLoader
    protected lateinit var eventJob: Job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView()
    }

    abstract fun bindView()

    override fun onStop() {
        super.onStop()
        if (this::eventJob.isInitialized) {
            eventJob.cancel()
        }
    }
}
