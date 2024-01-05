package com.yapp.bol.domain.usecase.group

import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    operator fun invoke(groupId: String): Flow<ApiResult<String>> = groupRepository.deleteGroup(groupId)
}
