package com.luckylittlesparrow.waterwall.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_host)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
    }
}
