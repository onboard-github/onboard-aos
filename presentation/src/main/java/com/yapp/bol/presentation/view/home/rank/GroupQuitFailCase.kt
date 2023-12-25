package com.yapp.bol.presentation.view.home.rank

import androidx.annotation.StringRes
import com.yapp.bol.presentation.R

sealed class GroupQuitFailCase(
    @StringRes val dialogTopMessageId: Int,
    @StringRes val dialogTopBoldMessageId: Int,
    @StringRes val dialogBottomMessageId: Int,
) {
    object Owner : GroupQuitFailCase(
        R.string.group_quit_fail_cause_owner_top,
        R.string.group_quit_fail_cause_owner_top_bold,
        R.string.group_quit_fail_cause_owner_bottom,
    )
    object OnlyMember : GroupQuitFailCase(
        R.string.group_quit_fail_cause_only_member_top,
        R.string.group_quit_fail_cause_only_member_top_bold,
        R.string.group_quit_fail_cause_only_member_bottom,
    )
}
