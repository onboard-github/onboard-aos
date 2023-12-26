package com.yapp.bol.presentation.view.mypage

import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myGroupAdapter: MyGroupAdapter by lazy { MyGroupAdapter() }

    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onViewCreatedAction() {
        binding.rvGroupList.adapter = myGroupAdapter

        myPageViewModel.joinedGroups.observe(viewLifecycleOwner) {
            myGroupAdapter.submitList(it)
        }

        myPageViewModel.userName.observe(viewLifecycleOwner) {
            binding.tvUserName.text = String.format(requireContext().resources.getString(R.string.mypage_user_name), it)
        }

        binding.ivSetting.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_myPageFragment_to_settingFragment2)
        }
    }
}
