package com.yapp.bol.domain.repository

import com.yapp.bol.domain.model.ApiResult
import com.yapp.bol.domain.model.ImageFileItem
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FileRepository {

    fun postFileUpload(file: File): Flow<ApiResult<ImageFileItem>>
}
