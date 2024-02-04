package com.yapp.bol.presentation.view.mypage

import android.content.Intent
import androidx.fragment.app.viewModels
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myGroupAdapter: MyGroupAdapter by lazy {
        MyGroupAdapter { groupId, groupName, nickname, memberId ->
            ProfileSettingActivity.startActivity(
                context = binding.root.context,
                groupId = groupId,
                groupName = groupName,
                nickname = nickname,
                memberId = memberId
            )
        }
    }

    private val myPageViewModel: MyPageViewModel by viewModels()

    override fun onViewCreatedAction() {
        binding.rvGroupList.adapter = myGroupAdapter

        myPageViewModel.joinedGroups.observe(viewLifecycleOwner) {
            myGroupAdapter.submitList(it)
        }

        myPageViewModel.userName.observe(viewLifecycleOwner) {
            binding.tvUserName.text = String.format(requireContext().resources.getString(R.string.mypage_user_name), it)
        }

        myPageViewModel.matchTotalCount.observe(viewLifecycleOwner) {
            binding.tvMatchCount.text = DecimalFormat("#,###").format(it)
        }

        binding.ivSetting.setOnClickListener {
            startActivity(Intent(requireActivity(), SettingActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        myPageViewModel.getMyInfo()
        myPageViewModel.getJoinedGroup()
    }
}
