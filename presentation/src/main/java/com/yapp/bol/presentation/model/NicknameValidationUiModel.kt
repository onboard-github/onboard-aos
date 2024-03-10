package com.yapp.bol.presentation.model

sealed class NicknameValidationUiModel {
    object Valid : NicknameValidationUiModel()
    object Loading : NicknameValidationUiModel()
    object InvalidCauseDuplicated : NicknameValidationUiModel()
    object InvalidCauseIncorrectFormat : NicknameValidationUiModel()
    object UnknownError : NicknameValidationUiModel()
}
