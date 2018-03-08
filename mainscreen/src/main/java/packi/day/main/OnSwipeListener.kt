package packi.day.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class OnSwipeListener(ctx: Context, listener: SwipeListener) : View.OnTouchListener {

    private val gestureDetector = GestureDetector(ctx, GestureListener(listener))

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener(private val listener: SwipeListener) : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val diffY = e2.y - e1.y
            val diffX = e2.x - e1.x

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        listener.onSwipe(Direction.LEFT)
                    } else {
                        listener.onSwipe(Direction.RIGHT)
                    }
                }
            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    listener.onSwipe(Direction.DOWN)
                } else {
                    listener.onSwipe(Direction.UP)
                }
            }
            return true
        }

    }

    private companion object {
        const val SWIPE_THRESHOLD = 100
        const val SWIPE_VELOCITY_THRESHOLD = 100
    }

}

enum class Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN
}

interface SwipeListener {

    fun onSwipe(direction: Direction)

}