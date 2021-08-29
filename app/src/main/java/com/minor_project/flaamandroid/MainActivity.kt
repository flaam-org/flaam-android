package com.minor_project.flaamandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.minor_project.flaamandroid.data.UserPreferences
import com.minor_project.flaamandroid.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var userPrefs: UserPreferences
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navController = findNavController(R.id.fragmentContainerView)

        navController.navigate(R.id.splashFragment)

        runBlocking {
            delay(2000L)
            if(userPrefs.getToken().first() == null){
                navController.navigate(R.id.introFragment)
            }else{
                Timber.e(userPrefs.getToken().first())
                navController.navigate(R.id.feedFragment)
            }
        }
    }
}