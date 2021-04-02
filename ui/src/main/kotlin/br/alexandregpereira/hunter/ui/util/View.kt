package br.alexandregpereira.hunter.ui.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp

fun Context.createComposeView(
    content: @Composable (PaddingValues) -> Unit
): ComposeView {
    return ComposeView(this).doOnApplyWindowInsets { insets ->
        val top = insets.systemWindowInsetTop
        val bottom = insets.systemWindowInsetBottom

        setContent {
            content(
                PaddingValues(
                    top = top.toDp(this@createComposeView).dp,
                    bottom = bottom.toDp(this@createComposeView).dp
                )
            )
        }
    }
}

fun ComposeView.doOnApplyWindowInsets(
    block: ComposeView.(WindowInsets) -> Unit
): ComposeView {
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding & margin states
    setOnApplyWindowInsetsListener { _, insets ->
        block(insets)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
    return this
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun Int.toDp(context: Context): Float {
    return this / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}
