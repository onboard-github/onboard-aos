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
import com.yapp.bol.designsystem.databinding.DialogOneButtonBinding
import com.yapp.bol.designsystem.util.createBoldSpannable

class OneButtonDialog constructor(
    private val topMessage: SpannableStringBuilder?,
    private val bottomMessage: SpannableStringBuilder? = null,
    private val confirmMessage: String = "확인",
    private var onButtonClickDismissDialog: Boolean = true
) : DialogFragment() {

    private constructor(builder: Builder) : this(
        createBoldSpannable(builder.topMessage, builder.boldStringsOfTopMessage),
        createBoldSpannable(builder.bottomMessage, builder.boldStringOfBottomMessage),
        builder.buttonText,
        builder.onButtonClickDismissDialog
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
        var buttonText: String = "확인"
        var onButtonClickDismissDialog = true
        fun build() = OneButtonDialog(this)
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
         *      buttonText = "확인"
         *      onButtonClickDismissDialog = true
         * }
         * ```
         */
        inline fun create(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    fun interface OnButtonClick {
        fun action()
    }

    /**
     * 아래와 같이 사용하시면 됩니다.
     * ```
     * oneButtonDialog.setOnButtonClickListener { /* action */ }
     * ```
     */
    fun setOnButtonClickListener(onButtonClick: OnButtonClick?) {
        this.onButtonClick = onButtonClick
    }

    private lateinit var binding: DialogOneButtonBinding
    private var onButtonClick: OnButtonClick? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogOneButtonBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureUI()
    }

    private fun configureUI() {
        setTextView()
        handleEvent()
    }

    private fun setTextView() {
        binding.tvContentTop.text = topMessage
        binding.btnConfirm.text = confirmMessage
        bottomMessage?.let {
            binding.tvContentBottom.text = it
            binding.tvContentBottom.visibility = View.VISIBLE
        } ?: kotlin.run { binding.tvContentBottom.visibility = View.GONE }
    }

    private fun handleEvent() {
        binding.btnConfirm.setOnClickListener {
            onButtonClick?.action()
            if (onButtonClickDismissDialog) { dismiss() }
        }
    }
}
