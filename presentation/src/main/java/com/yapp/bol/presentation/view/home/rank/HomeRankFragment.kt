package com.yapp.bol.presentation.view.home.rank

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.yapp.bol.designsystem.ui.dialog.MessageCancelConfirmDialog
import com.yapp.bol.designsystem.ui.dialog.MessageConfirmDialog
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentHomeRankBinding
import com.yapp.bol.presentation.model.DrawerGroupInfoUiModel
import com.yapp.bol.presentation.model.UserRankUiModel
import com.yapp.bol.presentation.utils.collectWithLifecycle
import com.yapp.bol.presentation.utils.copyToClipboard
import com.yapp.bol.presentation.utils.setStatusBarColor
import com.yapp.bol.presentation.utils.showToast
import com.yapp.bol.presentation.view.home.HomeUiState
import com.yapp.bol.presentation.view.home.HomeViewModel
import com.yapp.bol.presentation.view.home.rank.UserRankViewModel.Companion.RV_SELECTED_POSITION_RESET
import com.yapp.bol.presentation.view.home.rank.game.UserRankGameAdapter
import com.yapp.bol.presentation.view.home.rank.game.UserRankGameLayoutManager
import com.yapp.bol.presentation.view.home.rank.group_info.DrawerGroupInfoAdapter
import com.yapp.bol.presentation.view.home.rank.user.UserRankAdapter
import com.yapp.bol.presentation.view.match.MatchActivity
import com.yapp.bol.presentation.view.setting.UpgradeActivity
import dagger.hilt.android.AndroidEntryPoint
import com.yapp.bol.designsystem.R as designsystemR

@AndroidEntryPoint
class HomeRankFragment : BaseFragment<FragmentHomeRankBinding>(R.layout.fragment_home_rank) {

    private val viewModel: UserRankViewModel by viewModels()
    private val activityViewModel: HomeViewModel by activityViewModels()

    private lateinit var drawerGroupInfoAdapter: DrawerGroupInfoAdapter
    private lateinit var userRankGameAdapter: UserRankGameAdapter

    override fun onViewCreatedAction() {
        super.onViewCreatedAction()

        initViewModel()

        setHomeRecyclerView()
        setDrawer()
        observeGameAndGroupUiState(drawerGroupInfoAdapter, userRankGameAdapter)

        setStatusBarColor(this@HomeRankFragment.requireActivity(), designsystemR.color.Gray_15, isIconBlack = false)

        scrollCenterWhenUserRankTouchDown()

        setFloatingButton()
        setGroupNameButton()
    }

    override fun onStart() {
        super.onStart()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.fetchAll(
            initGroupId = activityViewModel.groupId,
            initGameId = activityViewModel.gameId
        )
    }

    private fun setHomeRecyclerView() {
        setGameAdapter()
        setUserAdapter()
    }

