package com.yapp.bol.designsystem.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import com.yapp.bol.designsystem.databinding.DialogMessageConfirmBinding
import com.yapp.bol.designsystem.util.createBoldSpannable
import com.yapp.bol.designsystem.util.dialogWidthResize

class MessageConfirmDialog(
    private val context: Context,
    private val message: SpannableStringBuilder,
    private val confirmMessage: String,
) : Dialog(context) {

    constructor(
        context: Context,
        message: String,
        confirmMessage: String,
    ): this(context, SpannableStringBuilder(message), confirmMessage)

    constructor(
        context: Context,
        originalMessage: String,
        boldStringFromOriginal: List<String>,
        confirmMessage: String,
    ): this(context, createBoldSpannable(originalMessage, boldStringFromOriginal), confirmMessage)


    private lateinit var binding: DialogMessageConfirmBinding
    var onConfirmClickNeedDismissDialog: Boolean = true
    private var onConfirmClick: () -> Unit = { }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DialogMessageConfirmBinding.inflate(layoutInflater)
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
}
