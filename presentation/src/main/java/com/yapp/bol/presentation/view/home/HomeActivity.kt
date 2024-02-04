package com.yapp.bol.presentation.view.home

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.yapp.bol.presentation.R
import com.yapp.bol.presentation.base.BaseActivity
import com.yapp.bol.presentation.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {

    private val viewModel: HomeViewModel by viewModels()

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
    }

    override fun onCreateAction() {
        super.onCreateAction()
        intent.getLongExtra(HOME_GROUP_ID_KEY, NO_VALUE_PASSED).also {
            if (it == NO_VALUE_PASSED || it == NO_JOINED_GROUP) {
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
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        binding.menu.setupWithNavController(navHostFragment.findNavController())
    }

    companion object {
        const val HOME_GROUP_ID_KEY = "HOME_GROUP_ID"
        const val HOME_GAME_ID_KEY = "HOME_GAME_ID"
        private const val NO_VALUE_PASSED = -1L
        private const val NO_JOINED_GROUP = -100L

        fun startActivity(context: Context, groupId: Long?, gameId: Long? = null) {
            context.startActivity(
                Intent(context, HomeActivity::class.java)
                    .apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        groupId?.let { putExtra(HOME_GROUP_ID_KEY, groupId) }
                        gameId?.let { putExtra(HOME_GAME_ID_KEY, gameId) }
                    },
            )
        }
    }
}