    private fun setGameAdapter() {
        val onClick: (position: Int, gameId: Long) -> Unit = { position, gameId ->
            viewModel.setGameItemSelected(position)
            viewModel.fetchUserList(gameId)
        }
        val scrollAnimation: () -> Unit = {
            binding.rvGameList.smoothScrollToPosition(viewModel.getGameItemSelectedPosition())
        }
        userRankGameAdapter = UserRankGameAdapter(onClick, scrollAnimation)

        binding.rvGameList.apply {
            layoutManager = UserRankGameLayoutManager(
                binding.rvGameList.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = userRankGameAdapter
        }
    }

    private fun setUserAdapter() {
        val userRankAdapter = UserRankAdapter()
        val rvUserRank = binding.rvUserRank

        userRankAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                rvUserRank.scrollToPosition(0)
            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                rvUserRank.scrollToPosition(0)
            }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                rvUserRank.scrollToPosition(0)
            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                rvUserRank.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                rvUserRank.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
                rvUserRank.scrollToPosition(0)
            }
        })

        rvUserRank.adapter = userRankAdapter
        observeUserRankUiState(userRankAdapter)
    }

    private fun setDrawer() {
        setDrawerOpen()
        setDrawerAdapter()
        bindGroupQuitButton()
    }

    private fun setDrawerOpen() {
        binding.btnMeatBall.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }
    }

    private fun setDrawerAdapter() {
        // todo : 마이페이지 그룹 프로필 수정 화면으로 바로 이동해주어야 할듯.
        val otherGroupOnClick: (Long) -> Unit = { id ->
            activityViewModel.groupId = id
            viewModel.apply {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
                fetchAll(initGroupId = id)
            }
        }

        val copyButtonOnClick: (String) -> Unit = {
            it.copyToClipboard(binding.root.context)
            showToastForAndroid13Below()
        }

        drawerGroupInfoAdapter = DrawerGroupInfoAdapter(
            userProfileEditOnClick = otherGroupOnClick,
            copyButtonOnClick = copyButtonOnClick,
        )
        binding.rvGroupInfo.adapter = drawerGroupInfoAdapter
    }

    private fun bindGroupQuitButton() {
        binding.viewFooter.llBtnQuit.setOnClickListener {
            makeQuitDialog().show()
        }
    }

    private fun makeQuitDialog(): MessageCancelConfirmDialog {
        return MessageCancelConfirmDialog(
            context = binding.root.context,
            originalMessage = String.format(
                resources.getString(R.string.group_quit_dialog), viewModel.currentGroupName
            ),
            boldStringFromOriginal = listOf(viewModel.currentGroupName),
            confirmMessage = "나가기",
        ).apply { setOnConfirmClick { groupQuitFailDialog().show() } }
    }

    private fun groupQuitFailDialog(): MessageConfirmDialog {
        return MessageConfirmDialog(
            context = binding.root.context,
            originalMessage = resources.getString(R.string.group_quit_fail_cause_owner),
            boldStringFromOriginal = listOf(resources.getString(R.string.group_quit_fail_cause_owner_bold)),
            confirmMessage = "나가기",
        )
    }

    private fun setCurrentGroupInfo(currentGroupInfo: DrawerGroupInfoUiModel.CurrentGroupInfo) {
        binding.apply {
            val item = currentGroupInfo.groupDetailItem
            viewHeader.tvGroupName.text = item.name
            tvGroupName.text = item.name
            viewRankNotFound.tvCode.text = item.accessCode
            viewRankNotFound.btnCodeCopy.isVisible = item.accessCode != null
            item.accessCode?.let { code ->
                viewRankNotFound.btnCodeCopy.setOnClickListener {
                    code.copyToClipboard(root.context)
                    showToastForAndroid13Below()
                }
            }
        }
    }

    private val userRankSnackBar: SnackBarHomeReload by lazy {
        SnackBarHomeReload.make(
            view = binding.root,
            onClick = { viewModel.fetchUserList() }
        )
    }

    private fun observeUserRankUiState(userRankAdapter: UserRankAdapter) {
        viewModel.userUiState.collectWithLifecycle(this) { uiState ->
            fun List<UserRankUiModel>.isNoRank(): Boolean = this.isEmpty()

            when (uiState) {
                is HomeUiState.Success -> {
                    userRankSnackBar.dismiss()
                    val isNoRank: Boolean = uiState.data.isNoRank()

                    if (!isNoRank) { userRankAdapter.submitList(uiState.data) }

                    binding.apply {
                        if (isSelectedPositionValid()) {
                            rvGameList.smoothScrollToPosition(viewModel.getGameItemSelectedPosition())
                        }
                        viewRankLoading.visibility = View.GONE
                        viewRankNotFound.root.visibility = if (isNoRank) { View.VISIBLE } else { View.GONE }
                        rvUserRank.visibility = if (isNoRank) { View.GONE } else { View.VISIBLE }
                        rvUserRank.smoothScrollToPosition(0)
                    }
                }

                is HomeUiState.Loading -> {
                    binding.apply {
                        rvUserRank.visibility = View.GONE
                        viewRankNotFound.root.visibility = View.GONE
                        viewRankLoading.visibility = View.VISIBLE
                    }
                }

                is HomeUiState.Error -> {
                    if (uiState.error.message == "FORCE_UPDATE") {
                        UpgradeActivity.startActivity(requireContext())
                        requireActivity().finish()
                    }
                    userRankSnackBar.show()
                }
            }
        }
    }

    private val gameSnackBar: SnackBarHomeReload by lazy {
        SnackBarHomeReload.make(
            view = binding.root,
            onClick = { viewModel.fetchAll() }
        )
    }

    private fun observeGameAndGroupUiState(
        drawerGroupInfoAdapter: DrawerGroupInfoAdapter,
        userRankGameAdapter: UserRankGameAdapter,
    ) {
        var gameSnackBarNeedToShow: Boolean = false

        viewModel.gameUiState.collectWithLifecycle(this) { uiState ->
            when (uiState) {
                is HomeUiState.Success -> {
                    userRankGameAdapter.submitList(uiState.data)
                    binding.rvGameList.visibility = View.VISIBLE
                }

                is HomeUiState.Loading -> {
                    binding.rvGameList.visibility = View.GONE
                }

                is HomeUiState.Error -> {
                    if (uiState.error.message == "FORCE_UPDATE") {
                        UpgradeActivity.startActivity(requireContext())
                        requireActivity().finish()
                    }
                    gameSnackBarNeedToShow = true
                }
            }
        }

        viewModel.currentGroupUiState.collectWithLifecycle(this) { uiState ->
            when (uiState) {
                is HomeUiState.Success -> {
                    uiState.data.map { uiModel ->
                        if (uiModel is DrawerGroupInfoUiModel.CurrentGroupInfo) {
                            setCurrentGroupInfo(uiModel)
                        }
                    }
                    drawerGroupInfoAdapter.submitList(uiState.data)

                    binding.btnGroupName.visibility = View.VISIBLE
                    binding.loadingGroupName.visibility = View.INVISIBLE
                }

                is HomeUiState.Loading -> {
                    binding.btnGroupName.visibility = View.INVISIBLE
                    binding.loadingGroupName.visibility = View.VISIBLE
                }

                is HomeUiState.Error -> {
                    if (uiState.error.message == "FORCE_UPDATE") {
                        UpgradeActivity.startActivity(requireContext())
                        requireActivity().finish()
                    }
                    gameSnackBarNeedToShow = false
                }
            }
        }

        when (gameSnackBarNeedToShow) {
            true -> gameSnackBar.show()
            false -> gameSnackBar.dismiss()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun scrollCenterWhenUserRankTouchDown() {
        binding.rvUserRank.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState != SCROLL_STATE_IDLE && isSelectedPositionValid()) {
                    binding.rvGameList.smoothScrollToPosition(viewModel.getGameItemSelectedPosition())
                }
            }
        })
    }

    private fun showToastForAndroid13Below() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            binding.root.context.showToast(R.string.copy_access_code, Toast.LENGTH_SHORT)
        }
    }

    private fun isSelectedPositionValid(): Boolean {
        return RV_SELECTED_POSITION_RESET != viewModel.getGameItemSelectedPosition()
    }

    private fun setFloatingButton() {
        binding.btnCreateGroup.setOnClickListener {
            Intent(requireContext(), MatchActivity::class.java).also {
                it.putExtra(GROUP_ID, viewModel.groupId.toInt())
                startActivity(it)
            }
        }
    }

    private fun setGroupNameButton() {
        binding.btnGroupName.setOnClickListener {
            // todo : 그룹 다이얼로그 오픈으로 수정
        }
    }

    companion object {
        const val GROUP_ID = "group id"
    }
}
