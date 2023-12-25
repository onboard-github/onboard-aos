package com.yapp.bol.designsystem.util

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager

fun Context.dialogWidthResize(
    dialog: Dialog,
    widthRatio: Float,
) {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    if (Build.VERSION.SDK_INT < 30) {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        val window = dialog.window
        val width = (size.x * widthRatio).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        window?.setLayout(width, height)
    } else {
        val rect = windowManager.currentWindowMetrics.bounds

        val window = dialog.window
        val x = (rect.width() * widthRatio).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT

        window?.setLayout(x, height)
    }
}
