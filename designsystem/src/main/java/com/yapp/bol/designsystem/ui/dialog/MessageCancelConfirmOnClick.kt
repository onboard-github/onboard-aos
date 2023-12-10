package com.yapp.bol.designsystem.ui.dialog

interface MessageCancelConfirmOnClick : MessageConfirmOnClick {
    var onCancelClickNeedDismissDialog: Boolean
    fun onCancelClick() { }
}
