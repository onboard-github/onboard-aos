package com.yapp.bol.designsystem.util

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan

fun createBoldSpannable(
    originalString: String?,
    boldStringFromOriginal: List<String>?
): SpannableStringBuilder? {
    if (originalString == null) { return null }

    val boldIndexList: MutableList<Pair<Int, Int>> = mutableListOf()

    val spannableBuilder = SpannableStringBuilder(originalString)

    boldStringFromOriginal?.forEach { boldString ->
        var startIndex = 0
        var indexOfBoldString = originalString.indexOf(boldString, startIndex)

        while (indexOfBoldString != -1) {
            val endIndex = indexOfBoldString + boldString.length
            boldIndexList.add(Pair(indexOfBoldString, endIndex))

            startIndex = endIndex
            indexOfBoldString = originalString.indexOf(boldString, startIndex)
        }
    }

    boldIndexList.forEach { (start, end) ->
        spannableBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, 0)
    }

    return spannableBuilder
}
