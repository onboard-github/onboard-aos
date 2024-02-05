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
import com.yapp.bol.designsystem.ui.dialog.CancelAndActionDialog
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseFragment
import com.yapp.bol.presentation.databinding.FragmentHomeRankBinding
import com.yapp.bol.presentation.model.DrawerGroupInfoUiModel
import com.yapp.bol.presentation.model.GroupQuitUiModel
import com.yapp.bol.presentation.model.UserRankUiModel
import com.yapp.bol.presentation.utils.collectWithLifecycle
import com.yapp.bol.presentation.utils.copyToClipboard
import com.yapp.bol.presentation.utils.setStatusBarColor
import com.yapp.bol.presentation.utils.showToast
import com.yapp.bol.presentation.view.group.GroupDiscoveryActivity
import com.yapp.bol.presentation.view.home.HomeUiState
import com.yapp.bol.presentation.view.home.HomeViewModel
import com.yapp.bol.presentation.view.home.rank.UserRankViewModel.Companion.RV_SELECTED_POSITION_RESET
import com.yapp.bol.presentation.view.home.rank.change_group.GroupChangeDialog
import com.yapp.bol.presentation.view.home.rank.game.UserRankGameAdapter
import com.yapp.bol.presentation.view.home.rank.game.UserRankGameLayoutManager
import com.yapp.bol.presentation.view.home.rank.group_info.DrawerGroupInfoAdapter
import com.yapp.bol.presentation.view.home.rank.user.UserRankAdapter
import com.yapp.bol.presentation.view.login.splash.SplashActivity
import com.yapp.bol.presentation.view.match.MatchActivity
import com.yapp.bol.presentation.view.setting.UpgradeActivity
import com.yapp.bol.presentation.view.setting.group.GroupSettingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import com.yapp.bol.designsystem.R as designsystemR

@AndroidEntryPoint
class HomeRankFragment : BaseFragment<FragmentHomeRankBinding>(R.layout.fragment_home_rank) {

    private val viewModel: UserRankViewModel by viewModels()
    private val activityViewModel: HomeViewModel by activityViewModels()

    private lateinit var drawerGroupInfoAdapter: DrawerGroupInfoAdapter
    private lateinit var userRankGameAdapter: UserRankGameAdapter
    private val userRankAdapter by lazy { UserRankAdapter() }
    private val groupChangeDialog by lazy {
        GroupChangeDialog(
            onGroupClick = {
                activityViewModel.groupId = it
                viewModel.fetchAll(it)
            },
            onSearchGroupClick = {
                moveToGroupDiscovery()
            }
        )
    }

    override fun onViewCreatedAction() {
        super.onViewCreatedAction()

        setHomeRecyclerView()
        setDrawer()
        observeGameAndGroupUiState(drawerGroupInfoAdapter, userRankGameAdapter)
        observeJoinedGroupUiState(groupChangeDialog)
        observeUserRankUiState(userRankAdapter)
        observeOwnerCheckUiState()

        setStatusBarColor(this@HomeRankFragment.requireActivity(), designsystemR.color.Gray_15, isIconBlack = false)

        scrollCenterWhenUserRankTouchDown()

        setFloatingButton()
        setGroupNameButton()
        setGroupSearchButton()
    }

    override fun onStart() {
        super.onStart()
        initGroupData()
    }

    private fun initGroupData() {
        initUi(activityViewModel.groupId != null)
        activityViewModel.groupId?.let {
            viewModel.fetchAll(
                initGroupId = activityViewModel.groupId,
                initGameId = activityViewModel.gameId
            )
        }
    }

    private fun initUi(isGroupIdExist: Boolean) {
        binding.apply {
            viewRankLoading.isVisible = isGroupIdExist
            rvUserRank.isVisible = isGroupIdExist
            rvGameList.visibility = if (isGroupIdExist) { View.VISIBLE } else { View.INVISIBLE }
            viewNoJoinedGroup.root.isVisible = !isGroupIdExist
            viewRankLoading.isVisible = isGroupIdExist
            loadingGroupName.isVisible = isGroupIdExist
            btnGroupName.isVisible = isGroupIdExist
            btnMeatBall.isVisible = isGroupIdExist
            btnCreateGroup.isVisible = isGroupIdExist
        }
    }

    private fun moveToGroupDiscovery() {
        // todo 잘 돌아가는지 체크 필요
        Intent(binding.root.context, GroupDiscoveryActivity::class.java).apply {
            startActivity(this)
        }
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
    }

