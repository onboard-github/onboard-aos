package com.yapp.bol.domain.usecase.group

import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.MemberItems
import com.yapp.bol.domain.repository.GroupRepository
import com.yapp.bol.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangeOwnerUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val groupRepository: GroupRepository
) {
    fun getMemberList(
        groupId: Int,
        pageSize: Int,
        cursor: String?,
        nickname: String?,
    ): Flow<ApiResult<MemberItems>> {

        return memberRepository.getMemberList(groupId, pageSize, cursor, nickname)
    }

    fun updateOwner(groupId: Int, memberId: Int) = groupRepository.updateOwner(groupId, memberId)
}
