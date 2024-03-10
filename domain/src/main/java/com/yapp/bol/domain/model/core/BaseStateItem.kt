package com.yapp.bol.domain.model.core

import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.ErrorItem
import okhttp3.ResponseBody

sealed class BaseStateItem {
    object Success : BaseStateItem()
    data class Error(val exception: ErrorItem) : BaseStateItem()

    fun ApiResult<ResponseBody>.toBaseItem(): BaseStateItem {
        return when (this) {
            is ApiResult.Success -> Success
            is ApiResult.Error -> { Error(exception) }
        }
    }
}
