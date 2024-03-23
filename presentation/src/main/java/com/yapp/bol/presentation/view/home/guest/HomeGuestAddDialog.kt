package com.yapp.bol.presentation.view.home.guest

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.yapp.bol.designsystem.R
import com.yapp.bol.presentation.databinding.HomeGuestAddDialogBinding
import com.yapp.bol.presentation.utils.Converter
import com.yapp.bol.presentation.utils.dialogWidthResize
import com.yapp.bol.presentation.utils.textChangesToFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeGuestAddDialog(
    private val context: Context,
    private val onAddGuestButtonClicked: (String) -> Unit,
    private val scope: CoroutineScope,
    private val checkNicknameValidation: (String) -> Unit,
) : Dialog(context) {

    private lateinit var binding: HomeGuestAddDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = HomeGuestAddDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUISetting()
        setEventHandler()
    }

    private fun initUISetting() {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.BOTTOM)
        context.dialogWidthResize(this, 1f)
    }

    private fun setEventHandler() {
        setNicknameTypedEventHandler()
        setAddGuestButtonEventHandler()
    }

    /**
     * guest nickname 타이핑 시 필요한 로직 처리
     * - 닉네임 길이 체크
     * - 닉네임 유효성 체크
     */
    @OptIn(FlowPreview::class)
    private fun setNicknameTypedEventHandler() = scope.launch {
        val typedNicknameFlow = binding.etGuestName.textChangesToFlow()
        val debounceDuration = 300L

        typedNicknameFlow
            .onEach { handleNicknameLength(it?.count() ?: 0) }
            .debounce(debounceDuration)
            .onEach { checkNicknameValidation(it.toString()) }
            .launchIn(this)
    }

    private fun handleNicknameLength(length: Int) {
        val color = if (length == 10) R.color.Red else R.color.Gray_8
        binding.tvGuestNameCount.setTextColor(ContextCompat.getColor(context, color))
        binding.tvGuestNameCount.text = Converter.convertLengthToString(PROFILE_NAME_MAX_LENGTH, length)
    }

    private fun setAddGuestButtonEventHandler() {
        binding.btnGuestAdd.setOnClickListener {
            onAddGuestButtonClicked(binding.etGuestName.text.toString())
        }
    }

    override fun show() {
        super.show()
        binding.etGuestName.requestFocus()
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    override fun dismiss() {
        binding.etGuestName.text = null
        binding.btnGuestAdd.isEnabled = false
        super.dismiss()
    }

    fun setTypedNicknameValid() {
        binding.btnGuestAdd.isEnabled = true
        binding.tvDuplicationGuide.isVisible = false
    }

    fun setTypedNicknameInvalid(isDuplicated: Boolean) {
        binding.btnGuestAdd.isEnabled = false
        binding.tvDuplicationGuide.isVisible = isDuplicated
    }

    companion object {
        const val PROFILE_NAME_MAX_LENGTH = 10
    }
}
