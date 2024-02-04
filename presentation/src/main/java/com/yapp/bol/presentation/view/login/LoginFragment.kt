package com.yapp.bol.presentation.view.login

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.yapp.bol.presentation.BuildConfig
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentMainBinding
import com.yapp.bol.presentation.utils.collectWithLifecycle
import com.yapp.bol.presentation.utils.showToast
import com.yapp.bol.presentation.view.home.HomeActivity
import com.yapp.bol.presentation.view.login.auth.KakaoTestActivity
import com.yapp.bol.presentation.view.login.dialog.TermsDialog
import com.yapp.bol.presentation.view.login.auth.AuthViewModel
import com.yapp.bol.presentation.view.login.auth.LoginType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    private val authViewModel: AuthViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    private val googleLoginForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            handleGoogleSignInResult(result)
        }

    private val dialog by lazy {
        TermsDialog(
            context = requireContext(),
            onClickTermsListener = object : TermsDialog.OnClickTermsListener {
                override fun onClickLike(position: Int, isChecked: Boolean) {
                    loginViewModel.updateLike(position, isChecked)
                }

                override fun onClickLikeAll(isChecked: Boolean) {
                    loginViewModel.updateAllLike(isChecked)
                }

                override fun onClickSignUp() {
                    moveSignUp()
                }

                override fun onClickTermsDetail(url: String) {
                    moveTermsDetail(url)
                }

                override fun checkedTermsAll(state: Boolean): Boolean {
                    return loginViewModel.checkedTermsAll(state)
                }

                override fun dismissAction(state: Boolean) {
                    loginViewModel.updateDialogState(state)
                }
            },
        )
    }

    override fun onViewCreatedAction() {
        super.onViewCreatedAction()
        setButtonListener()
        subscribeObservables()
        subscribeGoogleLoginObservables()
    }

    private fun subscribeObservables() = with(loginViewModel) {
        onboardState.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            if (it.onBoarding.isEmpty() && it.mainGroupId != null) {
                HomeActivity.startActivity(binding.root.context, it.mainGroupId.toLong())
                requireActivity().finish()
                return@observe
            }

            if (it.onBoarding.isEmpty()) {
                moveGroupSearch()
                return@observe
            }

            when (it.onBoarding.first()) { // TODO : 현재 배열로 되어있어서 첫번째 부분 확인, 추후 서버분들에게 시작 포인트 내려달라고 요청 예정
                ONBOARD_TERMS -> loginViewModel.getTerms()
                UPDATE_TERMS -> {
                    // TODO : 약관 업데이트 UI 로 이동
                }
                ONBOARD_NICKNAME -> moveSignUp()
                JOIN_GROUP -> moveGroupSearch()
            }
        }

        termsList.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            dialog.termsAdapter.submitList(it)
        }

        dialogState.observe(viewLifecycleOwner) {
            if (it.not()) return@observe
            dialog.show()
        }

        isEnableSignUp.observe(viewLifecycleOwner) {
            if (dialog.isShowing.not()) return@observe
            dialog.updateSignUpEnabled(it)
        }
    }

    private fun setButtonListener() {
        binding.btnGoogle.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            binding.tvLoading.visibility = View.VISIBLE
            startGoogleLogin()
        }

        binding.btnKakao.setOnClickListener {
            Intent(requireActivity(), KakaoTestActivity::class.java).also { startActivity(it) }
            requireActivity().finish()
        }

        binding.btnApple.setOnClickListener {
            // Intent(requireActivity(), NaverTestActivity::class.java).also { startActivity(it) }
            requireContext().showToast("준비중입니다.")
        }
    }

    private fun moveSignUp() {
        loginViewModel.postTerms()
        findNavController().navigate(R.id.action_mainFragment_to_signUpNicknameFragment)
    }

    private fun moveGroupSearch() {
        loginViewModel.postTerms()
        HomeActivity.startActivity(binding.root.context, null)
        requireActivity().finish()
    }

    private fun moveTermsDetail(url: String) {
        val intent = Intent(requireActivity(), TermsWebViewActivity::class.java).apply {
            putExtra(WEB_VIEW_KEY, url)
        }
        startActivity(intent)
    }

    private fun startGoogleLogin() {
        val apiKey = if (BuildConfig.DEBUG) {
            BuildConfig.GOOGLE_LOGIN_API_KEY_SANDBOX
        } else {
            BuildConfig.GOOGLE_LOGIN_API_KEY
        }
        val googleLoginClientIntent: Intent = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(apiKey)
            .requestEmail()
            .build().let {
                GoogleSignIn.getClient(requireActivity(), it).signInIntent
            }

        googleLoginForResult.launch(googleLoginClientIntent)
    }

    private fun handleGoogleSignInResult(result: ActivityResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            binding.pbLoading.visibility = View.GONE
            binding.tvLoading.visibility = View.GONE
            try {
                val account = GoogleSignIn
                    .getSignedInAccountFromIntent(result.data)
                    .getResult(ApiException::class.java)
                val token = account.idToken
                requireNotNull(token)

                authViewModel.login(LoginType.GOOGLE, token)
            } catch (e: Exception) {
                binding.pbLoading.visibility = View.GONE
                binding.tvLoading.visibility = View.GONE
                requireContext().showToast("구글 로그인 중 오류가 발생했습니다. 다시 시도해 주세요.")
            }
        } else {
            binding.pbLoading.visibility = View.GONE
            binding.tvLoading.visibility = View.GONE
            requireContext().showToast("구글 로그인 중 오류가 발생했습니다. 다시 시도해 주세요.")
        }
    }

    private fun subscribeGoogleLoginObservables() {
        authViewModel.loginResult.collectWithLifecycle(this) {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
    companion object {
        const val ONBOARD_TERMS = "TERMS"
        const val ONBOARD_NICKNAME = "NICKNAME"
        const val JOIN_GROUP = "JOIN_GROUP"
        const val UPDATE_TERMS = "UPDATE_TERMS"
        const val WEB_VIEW_KEY = "web view"
    }
}
