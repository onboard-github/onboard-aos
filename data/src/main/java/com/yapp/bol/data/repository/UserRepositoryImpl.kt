package com.yapp.bol.data.repository

import com.yapp.bol.data.datasource.user.UserDataSource
import com.yapp.bol.data.mapper.UserMapper.deleteAccountToDomain
import com.yapp.bol.data.mapper.UserMapper.toBoardDomain
import com.yapp.bol.data.mapper.UserMapper.toJoinedGroupItem
import com.yapp.bol.data.mapper.UserMapper.totalMatchCountToDomain
import com.yapp.bol.data.mapper.UserMapper.toUserDomain
import com.yapp.bol.data.model.user.UserRequest
import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.JoinedGroupItem
import com.yapp.bol.domain.model.OnBoardingItem
import com.yapp.bol.domain.model.user.DeleteAccountItem
import com.yapp.bol.domain.model.user.TotalMatchCountItem
import com.yapp.bol.domain.model.user.UserItem
import com.yapp.bol.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
) : UserRepository {

    override suspend fun putUserName(nickName: String) {
        userDataSource.putUserName(UserRequest(nickName))
    }

    override fun getJoinedGroup(): Flow<ApiResult<List<JoinedGroupItem>>> =
        userDataSource.getJoinedGroup().map { it.toJoinedGroupItem() }

    override fun getOnBoard(): Flow<ApiResult<OnBoardingItem>> {
        return userDataSource.getOnBoard().map { it.toBoardDomain() }
    }

    override fun getUserInfo(): Flow<ApiResult<UserItem>> {
        return userDataSource.getUserInfo().map {
            it.toUserDomain()
        }
    }

    override fun getMyTotalMatchCount(): Flow<ApiResult<TotalMatchCountItem>> {
        return userDataSource.getMyTotalMatchCount().map { it.totalMatchCountToDomain() }
    }

    override fun deleteAccount(): Flow<DeleteAccountItem> {
        return userDataSource.deleteAccount().map { it.deleteAccountToDomain() }
    }
}
