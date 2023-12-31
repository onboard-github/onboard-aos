package com.yapp.bol.presentation.view.setting.group

import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentGroupSettingBinding

class GroupSettingFragment : BaseFragment<FragmentGroupSettingBinding>(R.layout.fragment_group_setting) {
    private val activityViewModel: GroupSettingViewModel by activityViewModels()

    override fun onViewCreatedAction() {
        super.onViewCreatedAction()
        setEventHandler()
    }

    private fun setEventHandler() {
        setBackEventHandler()
        setOwnerChangeEventHandler()
        setGroupDeleteEventHandler()
    }

    private fun setBackEventHandler() {
        binding.btnBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun setOwnerChangeEventHandler() {
        binding.btnChangeOwner.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_groupSettingFragment_to_changeOwnerFragment)
        }
    }

    private fun setGroupDeleteEventHandler() {
        binding.btnDeleteGroup.setOnClickListener {
            // todo 재홍 오빠 작업 부분
        }
    }
}
