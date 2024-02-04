package com.yapp.bol.presentation.view.setting.group

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseActivity
import com.yapp.bol.presentation.databinding.ActivityGroupSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupSettingActivity : BaseActivity<ActivityGroupSettingBinding>(R.layout.activity_group_setting) {

    private val viewModel: GroupSettingViewModel by viewModels()

    override fun onCreateAction() {
        super.onCreateAction()
        getGroupIdFromIntent()
    }

    private fun getGroupIdFromIntent() {
        val noValuePassed = -1L
        intent.getLongExtra(GROUP_ID_FOR_SETTING, noValuePassed).also {
            if (it == noValuePassed) {
                finish()
            } else {
                viewModel.groupId = it
            }
        }
        intent.getStringExtra(GROUP_NAME_FOR_SETTING)?.let {
            viewModel.setGroupName(it)
        } ?: finish()
    }

    companion object {
        const val GROUP_ID_FOR_SETTING = "HOME_GROUP_ID"
        const val GROUP_NAME_FOR_SETTING = "HOME_GROUP_NAME"
        fun startActivity(context: Context, groupId: Long, groupName: String) {
            context.startActivity(
                Intent(context, GroupSettingActivity::class.java)
                    .apply {
                        putExtra(GROUP_ID_FOR_SETTING, groupId)
                        putExtra(GROUP_NAME_FOR_SETTING, groupName)
                    }
            )
        }
    }
}