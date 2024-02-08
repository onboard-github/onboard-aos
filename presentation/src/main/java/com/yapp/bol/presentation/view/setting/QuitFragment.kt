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

    private fun makeQuitDialog(): CancelAndActionDialog {
        val quitDialog = CancelAndActionDialog.create {
            topMessage = resources.getString(R.string.delete_account_dialog_top)
            bottomMessage = resources.getString(R.string.delete_account_dialog_bottom)
            actionButtonText = resources.getString(R.string.delete_account_dialog_action)
        }

        quitDialog.setOnActionClickListener { viewModel.deleteAccount() }

        return quitDialog
    }

    private fun makeQuitFailDialog(): CancelAndActionDialog {
        val failDialog = CancelAndActionDialog.create {
            topMessage = resources.getString(R.string.delete_account_fail_dialog_top)
            boldStringsOfTopMessage = listOf(resources.getString(R.string.delete_account_fail_dialog_top_bold))
            bottomMessage = resources.getString(R.string.delete_account_fail_dialog_bottom)
            actionButtonText = resources.getString(R.string.delete_account_fail_dialog_action)
        }

        failDialog.setOnActionClickListener {
            // todo 그룹 오너 이관 프로세스 구현 필요
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
                is SettingUiState.Loading -> {

                }

                is SettingUiState.Success -> {
                    moveSplashActivity()
                }

                is SettingUiState.Error -> {
                    requireContext().showToast("현재 요청하신 작업을 수행할 수 없습니다. 다시 시도해 주십시오.")
                }
            }
        }
    }
}
