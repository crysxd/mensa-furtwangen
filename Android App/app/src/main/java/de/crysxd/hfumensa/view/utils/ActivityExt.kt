package de.crysxd.hfumensa.view.utils

import android.app.Activity
import de.crysxd.hfumensa.R
import de.crysxd.hfumensa.view.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

fun Activity.setToolbarMode(toolbarMode: ToolbarMode, animated: Boolean = true) {
    if (this is MainActivity) {
        val (progressTranslationY, translationY) = when (toolbarMode) {
            ToolbarMode.IDLE -> {
                toolbarBackground.stop()
                val idleHeight = viewRoot.resources.getDimension(R.dimen.toolbarHeight).toInt()
                Pair(- toolbarBackground.height.toFloat(), idleHeight - toolbarBackground.height.toFloat())
            }
            ToolbarMode.LOADING -> {
                toolbarBackground.start()
                toolbarBackground.velocity = 10f
                Pair(0f,-toolbarBackground.height / 3f * 2f)
            }
            ToolbarMode.FULLSCREEN_LOADING -> {
                toolbarBackground.start()
                toolbarBackground.velocity = 10f
                Pair(0f,0f)
            }
        }

        if (animated) {
            toolbarBackground.animate().translationY(translationY).start()
            textSwitcherTitle.animate().translationY(translationY).start()
            toolbar.animate().translationY(translationY).start()
            progressBar.animate().translationY(progressTranslationY).start()
        } else {
            toolbarBackground.translationY = translationY
            textSwitcherTitle.translationY = translationY
            toolbar.translationY = translationY
            progressBar.translationY = progressTranslationY
        }
    }
}

fun Activity.getToolbar() = (if (this is MainActivity) {
    toolbar
} else {
    throw IllegalAccessException("Only available for MainActivity")
})!!

enum class ToolbarMode {
    IDLE,
    LOADING,
    FULLSCREEN_LOADING
}