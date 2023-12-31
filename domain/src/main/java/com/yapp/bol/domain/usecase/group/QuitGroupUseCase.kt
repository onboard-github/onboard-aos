package com.yapp.bol.domain.usecase.group

import android.util.Log
import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.ErrorItem
import com.yapp.bol.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.ResponseBody
import javax.inject.Inject

class QuitGroupUseCase @Inject constructor(
    private val repository: MemberRepository,
) {
    operator fun invoke(groupId: Long, nickname: String) = repository.quitGroup(groupId, nickname)
}
