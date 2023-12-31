package com.yapp.bol.domain.usecase.group

import com.yapp.bol.domain.repository.MemberRepository
import javax.inject.Inject

class QuitGroupUseCase @Inject constructor(
    private val repository: MemberRepository,
) {
    operator fun invoke(groupId: Long, nickname: String) = repository.quitGroup(groupId, nickname)
}
