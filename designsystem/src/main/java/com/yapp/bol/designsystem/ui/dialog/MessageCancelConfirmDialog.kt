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

/**
 * @constructor [MessageCancelConfirmDialog]의 인스턴스 생성
 * @param message 표시할 메시지를 나타내는 [SpannableStringBuilder].
 * @param confirmMessage 확인 버튼의 텍스트를 나타내는 [String].
 */
class MessageCancelConfirmDialog(
    private val context: Context,
    private val message: SpannableStringBuilder,
    private val confirmMessage: String,
) : Dialog(context) {

    /**
     * @constructor [MessageCancelConfirmDialog]의 인스턴스 생성
     * @param message 표시할 메시지를 나타내는 [String].
     * @param confirmMessage 확인 버튼의 텍스트를 나타내는 [String].
     */
    constructor(
        context: Context,
        message: String,
        confirmMessage: String,
    ) : this(context, SpannableStringBuilder(message), confirmMessage)

    /**
     * @constructor [MessageCancelConfirmDialog]의 인스턴스 생성
     * @param originalMessage 표시할 메시지를 나타내는 [String].
     * @param boldStringFromOriginal 볼드체로 표시될 섹션을 나타내는 [List]의 [String].
     * @param confirmMessage 확인 버튼의 텍스트를 나타내는 [String].
     */
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

    /**
     * 확인 버튼에 대한 사용자 지정 클릭 리스너를 설정합니다.
     *
     * @param onConfirmClickListener 확인 버튼이 클릭될 때 수행할 동작을 나타내는 람다 함수.
     */
    fun setOnConfirmClick(onConfirmClickListener: () -> Unit) {
        onConfirmClick = onConfirmClickListener
    }

    /**
     * 취소 버튼에 대한 사용자 지정 클릭 리스너를 설정합니다.
     *
     * @param onCancelClickListener 취소 버튼이 클릭될 때 수행할 동작을 나타내는 람다 함수.
     */
    fun setOnCancelClick(onCancelClickListener: () -> Unit) {
        onCancelClick = onCancelClickListener
    }
}
