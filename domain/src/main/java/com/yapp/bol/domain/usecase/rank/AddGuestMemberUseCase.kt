package com.yapp.bol.domain.usecase.rank

import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.NicknameValidItem
import com.yapp.bol.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddGuestMemberUseCase @Inject constructor(
    private val repository: MemberRepository
) {
    suspend fun addGuest(groupId: Long, guestNickname: String) = repository.postGuestMember(
        groupId.toInt(), guestNickname
    )

    fun checkNicknameValidation(groupId: Long, nickname: String): Flow<ApiResult<NicknameValidItem>> {
        return repository.getValidateNickName(groupId.toInt(), nickname)
    }
}
