package com.example.myblog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val navController = findNavController(R.id.nav_host_fragment)
            setupActionBarWithNavController(navController)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return try {
            val navController = findNavController(R.id.nav_host_fragment)
            navController.navigateUp() || super.onSupportNavigateUp()
        } catch (e: Exception) {
            e.printStackTrace()
            super.onSupportNavigateUp()
        }
    }
}