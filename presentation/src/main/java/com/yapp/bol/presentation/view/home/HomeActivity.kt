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
        getValueFromIntent()
        setBottomNavigation()
    }

    private fun getValueFromIntent() {
        viewModel.groupId = intent.getLongExtra(HOME_GROUP_ID_KEY, -1L)
        intent.getLongExtra(HOME_GAME_ID_KEY, -1L).also {
            if (it == -1L) {
                viewModel.gameId = null
            } else {
                viewModel.gameId = it
            }
        }
    }

    private fun setBottomNavigation() {
        binding.menu.setupWithNavController(navHostFragment.findNavController())
    }

    companion object {
        const val HOME_GROUP_ID_KEY = "HOME_GROUP_ID"
        const val HOME_GAME_ID_KEY = "HOME_GAME_ID"

        fun startActivity(context: Context, groupId: Long, gameId: Long? = null) {
            context.startActivity(
                Intent(context, HomeActivity::class.java)
                    .putExtra(HOME_GROUP_ID_KEY, groupId)
                    .apply {
                        gameId?.let {
                            putExtra(HOME_GAME_ID_KEY, gameId)
                        }
                    },
            )
        }
    }
}
