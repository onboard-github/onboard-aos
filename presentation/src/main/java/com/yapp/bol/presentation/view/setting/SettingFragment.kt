package com.yapp.bol.presentation.view.setting

import android.content.Intent
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentSettingBinding
import com.yapp.bol.presentation.utils.collectWithLifecycle
import com.yapp.bol.presentation.utils.navigateFragment
import com.yapp.bol.presentation.utils.sendMailToHelpAddress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    private val termViewModel: TermViewModel by viewModels()
    private val settingViewModel: SettingViewModel by viewModels()
    private var privacyUrl: String = ""
    private var serviceUrl: String = ""

    override fun onViewCreatedAction() {
        super.onViewCreatedAction()
        initDataInViewModel()
        setNavigateButton()
        setBackButton()
        subscribeObservables()
    }

    private fun initDataInViewModel() {
        settingViewModel.getUserInfo()
    }

    private fun subscribeObservables() {
        termViewModel.termStateFlow.collectWithLifecycle(this) {
            privacyUrl = it.privacy
            serviceUrl = it.service
        }
    }

    private fun setNavigateButton() {
        binding.apply {
            btnProfileSetting.setOnClickListener {
                findNavController().navigateFragment(R.id.action_settingFragment_to_userSettingFragment)
            }

            btnHelp.setOnClickListener { sendEmail() }

            btnQuit.setOnClickListener {
                findNavController().navigateFragment(R.id.action_settingFragment_to_quitFragment)
            }
            btnTermsPrivacy.setOnClickListener {
                TermFragment.startFragment(
                    findNavController(),
                    isPrivacy = true,
                    termUrl = privacyUrl,
                )
            }
            btnTermsService.setOnClickListener {
                TermFragment.startFragment(
                    findNavController(),
                    isPrivacy = false,
                    termUrl = serviceUrl,
                )
            }
            btnOpenSource.setOnClickListener {
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
            }
        }
    }

    private fun sendEmail() {
        val string = binding.root.resources.getString(R.string.help_email_content)
        val content = String.format(string, settingViewModel.id, settingViewModel.nickName)
        binding.root.context.sendMailToHelpAddress("온보드 문의", content)
    }

    private fun setBackButton() {
        binding.btnBack.setOnClickListener {
            requireActivity().finish()
        }
    }
}
