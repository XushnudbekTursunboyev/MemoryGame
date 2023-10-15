package uz.xushnudbek.memorygame.ui.screens

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.xushnudbek.memorygame.R
import uz.xushnudbek.memorygame.data.LevelEnum
import uz.xushnudbek.memorygame.databinding.ScreenLevelBinding
import uz.xushnudbek.memorygame.utils.local.SharedPref

class LevelScreen:Fragment(R.layout.screen_level) {
    private var _bn:ScreenLevelBinding?=null
    private lateinit var pref: SharedPref
    private val bn get() = _bn ?: throw NullPointerException("cannot inflate")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _bn = ScreenLevelBinding.bind(view)
        pref = SharedPref(requireContext())
        pref.clearData()
        //pref.sound = true

        bn.btnInfo.setOnClickListener { findNavController().navigate(R.id.infoScreen) }

        bn.btnEasy.setOnClickListener { openGameScreen(LevelEnum.EASY) }
        bn.btnMedium.setOnClickListener { openGameScreen(LevelEnum.MEDIUM) }
        bn.btnHard.setOnClickListener { openGameScreen(LevelEnum.HARD) }
    }

    private fun openGameScreen(level: LevelEnum) {
        findNavController().navigate(R.id.gameScreen, bundleOf("data" to level))
    }
}