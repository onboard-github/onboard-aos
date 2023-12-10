package com.yapp.bol.designsystem.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import com.yapp.bol.designsystem.databinding.DialogMessageCancelConfirmBinding
import com.yapp.bol.designsystem.util.dialogWidthResize
import com.yapp.bol.designsystem.util.createBoldSpannable

class MessageCancelConfirmDialog(
    private val context: Context,
    private val message: SpannableStringBuilder,
    private val confirmMessage: String,
    private val onClick: MessageCancelConfirmOnClick,
) : Dialog(context) {

    constructor(
        context: Context,
        message: String,
        confirmMessage: String,
        onClick: MessageCancelConfirmOnClick,
    ): this(context, SpannableStringBuilder(message), confirmMessage, onClick)

    constructor(
        context: Context,
        originalMessage: String,
        boldStringFromOriginal: List<String>,
        confirmMessage: String,
        onClick: MessageCancelConfirmOnClick,
    ): this(context, createBoldSpannable(originalMessage, boldStringFromOriginal), confirmMessage, onClick)

    private lateinit var binding: DialogMessageCancelConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DialogMessageCancelConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBackgroundView()
        resizeDialog()
        bind()
    }

    private fun bind() {
        setText()
        setOnClick()
    }

    private fun setText() {
        binding.tvContent.text = message
        binding.btnConfirm.text = confirmMessage
    }

    private fun setOnClick() {
        binding.btnCancel.setOnClickListener {
            onClick.onCancelClick()
            if (onClick.onCancelClickNeedDismissDialog) { dismiss() }
        }
        binding.btnConfirm.setOnClickListener {
            onClick.onConfirmClick()
            if (onClick.onConfirmClickNeedDismissDialog) { dismiss() }
        }
    }

    private fun setBackgroundView() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun resizeDialog() {
        context.dialogWidthResize(this, 0.9f)
    }
}
