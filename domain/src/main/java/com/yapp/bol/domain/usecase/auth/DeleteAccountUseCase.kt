package com.yapp.bol.domain.usecase.auth

import com.yapp.bol.domain.repository.UserRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke() = repository.deleteAccount()
}
