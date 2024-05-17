package com.example.newssimple.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.newssimple.R
import com.example.newssimple.databinding.ActivityNewsBinding
import com.example.newssimple.model.NewsRepository
import com.example.newssimple.model.api.RetrofitSingle
import com.example.newssimple.model.local.MyDatabase
import com.example.newssimple.util.ViewModelFactory
import com.example.newssimple.viewModel.NewsViewModel

class NewsActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //after previous method not working i changed navHost to a simple fragment
        //then used thsese 2 lines instead of the previous version
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                NewsRepository(
                    MyDatabase.getDatabase(applicationContext).articleDao
                )
            )
        ).get(NewsViewModel::class.java)




    }
}