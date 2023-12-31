package com.yapp.bol.presentation.view.mypage

import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.findNavController
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseActivity
import com.yapp.bol.presentation.databinding.ActivityProfileSettingBinding
import com.yapp.bol.presentation.utils.Converter
import com.yapp.bol.presentation.utils.isNicknameValid
import com.yapp.bol.presentation.utils.textChangesToFlow
import com.yapp.bol.presentation.view.mypage.MyPageFragment.Companion.GROUP_ID
import com.yapp.bol.presentation.view.mypage.MyPageFragment.Companion.GROUP_NAME
import com.yapp.bol.presentation.view.mypage.MyPageFragment.Companion.MEMBER_ID
import com.yapp.bol.presentation.view.mypage.MyPageFragment.Companion.NICKNAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileSettingActivity : BaseActivity<ActivityProfileSettingBinding>(R.layout.activity_profile_setting) {

    private val profileSettingViewModel: ProfileSettingViewModel by viewModels()

    private val groupId by lazy { intent.getIntExtra(GROUP_ID, -1) }
    private val groupName by lazy { intent.getStringExtra(GROUP_NAME) }
    private val nickname by lazy { intent.getStringExtra(NICKNAME) }
    private val memberId by lazy { intent.getIntExtra(MEMBER_ID, -1) }

    override fun onCreateAction() {
        setBackButton()
        setDebounce()
        binding.etUserName.doOnTextChanged { _, start, _, count ->
            setNicknameCount(start, count)
        }
        binding.tvGroupName.text = groupName
        binding.tvNickname.text = nickname

        profileSettingViewModel.nicknameValidate.observe(this) {
            setNicknameValid(it.isAvailable, it.reason)
        }
        binding.btnSettingComplete.setOnClickListener {
            profileSettingViewModel.patchGroupMemberNicknameUseCase(
                groupId,
                memberId,
                binding.etUserName.text.toString(),
            )
        }

        profileSettingViewModel.newNickname.observe(this) {
            if (binding.etUserName.text.toString() == it) binding.root.findNavController().popBackStack()
        }
    }

    private fun setDebounce() {
        lifecycleScope.launch {
            whenStarted {
                val ediTextFlow = binding.etUserName.textChangesToFlow()
                val debounceDuration = 300L

                ediTextFlow
                    .debounce(debounceDuration)
                    .onEach {
                        val keyword = it.toString()
                        profileSettingViewModel.getValidateNickName(groupId, keyword)
                    }
                    .launchIn(this)
            }
        }
    }

    private fun setNicknameCount(start: Int, count: Int) {
        val color = if (count != 10) com.yapp.bol.designsystem.R.color.Gray_8 else com.yapp.bol.designsystem.R.color.Red
        binding.tvUserNameCount.setTextColor(ContextCompat.getColor(this, color))
        binding.tvUserNameCount.text = Converter.convertLengthToString(10, start + count)
    }

    private fun setBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setNicknameValid(value: Boolean, reason: String?) = with(binding) {
        val isNicknameValid = isNicknameValid(nickname)
        val isUserNameNotEmpty = etUserName.text.isNotEmpty()

        btnSettingComplete.isEnabled = value && isNicknameValid && isUserNameNotEmpty

        val guideTextResId = if (isUserNameNotEmpty && reason == "DUPLICATED_NICKNAME") {
            R.string.duplication_guide
        } else {
            R.string.nickname_setting_guide
        }
        tvNicknameSettingGuide.text = getString(guideTextResId)

        val guideTextColorResId = when {
            isUserNameNotEmpty && isNicknameValid -> if (value) com.yapp.bol.designsystem.R.color.Gray_8 else com.yapp.bol.designsystem.R.color.Red // ktlint-disable max-line-length
            isUserNameNotEmpty && reason == "DUPLICATED_NICKNAME" -> com.yapp.bol.designsystem.R.color.Red
            else -> com.yapp.bol.designsystem.R.color.Gray_8
        }
        tvNicknameSettingGuide.setTextColor(ContextCompat.getColor(applicationContext, guideTextColorResId))
    }
}
