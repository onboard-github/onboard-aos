package com.yapp.bol.data.mapper

import com.yapp.bol.data.model.file.ImageFileUploadResponse
import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.ImageFileItem

object FileMapper {

    fun ApiResult<ImageFileUploadResponse>.fileUploadToDomain(): ApiResult<ImageFileItem> {
        return when (this) {
            is ApiResult.Success -> ApiResult.Success(ImageFileItem(data.uuid, data.url))
            is ApiResult.Error -> ApiResult.Error(exception)
        }
    }
}
