package com.yapp.bol.presentation.view.setting.group

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentChangeOwnerBinding
import com.yapp.bol.presentation.utils.KeyboardManager
import com.yapp.bol.presentation.utils.textChangesToFlow
import com.yapp.bol.presentation.view.match.member_select.MembersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangeOwnerFragment : BaseFragment<FragmentChangeOwnerBinding>(R.layout.fragment_change_owner) {

    private val groupSettingViewModel: GroupSettingViewModel by activityViewModels()
    private val changeOwnerViewModel: ChangeOwnerViewModel by viewModels()

    private val groupId: Int by lazy {
        groupSettingViewModel.groupId.toInt()
    }

    private val keyboardManager by lazy {
        KeyboardManager(requireActivity())
    }

    private val membersAdapter = MembersAdapter(
        memberClickListener = { member, _, isChecked ->
            changeOwnerViewModel.updateMember(member.id, isChecked)
        },
        checkedMaxPlayer = { true },
    )

    override fun onViewCreatedAction() {
        super.onViewCreatedAction()

        binding.rvMembers.adapter = membersAdapter
        changeOwnerViewModel.getMembers(groupId = groupId)
        setViewModelObverse()

        viewLifecycleOwner.lifecycleScope.launch {
            val editTextFlow = binding.etSearchMember.textChangesToFlow()
            val debounceDuration = 300L

            editTextFlow.debounce(debounceDuration)
                .onEach {
                    val keyword = it.toString()

                    if (keyword.isNotEmpty()) binding.etSearchMember.requestFocus()
                    changeOwnerViewModel.clearNextPage()
                    changeOwnerViewModel.getMembers(getInputTextValue(), groupSettingViewModel.groupId.toInt())
                }
                .launchIn(this)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                getNextMember(recyclerView)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                keyboardManager.hideKeyboard()
            }
        }
        binding.rvMembers.addOnScrollListener(scrollListener)
    }
    private fun setViewModelObverse() = with(changeOwnerViewModel) {
        members.observe(viewLifecycleOwner) { members ->
            if (members == null) return@observe
            val isVisible = members.isEmpty()
            setSearchResultNothing(isVisible, getInputTextValue())
            membersAdapter.submitList(members)
        }
    }

    private fun getInputTextValue(): String {
        return binding.etSearchMember.text.toString()
    }

    private fun setSearchResultNothing(visible: Boolean, keyword: String) = with(binding) {
        val searchResult = String.format(resources.getString(R.string.search_result_nothing), keyword)
        tvSearchResultNothing.apply {
            text = searchResult
        }
        tvSearchResultNothing.isVisible = visible
    }

    private fun getNextMember(recyclerView: RecyclerView) {
        val newPagePointItemVisible =
            (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition() ?: 0

        val itemTotalCount = membersAdapter.itemCount - 10

        if (newPagePointItemVisible == itemTotalCount) {
            changeOwnerViewModel.getMembers(groupId = groupId)
        }
    }
}