    private fun setDrawer() {
        setDrawerOpen()
        setDrawerAdapter()
        setDrawerGroupSettingButton()
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

    private fun setDrawerGroupSettingButton() {
        binding.viewHeader.btnGroupSetting.setOnClickListener {
            // todo group setting 연결
        }
    }

    private fun bindGroupQuitButton() {
        binding.viewFooter.llBtnQuit.setOnClickListener {
            activity?.supportFragmentManager?.let { makeQuitDialog().show(it, null) }
        }
    }

    private fun makeQuitDialog(): CancelAndActionDialog {
        val quitDialog = CancelAndActionDialog.create {
            topMessage = resources.getString(R.string.group_quit_dialog_top)
            bottomMessage = String.format(
                resources.getString(R.string.group_quit_dialog_bottom), viewModel.currentGroupName
            )
            boldStringOfBottomMessage = listOf(viewModel.currentGroupName)
            actionButtonText = "나가기"
        }
        quitDialog.setOnActionClickListener {
            viewModel.quitGroup()
            val fragmentManager = activity?.supportFragmentManager
            viewModel.groupQuitUiState.collectWithLifecycle(this) {
                when (it) {
                    is GroupQuitUiModel.Success -> {
                        this.cancel()
                        val intent = Intent(binding.root.context, SplashActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    is GroupQuitUiModel.FailCauseOwner -> {
                        this.cancel()
                        val dialog = makeFailCause(GroupQuitFailCase.Owner) {
                            // todo 이동하기 로직 연결
                        }
                        fragmentManager?.let { manager -> dialog.show(manager, null) }
                    }
                    is GroupQuitUiModel.FailUnknownError -> {
                        this.cancel()
                        binding.root.context.showToast("알 수 없는 에러가 발생했습니다. 다음에 다시 시도해주세요.")
                    }
                    is GroupQuitUiModel.FailCauseOnlyOneMember -> {
                        this.cancel()
                        val dialog = makeFailCause(GroupQuitFailCase.OnlyMember) {
                            // todo 이동하기 로직 연결
                        }
                        fragmentManager?.let { manager -> dialog.show(manager, null) }
                    }
                    is GroupQuitUiModel.Loading -> {}
                }
            }
        }
        return quitDialog
    }

    private fun makeFailCause(case: GroupQuitFailCase, onAction: () -> Unit): CancelAndActionDialog {
        val dialog = CancelAndActionDialog.create {
            topMessage = resources.getString(case.dialogTopMessageId)
            boldStringsOfTopMessage = listOf(resources.getString(case.dialogTopBoldMessageId))
            bottomMessage = resources.getString(case.dialogBottomMessageId)
            actionButtonText = "이동하기"
        }
        dialog.setOnActionClickListener {
            onAction.invoke()
        }
        return dialog
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

    private fun isNoJoinedGroup() = activityViewModel.groupId == null

    private val userRankSnackBar: SnackBarHomeReload by lazy {
        SnackBarHomeReload.make(
            view = binding.root,
            onClick = { viewModel.fetchUserList() }
        )
    }

    private fun observeUserRankUiState(userRankAdapter: UserRankAdapter) {
        viewModel.userUiState.collectWithLifecycle(this) { uiState ->
            if (isNoJoinedGroup()) { return@collectWithLifecycle }
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
            onClick = { viewModel.fetchAll(initGroupId = activityViewModel.groupId) }
        )
    }

    private fun observeGameAndGroupUiState(
        drawerGroupInfoAdapter: DrawerGroupInfoAdapter,
        userRankGameAdapter: UserRankGameAdapter,
    ) {
        viewModel.gameUiState.collectWithLifecycle(this) { uiState ->
            if (isNoJoinedGroup()) { return@collectWithLifecycle }
            when (uiState) {
                is HomeUiState.Success -> {
                    userRankGameAdapter.submitList(uiState.data)
                    binding.rvGameList.visibility = View.VISIBLE
                }

                is HomeUiState.Loading -> {
                    binding.rvGameList.visibility = View.INVISIBLE
                }

                is HomeUiState.Error -> {
                    if (uiState.error.message == "FORCE_UPDATE") {
                        UpgradeActivity.startActivity(requireContext())
                        requireActivity().finish()
                    }
                    gameSnackBar.show()
                }
            }
        }

        viewModel.currentGroupUiState.collectWithLifecycle(this) { uiState ->
            if (isNoJoinedGroup()) { return@collectWithLifecycle }
            when (uiState) {
                is HomeUiState.Success -> {
                    uiState.data.map { uiModel ->
                        if (uiModel is DrawerGroupInfoUiModel.CurrentGroupInfo) {
                            setCurrentGroupInfo(uiModel)
                        }
                    }
                    drawerGroupInfoAdapter.submitList(uiState.data)

                    binding.tvGroupName.visibility = View.VISIBLE
                    binding.loadingGroupName.visibility = View.INVISIBLE
                }

                is HomeUiState.Loading -> {
                    binding.tvGroupName.visibility = View.INVISIBLE
                    binding.loadingGroupName.visibility = View.VISIBLE
                }

                is HomeUiState.Error -> {
                    if (uiState.error.message == "FORCE_UPDATE") {
                        UpgradeActivity.startActivity(requireContext())
                        requireActivity().finish()
                    }
                    gameSnackBar.show()
                }
            }
        }
    }

    private fun observeJoinedGroupUiState(groupChangeDialog: GroupChangeDialog) {
        viewModel.joinedGroupUiState.collectWithLifecycle(this) { uiState ->
            when (uiState) {
                is HomeUiState.Success -> {
                    groupChangeDialog.submitGroupList(uiState.data)
                    setGroupNameButtonEnable(true)
                }
                is HomeUiState.Loading -> { setGroupNameButtonEnable(false) }
                is HomeUiState.Error -> { setGroupNameButtonEnable(false) }
            }
        }
    }

    private fun observeOwnerCheckUiState() {
        viewModel.ownerCheckUiState.collectWithLifecycle(this) { uiState ->
            when (uiState) {
                is HomeUiState.Success -> { binding.viewHeader.btnGroupSetting.isVisible = uiState.data }
                is HomeUiState.Loading -> { binding.viewHeader.btnGroupSetting.isVisible = false }
                is HomeUiState.Error -> { binding.viewHeader.btnGroupSetting.isVisible = false }
            }
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
            activity?.supportFragmentManager?.let {
                if (!groupChangeDialog.isAdded) { groupChangeDialog.show(it, null) }
            }
        }
    }

    private fun setGroupNameButtonEnable(isEnable: Boolean) {
        binding.btnGroupName.isClickable = isEnable
    }

    private fun setGroupSearchButton() {
        binding.viewNoJoinedGroup.btnGroupSearch.setOnClickListener {
            moveToGroupDiscovery()
        }
    }

    companion object {
        const val GROUP_ID = "group id"
    }
}
