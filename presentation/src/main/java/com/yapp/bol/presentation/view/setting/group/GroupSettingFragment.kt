package com.yapp.bol.presentation.view.setting.group

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.yapp.bol.designsystem.ui.dialog.CancelAndActionDialog
import com.yapp.bol.designsystem.ui.dialog.OneButtonDialog
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentGroupSettingBinding
import com.yapp.bol.presentation.utils.showToast
import com.yapp.bol.presentation.view.login.splash.SplashActivity

class GroupSettingFragment : BaseFragment<FragmentGroupSettingBinding>(R.layout.fragment_group_setting) {

    private val activityViewModel: GroupSettingViewModel by activityViewModels()

    val dialog by lazy {
        CancelAndActionDialog.create {
            topMessage = "모임 삭제 시 모임과 관련된 모든 기록이\n삭제되며, 취소 또는 복구는 불가능합니다."
            boldStringOfBottomMessage = listOf(activityViewModel.groupName)
            bottomMessage = "${activityViewModel.groupName} 모임을 삭제하시겠습니까 ?"
            actionButtonText = "삭제하기"
        }
    }

    private val completeDialog by lazy {
        OneButtonDialog.create {
            topMessage = "${activityViewModel.groupName} 모임을 삭제했습니다.\n서비스를 다식 시작합니다."
            boldStringsOfTopMessage = listOf(activityViewModel.groupName)
        }.apply {
            setOnButtonClickListener {
                startActivity(Intent(requireActivity(), SplashActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    override fun onViewCreatedAction() {
        super.onViewCreatedAction()
        setEventHandler()
    }

    private fun setEventHandler() {
        setBackEventHandler()
        setOwnerChangeEventHandler()
        setGroupDeleteEventHandler()
        setObserve()
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

    private fun setObserve() {
        activityViewModel.isGroupDeleted.observe(viewLifecycleOwner) { isGroupDeleted ->
            if (isGroupDeleted == null) return@observe
            if (isGroupDeleted) {
                activity?.supportFragmentManager?.let { completeDialog.show(it, null) }
            } else {
                binding.root.context.showToast("알 수 없는 에러가 발생했습니다. 다음에 다시 시도해주세요.")
            }
        }
    }

    private fun setGroupDeleteEventHandler() {
        binding.btnDeleteGroup.setOnClickListener {
            activity?.supportFragmentManager?.let { dialog.show(it, null) }
        }
        dialog.setOnActionClickListener {
            activityViewModel.deleteGroup()
        }
    }
}
