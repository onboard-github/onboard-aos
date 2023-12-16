package com.yapp.bol.presentation.view.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.bol.domain.model.NewGroupItem
import com.yapp.bol.domain.usecase.login.NewGroupUseCase
import com.yapp.bol.presentation.utils.Constant.EMPTY_STRING
import com.yapp.bol.presentation.utils.checkedApiResult
import com.yapp.bol.presentation.utils.isInputTextValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NewGroupViewModel @Inject constructor(
    private val newGroupUseCase: NewGroupUseCase,
) : ViewModel() {

    private val _groupName = MutableLiveData(EMPTY_STRING)
    val groupName: LiveData<String> = _groupName

    private val _groupDescription = MutableLiveData(EMPTY_STRING)
    val groupDescription: LiveData<String> = _groupDescription

    private val _successGroupDate = MutableLiveData<NewGroupItem?>(null)
    val successGroupDate: LiveData<NewGroupItem?> = _successGroupDate

    private val _groupRandomImage = MutableLiveData("")
    val groupRandomImage: LiveData<String> = _groupRandomImage

    var userName = ""

    private var groupOrganization = EMPTY_STRING
    private var imageFile: File? = null
    private var uuid: String = ""

    val isCompleteButtonActivation
        get() = isInputTextValid(groupName.value) && isInputTextValid(groupDescription.value)

    init {
        getRandomImage()
        getUserInfo()
    }

    fun createNewGroup(nickName: String) {
        viewModelScope.launch {
            val (uuid, imageUrl) = withContext(Dispatchers.IO) {
                imageFile?.let { postFileUpload(it) }
                    ?: Pair(uuid, groupRandomImage.value.orEmpty())
            }
            postCreateGroup(nickName, uuid, imageUrl)
        }
    }

    /**
     * first: uuid | second: url
     */
    private suspend fun postFileUpload(file: File): Pair<String, String> {
        var imageInfo = Pair(EMPTY_STRING, EMPTY_STRING)

        newGroupUseCase.postFileUpload(file).collectLatest {
            checkedApiResult(
                apiResult = it,
                success = { data ->
                    imageInfo = Pair(data.uuid, data.url)
                },
            )
        }

        return imageInfo
    }

    private suspend fun postCreateGroup(nickName: String, uuid: String, imageUrl: String) {
        newGroupUseCase.postCreateGroup(
            name = groupName.value ?: EMPTY_STRING,
            description = groupDescription.value ?: EMPTY_STRING,
            organization = groupOrganization,
            imageUrl = imageUrl,
            uuid = uuid,
            nickname = nickName,
        ).collectLatest {
            checkedApiResult(
                apiResult = it,
                success = { data -> _successGroupDate.value = data },
            )
        }
    }

    fun getRandomImage() {
        viewModelScope.launch {
            newGroupUseCase.getRandomImage().collectLatest {
                checkedApiResult(
                    apiResult = it,
                    success = { data ->
                        uuid = data.uuid
                        _groupRandomImage.value = data.url
                    },
                )
            }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            newGroupUseCase.getUserInfo().collectLatest {
                checkedApiResult(apiResult = it, success = { data -> userName = data.nickname })
            }
        }
    }

    fun updateGroupInfo(type: String, value: String) {
        when (type) {
            NEW_GROUP_NAME -> _groupName.value = value
            NEW_GROUP_DESCRIPTION -> _groupDescription.value = value
            NEW_GROUP_ORGANIZATION -> groupOrganization = value
        }
    }

    fun updateImageFile(file: File) {
        imageFile = file
    }

    companion object {
        const val NEW_GROUP_NAME = "newGroupName"
        const val NEW_GROUP_DESCRIPTION = "newGroupDescription"
        const val NEW_GROUP_ORGANIZATION = "newGroupOrganization"
    }
}
