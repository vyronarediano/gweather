package com.ced.commons.ui.extensions

import android.os.Build
import android.text.Html
import android.text.Spanned

fun String?.fromHtmlAppCompat(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else Html.fromHtml(this)
}