package com.yapp.bol.presentation.view.mypage

import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myGroupAdapter: MyGroupAdapter by lazy { MyGroupAdapter() }

    override fun onViewCreatedAction() {
        binding.rvGroupList.adapter = myGroupAdapter
    }
}
