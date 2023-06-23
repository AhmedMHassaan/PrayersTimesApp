package com.ahmed.m.hassaan.prayerstimesapp.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahmed.m.hassaan.prayerstimesapp.R
import com.ahmed.m.hassaan.prayerstimesapp.databinding.BottomSheetQiblaChooserBinding
import com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.qibla.QiblaDirectionActivity
import com.ahmed.m.hassaan.prayerstimesapp.presentation.ui.qibla_live.CompassActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class QiblaTypeBottomSheet(val latitude: Double, val longitue: Double, val direction: Double) :
    BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var binding: BottomSheetQiblaChooserBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetQiblaChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.listener = this
    }

    override fun onClick(v: View?) {
        val intent = when (v) {
            binding.btnUsingNormal -> {
                Intent(
                    context,
                    CompassActivity::class.java
                )
            }

            else -> {
                Intent(
                    context,
                    QiblaDirectionActivity::class.java
                )
            }
        }.also {
            it.putExtra("lat", latitude)
            it.putExtra("long", longitue)
            it.putExtra("dir", direction)
        }
        startActivity(intent)
    }
}