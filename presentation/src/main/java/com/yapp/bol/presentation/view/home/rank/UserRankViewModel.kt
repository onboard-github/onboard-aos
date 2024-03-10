package com.yapp.bol.presentation.view.home.rank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.model.Role
import com.yapp.bol.domain.model.core.BaseStateItem
import com.yapp.bol.domain.model.user.GroupQuitItem
import com.yapp.bol.domain.usecase.group.GetGroupDetailUseCase
import com.yapp.bol.domain.usecase.group.GetJoinedGroupUseCase
import com.yapp.bol.domain.usecase.group.QuitGroupUseCase
import com.yapp.bol.domain.usecase.login.GetMyInfoUseCase
import com.yapp.bol.domain.usecase.rank.AddGuestMemberUseCase
import com.yapp.bol.domain.usecase.rank.GetUserRankGameListUseCase
import com.yapp.bol.domain.usecase.rank.GetUserRankUseCase
import com.yapp.bol.presentation.mapper.HomeMapper.getMyInfo
import com.yapp.bol.presentation.mapper.HomeMapper.toHomeGameItemUiModelList
import com.yapp.bol.presentation.mapper.HomeMapper.toMyGroupProfileInfo
import com.yapp.bol.presentation.mapper.HomeMapper.toOtherGroupInfo
import com.yapp.bol.presentation.mapper.HomeMapper.toUserRankUiModel
import com.yapp.bol.presentation.model.DrawerGroupInfoUiModel
import com.yapp.bol.presentation.model.GameItemWithSelected
import com.yapp.bol.presentation.model.GroupQuitUiModel
import com.yapp.bol.presentation.model.HomeGameItemUiModel
import com.yapp.bol.presentation.model.JoinedGroupUiModel
import com.yapp.bol.presentation.model.NicknameValidationUiModel
import com.yapp.bol.presentation.model.UserRankUiModel
import com.yapp.bol.presentation.utils.checkedApiResult
import com.yapp.bol.presentation.utils.config.HomeConfig.USER_RANK_LOAD_FORCE_DELAY
import com.yapp.bol.presentation.view.home.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserRankViewModel @Inject constructor(
    private val getUserRankGameListUseCase: GetUserRankGameListUseCase,
    private val getUserRankUseCase: GetUserRankUseCase,
    private val getJoinedGroupUseCase: GetJoinedGroupUseCase,
    private val getGroupDetailUseCase: GetGroupDetailUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val quitGroupUseCase: QuitGroupUseCase,
    private val addGuestMemberUseCase: AddGuestMemberUseCase,
) : ViewModel() {

    private var userListFetchJob: Job? = null

    private var selectedPosition: Int = RV_SELECTED_POSITION_RESET

    private val _userUiState = MutableStateFlow<HomeUiState<List<UserRankUiModel>>>(HomeUiState.Loading)
    val userUiState: StateFlow<HomeUiState<List<UserRankUiModel>>> = _userUiState

    private val _joinedGroupUiState = MutableStateFlow<HomeUiState<List<JoinedGroupUiModel>>>(HomeUiState.Loading)
    val joinedGroupUiState: StateFlow<HomeUiState<List<JoinedGroupUiModel>>> = _joinedGroupUiState

    private val _currentGroupUiState = MutableStateFlow<HomeUiState<List<DrawerGroupInfoUiModel>>>(HomeUiState.Loading)
    val currentGroupUiState: StateFlow<HomeUiState<List<DrawerGroupInfoUiModel>>> = _currentGroupUiState

    private val _gameUiState = MutableStateFlow<HomeUiState<List<HomeGameItemUiModel>>>(HomeUiState.Loading)
    val gameUiState: StateFlow<HomeUiState<List<HomeGameItemUiModel>>> = _gameUiState

    private val _ownerCheckUiState = MutableStateFlow<HomeUiState<Boolean>>(HomeUiState.Loading)
    val ownerCheckUiState: StateFlow<HomeUiState<Boolean>> = _ownerCheckUiState

    private val _groupQuitUiState = MutableStateFlow<GroupQuitUiModel>(GroupQuitUiModel.Loading)
    val groupQuitUiState: StateFlow<GroupQuitUiModel> = _groupQuitUiState

    private val _checkNicknameValidation = MutableStateFlow<NicknameValidationUiModel>(NicknameValidationUiModel.Loading)
    val checkNicknameValidation: StateFlow<NicknameValidationUiModel> = _checkNicknameValidation

    private val _checkAddingGuestCompleted = MutableStateFlow<HomeUiState<Boolean>>(HomeUiState.Loading)
    val checkAddingGuestCompleted: StateFlow<HomeUiState<Boolean>> = _checkAddingGuestCompleted

    var groupId: Long = GAME_USER_ID_TO_BE_SET
    var gameId: Long = GAME_USER_ID_TO_BE_SET
    var myId: Long = GAME_USER_ID_TO_BE_SET
    var nickName: String = ""
    var currentGroupName: String = ""

    fun setGameItemSelected(newPosition: Int) {
        val gameUiList: MutableList<HomeGameItemUiModel> = gameUiState.value._data?.toMutableList() ?: return

        val beforePosition = selectedPosition

        val beforeItem = gameUiList.getOrNull(beforePosition) as? HomeGameItemUiModel.GameItem
        beforeItem?.let {
            gameUiList[beforePosition] = HomeGameItemUiModel.GameItem(
                GameItemWithSelected(it.item.gameItem, false)
            )
        }

        val newItem = gameUiList.getOrNull(newPosition) as? HomeGameItemUiModel.GameItem
        newItem?.let {
            gameUiList[newPosition] = HomeGameItemUiModel.GameItem(
                GameItemWithSelected(it.item.gameItem, true)
            )
        }

        selectedPosition = newPosition
        _gameUiState.value = HomeUiState.Success(gameUiList)
    }

    fun fetchAll(initGroupId: Long?, initGameId: Long? = null) {
        initGroupId?.let {
            groupId = it
            initGameId?.let { id -> gameId = id }
            fetchAllFromServer(it, initGameId)
        }
    }

    private fun fetchAllFromServer(groupId: Long, initGameId: Long? = null) {
        viewModelScope.launch {
            _gameUiState.value = HomeUiState.Loading
            _currentGroupUiState.value = HomeUiState.Loading
            _joinedGroupUiState.value = HomeUiState.Loading

            val startPadding = 1

            val gameItemFlow = getUserRankGameListUseCase(groupId = groupId.toInt())
            val currentGroupFlow = getGroupDetailUseCase(groupId)
            val joinedGroupFlow = getJoinedGroupUseCase()
            val myInfoFlow = getMyInfoUseCase()

            val game: MutableList<HomeGameItemUiModel> = mutableListOf()
            val group: MutableList<DrawerGroupInfoUiModel> = mutableListOf()
            val otherGroupList: MutableList<JoinedGroupUiModel> = mutableListOf()
            var gameIndex = -1

            fun <T> List<T>.middleIndex() = this.size / 2

            gameItemFlow
                .combine(currentGroupFlow) { gameItem, currentGroup ->
                    checkedApiResult(
                        apiResult = gameItem,
                        success = { data ->
                            game.addAll(data.toHomeGameItemUiModelList())

                            initGameId?.let {
                                data.mapIndexed { index, gameItem ->
                                    if (gameItem.id == initGameId) { gameIndex = index + startPadding }
                                }
                                gameId = it
                            } ?: kotlin.run {
                                gameIndex = data.middleIndex() + startPadding
                                gameId = data[data.middleIndex()].id
                            }
                        },
                        error = { throwable -> throw Exception(throwable.code) }
                    )

                    checkedApiResult(
                        apiResult = currentGroup,
                        success = { data ->
                            group.add(DrawerGroupInfoUiModel.CurrentGroupInfo(data))
                            currentGroupName = data.name
                        },
                        error = { throwable -> throw Exception(throwable.code) }
                    )
                }
                .combine(joinedGroupFlow) { _, joinedGroup ->
                    checkedApiResult(
                        apiResult = joinedGroup,
                        success = { data ->
                            otherGroupList.addAll(data.toOtherGroupInfo(currentGroupId = groupId))
                            data.toMyGroupProfileInfo(groupId)?.let { group.add(it) }
                        },
                        error = { throwable -> throw Exception(throwable.code) }
                    )
                }
                .combine(myInfoFlow) { _, info ->
                    checkedApiResult(
                        apiResult = info,
                        success = { data ->
                            myId = data.id.toLong()
                            nickName = data.nickname
                        },
                        error = { throwable -> throw Exception(throwable.code) }
                    )
                }
                .catch {
                    _gameUiState.value = HomeUiState.Error(it)
                    _currentGroupUiState.value = HomeUiState.Error(it)
                    _joinedGroupUiState.value = HomeUiState.Error(it)
                }
                .collectLatest {
                    _joinedGroupUiState.value = HomeUiState.Success(otherGroupList)
                    _gameUiState.value = HomeUiState.Success(game)
                    _currentGroupUiState.value = HomeUiState.Success(group)

                    fetchUserListFromServer(groupId, gameId)
                    setGameItemSelected(gameIndex)
                }
        }
    }

    fun getGameItemSelectedPosition(): Int = selectedPosition

    fun fetchUserList(initGameId: Long? = null) {
        initGameId?.let {
            gameId = it
        }
        fetchUserListFromServer(groupId, gameId)
    }

    // todo : 여기서 role 검사해서 owner일 경우 setting button view 쪽에서 enable하게 하고 있는데
    // 이게 맞나 싶음. 백엔드분들께 요청해서 이거 좀 바꾸고 싶음.
    private fun fetchUserListFromServer(groupId: Long, gameId: Long) {
        _userUiState.value = HomeUiState.Loading
        _ownerCheckUiState.value = HomeUiState.Loading
        userListFetchJob?.cancel()

        val userRank: MutableList<UserRankUiModel> = mutableListOf()
        var isOwner: Boolean = false

        userListFetchJob = viewModelScope.launch {
            delay(USER_RANK_LOAD_FORCE_DELAY)

            getUserRankUseCase(groupId.toInt(), gameId.toInt())
                .map {
                    checkedApiResult(
                        apiResult = it,
                        success = { data ->
                            userRank.addAll(data.toUserRankUiModel(myId))
                            data.getMyInfo(myId)?.let { info ->
                                isOwner = info.role == Role.OWNER
                            } ?: run { throw NullPointerException() }
                        },
                        error = { throwable -> throw IllegalArgumentException(Exception(throwable.message)) }
                    )
                }
                .catch {
                    _userUiState.value = HomeUiState.Error(it)
                    _ownerCheckUiState.value = HomeUiState.Error(it)
                }
                .collectLatest {
                    _userUiState.value = HomeUiState.Success(userRank)
                    _ownerCheckUiState.value = HomeUiState.Success(isOwner)
                }
        }
    }

    fun quitGroup() {
        _groupQuitUiState.value = GroupQuitUiModel.Loading
        viewModelScope.launch {
            quitGroupUseCase.invoke(groupId, nickName).collectLatest {
                when (it) {
                    is GroupQuitItem.Success -> {
                        _groupQuitUiState.value = GroupQuitUiModel.Success
                    }
                    is GroupQuitItem.FailCauseOwner -> {
                        _groupQuitUiState.value = GroupQuitUiModel.FailCauseOwner
                    }
                    is GroupQuitItem.FailCauseOnlyOneMember -> {
                        _groupQuitUiState.value = GroupQuitUiModel.FailCauseOnlyOneMember
                    }
                    is GroupQuitItem.FailUnknownError -> {
                        _groupQuitUiState.value = GroupQuitUiModel.FailUnknownError
                    }
                }
            }
        }
    }

    fun addGuestMember(guestName: String) {
        addGuestToServer(guestName, this.groupId)
    }

    private fun addGuestToServer(
        guestName: String,
        groupId: Long
    ) = viewModelScope.launch {
        addGuestMemberUseCase.addGuest(groupId, guestName).collectLatest {
            when(it) {
                is BaseStateItem.Success -> { _checkAddingGuestCompleted.value = HomeUiState.Success(true) }
                is BaseStateItem.Error -> { _checkAddingGuestCompleted.value = HomeUiState.Error(
                    IllegalStateException(it.exception.message)
                ) }
            }
        }
    }

    fun checkNicknameValidation(nickname: String) {
        queryNicknameValidationToServer(nickname, groupId)
    }

    private fun queryNicknameValidationToServer(nickname: String, groupId: Long) = viewModelScope.launch {
        _checkNicknameValidation.value = NicknameValidationUiModel.Loading

        addGuestMemberUseCase.checkNicknameValidation(groupId, nickname).collectLatest {
            checkedApiResult(
                apiResult = it,
                success = { data ->
                    when (data.isAvailable) {
                        true -> { _checkNicknameValidation.value = NicknameValidationUiModel.Valid }
                        false -> {
                            _checkNicknameValidation.value = when (data.reason) {
                                "DUPLICATED_NICKNAME" -> { NicknameValidationUiModel.InvalidCauseDuplicated }
                                "INVALID_NICKNAME" -> { NicknameValidationUiModel.InvalidCauseIncorrectFormat }
                                else -> { NicknameValidationUiModel.UnknownError }
                            }
                        }
                    }
                },
                error = { _checkNicknameValidation.value = NicknameValidationUiModel.UnknownError }
            )
        }
    }

    companion object {
        const val RV_SELECTED_POSITION_RESET = -1
        private const val GAME_USER_ID_TO_BE_SET: Long = -1L
    }
}
