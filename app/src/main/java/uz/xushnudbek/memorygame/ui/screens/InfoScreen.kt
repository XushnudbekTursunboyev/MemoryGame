package uz.xushnudbek.memorygame.ui.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.xushnudbek.memorygame.R
import uz.xushnudbek.memorygame.databinding.ScreenGameBinding
import uz.xushnudbek.memorygame.databinding.ScreenInfoBinding

class InfoScreen:Fragment(R.layout.screen_info) {

    private var _bn: ScreenInfoBinding? = null
    private val bn get() = _bn ?: throw NullPointerException("cannot inflate")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _bn = ScreenInfoBinding.bind(view)
        bn.btnBack.setOnClickListener { findNavController().popBackStack() }

        bn.telegram.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Xushnudbek_developer"))
                intent.setPackage("org.telegram.messenger")
                startActivity(intent)
        }
    }
}