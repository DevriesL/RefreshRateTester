package io.github.devriesl.refreshratetester

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.display.DisplayManager
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("StaticFieldLeak")
class TesterViewModel(private val context: Context): ViewModel() {

    private val _refreshRate = MutableStateFlow(context.display?.refreshRate)
    val refreshRate: StateFlow<Float?>
        get() = _refreshRate

    private val _touchRate = MutableStateFlow<Long?>(null)
    val touchRate: StateFlow<Long?>
        get() = _touchRate

    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) {
        }

        override fun onDisplayRemoved(displayId: Int) {
        }

        override fun onDisplayChanged(displayId: Int) {
            _refreshRate.value = context.display?.refreshRate
        }
    }

    fun registerDisplayListener() {
        context.getSystemService<DisplayManager>()?.registerDisplayListener(displayListener, null)
    }

    fun supportSeamlessVrr(): Boolean {
        val type = context.getSystemService<DisplayManager>()?.matchContentFrameRateUserPreference
        return type == DisplayManager.MATCH_CONTENT_FRAMERATE_SEAMLESSS_ONLY || type == DisplayManager.MATCH_CONTENT_FRAMERATE_ALWAYS
    }

    fun getRefreshRates(): List<Float> {
        val displayModes = context.display?.supportedModes?.iterator()
        val refreshRates = mutableListOf<Float>()
        while (displayModes?.hasNext() == true) {
            val refreshRate = displayModes.next().refreshRate
            if (!refreshRates.contains(refreshRate)) {
                refreshRates.add(refreshRate)
            }
        }
        return refreshRates.toList()
    }

    fun updateTouchRate(touchRate: Long) {
        _touchRate.value = touchRate
    }
}