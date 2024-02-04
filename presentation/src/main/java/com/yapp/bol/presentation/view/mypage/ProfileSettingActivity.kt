package com.yapp.bol.presentation.view.mypage

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseActivity
import com.yapp.bol.presentation.databinding.ActivityProfileSettingBinding
import com.yapp.bol.presentation.utils.Converter
import com.yapp.bol.presentation.utils.isNicknameValid
import com.yapp.bol.presentation.utils.textChangesToFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import com.yapp.bol.designsystem.R as designR
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
            checkNicknameValid(it.isAvailable, it.reason)
        }
        binding.btnSettingComplete.setOnClickListener {
            profileSettingViewModel.patchGroupMemberNicknameUseCase(
                groupId.toLong(),
                memberId.toLong(),
                binding.etUserName.text.toString(),
            )
        }

        profileSettingViewModel.newNickname.observe(this) {
            if (binding.etUserName.text.toString() == it) finish()
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
        val color = if (count != 10) designR.color.Gray_8 else designR.color.Red
        binding.tvUserNameCount.setTextColor(ContextCompat.getColor(this, color))
        binding.tvUserNameCount.text = Converter.convertLengthToString(10, start + count)
    }

    private fun setBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun checkNicknameValid(value: Boolean, reason: String?) = with(binding) {
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
            isUserNameNotEmpty && isNicknameValid -> if (value) designR.color.Gray_8 else designR.color.Red // ktlint-disable max-line-length
            isUserNameNotEmpty && reason == "DUPLICATED_NICKNAME" -> designR.color.Red
            else -> com.yapp.bol.designsystem.R.color.Gray_8
        }
        tvNicknameSettingGuide.setTextColor(ContextCompat.getColor(applicationContext, guideTextColorResId))
    }

    companion object {
        const val GROUP_ID = "group_id"
        const val GROUP_NAME = "group_name"
        const val NICKNAME = "nickname"
        const val MEMBER_ID = "member_id"

        fun startActivity(context: Context, groupId: Int, groupName: String, nickname: String, memberId: Int) {
            val intent = Intent(context, ProfileSettingActivity::class.java).apply {
                putExtra(GROUP_ID, groupId)
                putExtra(GROUP_NAME, groupName)
                putExtra(NICKNAME, nickname)
                putExtra(MEMBER_ID, memberId)
            }
            context.startActivity(intent)
        }
    }
}
