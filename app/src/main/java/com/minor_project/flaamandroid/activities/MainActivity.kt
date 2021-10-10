package com.minor_project.flaamandroid.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.minor_project.flaamandroid.R
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.databinding.ActivityMainBinding
import com.minor_project.flaamandroid.utils.ApiResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userPrefs: UserPreferences
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragmentContainerView)

        lifecycleScope.launch {
            delay(2000L)

            if (userPrefs.accessToken.first() == null) {
                navController.navigate(R.id.introFragment)
            } else {
                viewModel.refreshToken()
            }
        }

        viewModel.refreshTokenResult.observe(this@MainActivity) {
            Timber.e("mainact $it")
            when (it) {
                is ApiResponse.Error -> {
                    Snackbar.make(binding.root, "can't refresh token!", Snackbar.LENGTH_SHORT)
                        .show()
                    navController.navigate(R.id.action_global_feedFragment)
                }

                is ApiResponse.Success -> {
                    lifecycleScope.launch {
                        userPrefs.updateTokens(it.body)
                        navController.navigate(R.id.action_global_feedFragment)
                    }

                }
            }

        }
    }

}