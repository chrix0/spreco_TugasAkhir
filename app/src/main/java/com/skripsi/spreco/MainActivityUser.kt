package com.skripsi.spreco

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.skripsi.spreco.databinding.ActivityMainUserBinding
import kotlinx.android.synthetic.main.activity_main_user.*

class MainActivityUser : AppCompatActivity() {

    private lateinit var binding: ActivityMainUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragmentContainerView)
        navBottom.setupWithNavController(navController)

        navBottom.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_katalog_user -> {
                    navController.navigate(R.id.nav_splist)
                    true
                }
                R.id.menu_wishlist_user -> {
                    navController.navigate(R.id.nav_wishlist)
                    true
                }
                R.id.menu_rekomendasi_user -> {
                    navController.navigate(R.id.nav_rekomendasi)
                    true
                }
                R.id.menu_about_user -> {
                    navController.navigate(R.id.nav_about)
                    true
                }
                else ->
                    false
            }
        }

//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_splist, R.id.nav_wishlist, R.id.nav_rekomendasi, R.id.nav_about
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navBottom.setupWithNavController(navController)
    }
}