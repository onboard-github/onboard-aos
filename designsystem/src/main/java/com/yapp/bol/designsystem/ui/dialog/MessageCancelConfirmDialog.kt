package com.yapp.bol.designsystem.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import com.yapp.bol.designsystem.databinding.DialogMessageCancelConfirmBinding
import com.yapp.bol.designsystem.util.dialogWidthResize
import com.yapp.bol.designsystem.util.createBoldSpannable

class MessageCancelConfirmDialog(
    private val context: Context,
    private val message: SpannableStringBuilder,
    private val confirmMessage: String,
) : Dialog(context) {

    constructor(
        context: Context,
        message: String,
        confirmMessage: String,
    ) : this(context, SpannableStringBuilder(message), confirmMessage)

    constructor(
        context: Context,
        originalMessage: String,
        boldStringFromOriginal: List<String>,
        confirmMessage: String,
    ) : this(context, createBoldSpannable(originalMessage, boldStringFromOriginal), confirmMessage)

    private lateinit var binding: DialogMessageCancelConfirmBinding
    var onCancelClickNeedDismissDialog: Boolean = true
    var onConfirmClickNeedDismissDialog: Boolean = true
    private var onConfirmClick: () -> Unit = { }
    private var onCancelClick: () -> Unit = { }

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
        binding.btnConfirm.setOnClickListener {
            onConfirmClick.invoke()
            if (onConfirmClickNeedDismissDialog) { dismiss() }
        }
        binding.btnCancel.setOnClickListener {
            onCancelClick.invoke()
            if (onCancelClickNeedDismissDialog) { dismiss() }
        }
    }

    private fun setBackgroundView() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun resizeDialog() {
        context.dialogWidthResize(this, 0.9f)
    }

    fun setOnConfirmClick(onConfirmClickListener: () -> Unit) {
        onConfirmClick = onConfirmClickListener
    }

    fun setOnCancelClick(onCancelClickListener: () -> Unit) {
        onCancelClick = onCancelClickListener
    }
}
