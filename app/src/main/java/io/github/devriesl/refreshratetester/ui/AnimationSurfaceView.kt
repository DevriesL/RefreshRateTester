package io.github.devriesl.refreshratetester.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.view.Surface.CHANGE_FRAME_RATE_ONLY_IF_SEAMLESS
import android.view.Surface.FRAME_RATE_COMPATIBILITY_FIXED_SOURCE
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.ceil
import kotlin.math.min

class AnimationSurfaceView(context: Context): SurfaceView(context), SurfaceHolder.Callback {
    private var refreshRate: Float = 0f

    private val animationThread = object : Thread() {
        var circleRadius: Float = 0f
        val paint = Paint().apply { color = Color.WHITE }

        override fun run() {
            while (holder.surface.isValid) {
                val canvas = holder.lockCanvas()
                canvas.drawARGB(255, 13, 61, 80)
                canvas.drawCircle(
                    (canvas.width / 2).toFloat(),
                    (canvas.height / 2).toFloat(),
                    circleRadius, paint
                )

                if (circleRadius < min(canvas.width, canvas.height) / 2) {
                    circleRadius++
                } else {
                    circleRadius = 0f
                }
                holder.unlockCanvasAndPost(canvas)

                try {
                    sleep(ceil(1000 / refreshRate).toLong())
                } catch (ex: InterruptedException) {
                    ex.printStackTrace()
                }
            }
        }
    }

    fun changeRefreshRate(refreshRate: Float) {
        if (holder.surface.isValid) {
            holder.surface.setFrameRate(refreshRate, FRAME_RATE_COMPATIBILITY_FIXED_SOURCE, CHANGE_FRAME_RATE_ONLY_IF_SEAMLESS)
            this.refreshRate = refreshRate
            if (animationThread.state == Thread.State.NEW || animationThread.state == Thread.State.TERMINATED) {
                animationThread.start()
            }
        } else {
            animationThread.join()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
    }
}