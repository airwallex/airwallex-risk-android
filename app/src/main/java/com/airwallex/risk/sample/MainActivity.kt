package com.airwallex.risk.sample

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.airwallex.risk.AirwallexRisk
import com.airwallex.risk.sample.databinding.ActivityMainBinding

class MainActivity: FragmentActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val currentEventName = binding.eventNameEditText.text?.toString()?.trim()
            val currentScreenName = binding.screenNameEditText.text?.toString()?.trim()

            if (currentEventName != null && currentScreenName != null) {
                AirwallexRisk.log(event = currentEventName, screen = currentScreenName)
            }
        }
    }
}