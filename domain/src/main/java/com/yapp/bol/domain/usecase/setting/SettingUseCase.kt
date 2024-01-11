package com.yapp.bol.domain.usecase.setting

import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.GroupMemberItem
import com.yapp.bol.domain.model.NicknameValidItem
import com.yapp.bol.domain.repository.GroupRepository
import com.yapp.bol.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val groupRepository: GroupRepository,
) {

    fun getValidateNickName(groupId: Int, nickname: String): Flow<ApiResult<NicknameValidItem>> {
        return memberRepository.getValidateNickName(groupId, nickname)
    }
    fun patchGroupMemberNickname(groupId: Long, memberId: Long, newNickname: String): Flow<ApiResult<GroupMemberItem>> {
        return groupRepository.patchGroupMemberNickname(groupId, memberId, newNickname)
    }
}
