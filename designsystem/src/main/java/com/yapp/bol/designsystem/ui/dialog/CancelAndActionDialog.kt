package com.yapp.bol.designsystem.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.yapp.bol.designsystem.databinding.DialogCancelAndActionBinding
import com.yapp.bol.designsystem.util.createEmphasizeSpannable

class CancelAndActionDialog(
    private val topMessage: SpannableStringBuilder?,
    private val bottomMessage: SpannableStringBuilder? = null,
    private val cancelButtonText: String,
    private val actionButtonText: String = "취소",
    private var onCancelClickDismissDialog: Boolean = true,
    private var onActionClickDismissDialog: Boolean = true,
) : DialogFragment() {

    private constructor(builder: Builder) : this(
        createEmphasizeSpannable(builder.topMessage, builder.boldStringsOfTopMessage),
        createEmphasizeSpannable(builder.bottomMessage, builder.boldStringOfBottomMessage),
        builder.cancelButtonText,
        builder.actionButtonText,
        builder.onCancelClickDismissDialog,
        builder.onActionClickDismissDialog,
    )

    /**
     * 다양한 메시지와 설정을 갖는 MessageConfirmDialog를 생성하는 빌더 클래스입니다.
     * @property topMessage Dialog의 상단에 표시될 메시지입니다.
     * @property boldStringsOfTopMessage 상단 메시지에서 Bold로 표시할 문자열 목록입니다.
     * @property bottomMessage Dialog의 하단에 표시될 메시지입니다.
     * @property boldStringOfBottomMessage 하단 메시지에서 Bold로 표시할 문자열 목록입니다.
     * @property buttonText Dialog의 확인 버튼에 표시될 텍스트입니다.
     * @property onButtonClickDismissDialog 확인 버튼 클릭 시 다이얼로그를 닫을지 여부를 결정하는 플래그입니다.
     */
    class Builder {
        var topMessage: String = ""
        var boldStringsOfTopMessage: List<String>? = null
        var bottomMessage: String? = null
        var boldStringOfBottomMessage: List<String>? = null
        var cancelButtonText: String = "취소"
        var actionButtonText: String = "확인"
        var onCancelClickDismissDialog: Boolean = true
        var onActionClickDismissDialog: Boolean = true
        fun build() = CancelAndActionDialog(this)
    }

    companion object {
        /**
         * Kotlin DSL로, 아래와 같이 사용하시면 됩니다.
         * ```
         * OneButtonDialog.create {
         *      topMessage = "상단 메시지"
         *      boldStringsOfTopMessage = listOf("강조할", "메시지")
         *      bottomMessage = "하단 메시지"
         *      boldStringOfBottomMessage = listOf("강조할", "메시지")
         *      actionButtonText = "확인"
         *      cancelButtonText = "확인"
         *      onCancelClickDismissDialog = true
         *      onActionClickDismissDialog = true
         * }
         * ```
         */
        inline fun create(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    fun interface OnButtonClick {
        fun action()
    }

    /**
     * 우측 하단의 커스텀한 텍스트를 가진 액션 버튼 클릭 시 수행될 행동을 정의하며 아래와 같이 사용하시면 됩니다.
     * ```
     * oneButtonDialog.setOnButtonClickListener { /* action */ }
     * ```
     */
    fun setOnActionClickListener(onButtonClick: OnButtonClick?) {
        this.onActionClick = onButtonClick
    }

    /**
     * 좌측 하단의 취소 버튼 클릭 시 수행될 행동을 정의하며 아래와 같이 사용하시면 됩니다.
     * ```
     * oneButtonDialog.setOnButtonClickListener { /* action */ }
     * ```
     */
    fun setOnCancelClickListener(onButtonClick: OnButtonClick?) {
        this.onCancelClick = onButtonClick
    }

    private lateinit var binding: DialogCancelAndActionBinding
    private var onCancelClick: OnButtonClick? = null
    private var onActionClick: OnButtonClick? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCancelAndActionBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureUI()
    }

    private fun configureUI() {
        setText()
        handleEvent()
    }

    private fun setText() {
        binding.tvContentTop.text = topMessage
        binding.btnAction.text = actionButtonText
        bottomMessage?.let {
            binding.tvContentBottom.text = it
            binding.tvContentBottom.visibility = View.VISIBLE
        } ?: kotlin.run { binding.tvContentBottom.visibility = View.GONE }
        binding.btnCancel.text = cancelButtonText
    }

    private fun handleEvent() {
        binding.btnAction.setOnClickListener {
            onActionClick?.action()
            if (onActionClickDismissDialog) { dismiss() }
        }
        binding.btnCancel.setOnClickListener {
            onCancelClick?.action()
            if (onCancelClickDismissDialog) { dismiss() }
        }
    }
}
