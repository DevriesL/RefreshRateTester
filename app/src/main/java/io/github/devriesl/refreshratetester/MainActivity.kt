package io.github.devriesl.refreshratetester

import android.os.Bundle
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.TOOL_TYPE_FINGER
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.google.common.collect.EvictingQueue
import io.github.devriesl.refreshratetester.ui.MainPage
import io.github.devriesl.refreshratetester.ui.theme.RefreshRateTesterTheme

class MainActivity : ComponentActivity() {
    private val testerViewModel by lazy { TesterViewModel(this) }
    private var eventQueue = EvictingQueue.create<Long>(1000)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RefreshRateTesterTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainPage(testerViewModel)
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.pointerCount == 1
            && ev.action == ACTION_MOVE
            && ev.getToolType(0) == TOOL_TYPE_FINGER) {

            for (i in 0 until ev.historySize) {
                    eventQueue.add(ev.getHistoricalEventTime(i))
            }

            eventQueue.add(ev.eventTime)
        } else if (ev?.action != ACTION_MOVE && eventQueue.size > 100) {
            testerViewModel.updateTouchRate(calcTouchRate())
            eventQueue.clear()
        } else if (ev?.pointerCount != 1) {
            eventQueue.clear()
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun calcTouchRate(): Long {
        val eventIntervals = eventQueue.zipWithNext().map {
            it.second - it.first
        }.filter {
            it > 0
        }

        val intervalsCount = eventIntervals.toSortedSet().map {
            Pair(it, eventIntervals.count { interval -> interval == it })
        }

        val reliableInterval = intervalsCount.maxBy { it.second }.first

        return 1000 / reliableInterval
    }
}
