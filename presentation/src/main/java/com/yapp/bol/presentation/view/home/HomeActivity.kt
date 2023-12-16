package com.yapp.bol.presentation.view.home

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseActivity
import com.yapp.bol.presentation.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateAction() {
        super.onCreateAction()
        intent.getLongExtra(HOME_GROUP_ID_KEY, NO_VALUE_PASSED).also {
            if (it == NO_VALUE_PASSED) {
                viewModel.groupId = null
            } else {
                viewModel.groupId = it
            }
        }
        intent.getLongExtra(HOME_GAME_ID_KEY, NO_VALUE_PASSED).also {
            if (it == NO_VALUE_PASSED) {
                viewModel.gameId = null
            } else {
                viewModel.gameId = it
            }
        }
    }

    companion object {
        const val HOME_GROUP_ID_KEY = "HOME_GROUP_ID"
        const val HOME_GAME_ID_KEY = "HOME_GAME_ID"
        private const val NO_VALUE_PASSED = -1L

        fun startActivity(context: Context, groupId: Long?, gameId: Long? = null) {
            context.startActivity(
                Intent(context, HomeActivity::class.java)
                    .apply {
                        groupId?.let { putExtra(HOME_GAME_ID_KEY, groupId) }
                        gameId?.let { putExtra(HOME_GAME_ID_KEY, gameId) }
                    },
            )
        }
    }
}
