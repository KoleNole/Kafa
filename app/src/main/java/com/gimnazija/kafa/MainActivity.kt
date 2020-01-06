package com.gimnazija.kafa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private var screen : Screen? = null // nas ekran

    // funkcija koja se pozove kada zapocnemo aplikaciju
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screen = Screen(this)
        setContentView(screen)
    }

    //funkcija koja se pozove kada izadjemo iz aplikacije
    override fun onPause() {
        super.onPause()
        screen?.stopView()
    }
}
