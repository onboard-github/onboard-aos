package com.yapp.bol.data.datasource.user

import com.yapp.bol.data.model.user.JoinedGroupResponse
import com.yapp.bol.data.model.user.JoinedGroupV2Response
import com.yapp.bol.data.model.user.OnBoardResponse
import com.yapp.bol.data.model.user.TotalMatchCountResponse
import com.yapp.bol.data.model.user.UserRequest
import com.yapp.bol.data.model.user.UserResponse
import com.yapp.bol.data.remote.UserApi
import com.yapp.bol.domain.handle.BaseRepository
import com.yapp.bol.domain.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val userApi: UserApi,
) : BaseRepository(), UserDataSource {

    override suspend fun putUserName(userRequest: UserRequest) {
        userApi.putUserName(userRequest)
    }

    override fun getJoinedGroup(): Flow<ApiResult<JoinedGroupResponse>> =
        flow {
            safeApiCall {
                userApi.getJoinedGroup()
            }.also { emit(it) }
        }

    override fun getJoinedGroupV2(): Flow<ApiResult<JoinedGroupV2Response>> =
        flow {
            safeApiCall {
                userApi.getJoinedGroupV2()
            }.also { emit(it) }
        }

    override fun getOnBoard(): Flow<ApiResult<OnBoardResponse>> = flow {
        val result = safeApiCall { userApi.getOnboard() }
        emit(result)
    }

    override fun getUserInfo(): Flow<ApiResult<UserResponse>> = flow {
        val result = safeApiCall { userApi.getUserInfo() }
        emit(result)
    }

    override fun getMyTotalMatchCount(): Flow<ApiResult<TotalMatchCountResponse>> = flow {
        emit(safeApiCall { userApi.getMyTotalMatchCount() })
    }
}
