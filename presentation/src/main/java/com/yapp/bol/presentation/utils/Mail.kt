package com.yapp.bol.presentation.utils

import android.content.Context
import android.content.Intent
import com.yapp.bol.presentation.R

fun Context.sendMailToHelpAddress(
    title: String,
    content: String,
) {
    val email = Intent(Intent.ACTION_SEND)
    val onboardMail = resources.getString(R.string.onboard_mail)
    email.apply {
        type = "plain/text"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(onboardMail))
        putExtra(Intent.EXTRA_SUBJECT, title)
        putExtra(Intent.EXTRA_TEXT, content)
    }.also { startActivity(it) }
}
