package com.yapp.bol.presentation.view.mypage

import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentUserSettingBinding
import com.yapp.bol.presentation.utils.Converter
import com.yapp.bol.presentation.utils.isNicknameValid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSettingFragment : BaseFragment<FragmentUserSettingBinding>(R.layout.fragment_user_setting) {

    private val userSettingViewModel: UserSettingViewModel by viewModels()

    override fun onViewCreatedAction() {
        setBackButton()
        binding.etUserName.doOnTextChanged { text, start, _, count ->
            setNicknameCount(start, count)
            setNicknameValid(text.toString(), (start + count) > 0)
            userSettingViewModel.updateNewNickname(text.toString())
        }

        userSettingViewModel.nickname.observe(viewLifecycleOwner) {
            binding.tvNickname.text = it
        }

        binding.btnSettingComplete.setOnClickListener {
            userSettingViewModel.putUserName()
        }

        userSettingViewModel.isComplete.observe(viewLifecycleOwner) {
            if (it.not()) return@observe
            requireActivity().finish()
        }
    }

    private fun setNicknameCount(start: Int, count: Int) {
        val color = if (count != 10) com.yapp.bol.designsystem.R.color.Gray_8 else com.yapp.bol.designsystem.R.color.Red
        binding.tvUserNameCount.setTextColor(ContextCompat.getColor(requireContext(), color))
        binding.tvUserNameCount.text = Converter.convertLengthToString(10, start + count)
    }

    private fun setNicknameValid(nickname: String, isCount: Boolean) {
        val value = isNicknameValid(nickname)
        binding.btnSettingComplete.isEnabled = isCount && value
        val color = if (value) com.yapp.bol.designsystem.R.color.Gray_8 else com.yapp.bol.designsystem.R.color.Red
        binding.tvNicknameSettingGuide.setTextColor(ContextCompat.getColor(requireContext(), color))
    }

    private fun setBackButton() {
        binding.btnBack.setOnClickListener {
            binding.root.findNavController().popBackStack()
        }
    }
}
