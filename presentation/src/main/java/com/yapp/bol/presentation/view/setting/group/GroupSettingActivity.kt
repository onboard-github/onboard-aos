package com.yapp.bol.presentation.view.setting.group

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseActivity
import com.yapp.bol.presentation.databinding.ActivityGroupSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupSettingActivity : BaseActivity<ActivityGroupSettingBinding>(R.layout.activity_group_setting) {

    private val viewModel: GroupSettingViewModel by viewModels()
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.setting_nav_host) as NavHostFragment).navController
    }

    override fun onCreateAction() {
        super.onCreateAction()
        getGroupIdFromIntent()
        getPurposeFromIntent()
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

    private fun getPurposeFromIntent() {
        val normal = Purpose.NORMAL.ordinal
        intent.getIntExtra(PURPOSE_FOR_ACTIVITY, normal).also {
            if (it == Purpose.GROUP_CHANGE_OWNER.ordinal) {
                setStartDestinationToChangeOwnerFragment()
            }
        }
    }

    private fun setStartDestinationToChangeOwnerFragment() {
        val navGraph = navController.navInflater.inflate(R.navigation.group_setting_nav_graph)
        val changeOwnerFragmentId = R.id.changeOwnerFragment
        val startDestinationId = navGraph.findNode(changeOwnerFragmentId)?.id ?: return
        navGraph.setStartDestination(startDestinationId)
        navController.graph = navGraph
    }

    companion object {
        const val GROUP_ID_FOR_SETTING = "HOME_GROUP_ID"
        const val GROUP_NAME_FOR_SETTING = "HOME_GROUP_NAME"
        const val PURPOSE_FOR_ACTIVITY = "PURPOSE_FOR_ACTIVITY"
        enum class Purpose {
            NORMAL,
            GROUP_CHANGE_OWNER,
        }

        fun startActivity(
            context: Context,
            groupId: Long,
            groupName: String,
            purpose: Purpose = Purpose.NORMAL
        ) {
            context.startActivity(
                Intent(context, GroupSettingActivity::class.java)
                    .apply {
                        putExtra(GROUP_ID_FOR_SETTING, groupId)
                        putExtra(GROUP_NAME_FOR_SETTING, groupName)
                        putExtra(PURPOSE_FOR_ACTIVITY, purpose.ordinal)
                    }
            )
        }
    }
}
