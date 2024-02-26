package com.yapp.bol.presentation.view.setting

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.yapp.bol.designsystem.ui.dialog.CancelAndActionDialog
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentQuitBinding
import com.yapp.bol.presentation.utils.collectWithLifecycle
import com.yapp.bol.presentation.utils.showToast
import com.yapp.bol.presentation.view.login.splash.SplashActivity
import com.yapp.bol.presentation.view.setting.group.GroupSettingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuitFragment : BaseFragment<FragmentQuitBinding>(R.layout.fragment_quit) {

    private val viewModel by viewModels<QuitViewModel>()

    override fun onViewCreatedAction() {
        super.onViewCreatedAction()
        setButton()
        observeQuitUiState()
    }

    private fun setButton() {
        binding.btnBack.setOnClickListener {
            binding.root.findNavController().popBackStack()
        }

        binding.btnQuit.setOnClickListener { openQuitDialog() }
    }

    private fun openQuitDialog() {
        activity?.supportFragmentManager?.let { fragmentManager ->
            makeQuitDialog().show(fragmentManager, null)
        }
    }

    private fun openFailQuitDialog(changeOwnerGroupId: Long, changeOwnerGroupName: String) {
        activity?.supportFragmentManager?.let { fragmentManager ->
            makeQuitFailDialog(changeOwnerGroupId, changeOwnerGroupName).show(fragmentManager, null)
        }
    }

    private fun makeQuitDialog(): CancelAndActionDialog {
        val quitDialog = CancelAndActionDialog.create {
            topMessage = resources.getString(R.string.delete_account_dialog_top)
            bottomMessage = resources.getString(R.string.delete_account_dialog_bottom)
            actionButtonText = resources.getString(R.string.delete_account_dialog_action)
        }

        quitDialog.setOnActionClickListener { viewModel.deleteAccount() }

        return quitDialog
    }

    private fun makeQuitFailDialog(changeOwnerGroupId: Long, changeOwnerGroupName: String): CancelAndActionDialog {
        val quitFailGuideTopMsg = String.format(
            resources.getString(R.string.delete_account_fail_dialog_top),
            changeOwnerGroupName
        )
        val quitFailGuideTopBoldMsg = listOf(
            resources.getString(R.string.delete_account_fail_dialog_top_bold),
            changeOwnerGroupName
        )
        val failDialog = CancelAndActionDialog.create {
            topMessage = quitFailGuideTopMsg
            boldStringsOfTopMessage = quitFailGuideTopBoldMsg
            bottomMessage = resources.getString(R.string.delete_account_fail_dialog_bottom)
            actionButtonText = resources.getString(R.string.delete_account_fail_dialog_action)
        }

        failDialog.setOnActionClickListener {
            GroupSettingActivity.startActivity(
                context = binding.root.context,
                groupId = changeOwnerGroupId,
                groupName = changeOwnerGroupName
            )
        }

        return failDialog
    }

    private fun moveSplashActivity() {
        startActivity(Intent(requireActivity(), SplashActivity::class.java))
        requireActivity().finish()
    }

    private fun observeQuitUiState() {
        viewModel.userUiState.collectWithLifecycle(this) { uiState ->
            when (uiState) {
                is SettingUiState.Loading -> {}

                is SettingUiState.Success -> {
                    moveSplashActivity()
                }

                is SettingUiState.UnknownError -> {
                    requireContext().showToast("현재 요청하신 작업을 수행할 수 없습니다. 다시 시도해 주십시오.")
                }

                is SettingUiState.FailCauseOwner -> {
                    openFailQuitDialog(uiState.groupId, uiState.groupName)
                }
            }
        }
    }
}
