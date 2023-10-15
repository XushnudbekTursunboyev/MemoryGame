package uz.xushnudbek.memorygame.ui.screens

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.xushnudbek.memorygame.R
import uz.xushnudbek.memorygame.data.CardData
import uz.xushnudbek.memorygame.data.LevelEnum
import uz.xushnudbek.memorygame.databinding.FinishDialogBinding
import uz.xushnudbek.memorygame.databinding.MenuDialogBinding
import uz.xushnudbek.memorygame.databinding.ReloadDialogBinding
import uz.xushnudbek.memorygame.databinding.ScreenGameBinding
import uz.xushnudbek.memorygame.ui.viewmodel.factory.GameViewModelFactory
import uz.xushnudbek.memorygame.ui.viewmodel.impl.GameViewModelImpl
import uz.xushnudbek.memorygame.utils.closeCard
import uz.xushnudbek.memorygame.utils.hideCard
import uz.xushnudbek.memorygame.utils.local.SharedPref
import uz.xushnudbek.memorygame.utils.openCard

class GameScreen : Fragment(R.layout.screen_game) {
    private var _bn: ScreenGameBinding? = null
    private val bn get() = _bn ?: throw NullPointerException("cannot inflate")
    private lateinit var pref: SharedPref
    private val viewModel by lazy {
        ViewModelProvider(this, GameViewModelFactory())[GameViewModelImpl::class.java]
    }

    private val images = ArrayList<ImageView>()
    private var level = LevelEnum.EASY
    private var _h = 0
    private var _w = 0
    private var firstOpen = -1
    private var secondOpen = -1
    private var hideCount = 0
    private var openCount = 0
    private var closeCardSound: MediaPlayer? = null
    private var trueAnsSound: MediaPlayer? = null
    private var winSound: MediaPlayer? = null
    private var time = 45000
    private lateinit var timer: CountDownTimer
    private var isSound = true

    @SuppressLint("ObjectAnimatorBinding", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        level = requireArguments().getSerializable("data") as LevelEnum
        _bn = ScreenGameBinding.bind(view)

        pref = SharedPref(requireContext())
        media()
        bn.container.post {
            _h = bn.container.height / level.horCount
            _w = bn.container.width / level.verCount
            viewModel.loadCardByLevel(level)
        }
        viewModel.cardsLiveData.observe(viewLifecycleOwner, cardsObserver)
        bn.btnMenu.setOnClickListener {
            showMenuDialog()
        }
        isSound = pref.sound
        when (level) {
            LevelEnum.EASY -> {
                bn.tvLevelText.text = "EASY"
                time = 45000
            }

            LevelEnum.MEDIUM -> {
                bn.tvLevelText.text = "MEDIUM"
                time = 75000
            }

            LevelEnum.HARD -> {
                bn.tvLevelText.text = "HARD"
                time = 105000
            }
        }

        bn.btnReload.setOnClickListener {
            showReloadDialog()
        }
    }

    private fun media() {
        closeCardSound = MediaPlayer.create(context, R.raw.close)
        trueAnsSound = MediaPlayer.create(context, R.raw.remov)
        winSound = MediaPlayer.create(context, R.raw.win)
    }

    private fun winSound() {
        if (isSound) {
            winSound?.start()
        } else {
            winSound?.pause()
        }
    }

    private fun closeCardSound() {
        if (isSound) {
            closeCardSound?.start()
        } else {
            closeCardSound?.pause()
        }
    }

    private fun trueAnswerSound() {
        if (isSound) {
            trueAnsSound?.start()
        } else {
            trueAnsSound?.pause()
        }
    }

    private val cardsObserver = Observer<List<CardData>> {
        for (i in 0 until level.verCount) {
            for (j in 0 until level.horCount) {
                val imageView = ImageView(requireContext())
                bn.container.addView(imageView)
                val lp = imageView.layoutParams as ConstraintLayout.LayoutParams
                lp.apply {
                    width = _w - 4
                    height = _h - 4
                }
                imageView.x = 0f
                imageView.y = 0f
                imageView.layoutParams = lp
                imageView.tag = it[i * level.horCount + j]
                imageView.setBackgroundResource(R.drawable.img_background)
                imageView.setImageResource(R.drawable.image_back)
                imageView.setPadding(16, 16, 16, 16)
                imageView.animate().x(i * _w * 1f).y(j * _h * 1f).setDuration(2000).withEndAction {
                    images[i * level.horCount + j].openCard()
                    imageView.animate().setDuration(2000).withEndAction {
                        images[i * level.horCount + j].closeCard()
                        imageView.animate().setDuration(400).start()
                    }.start()
                }.start()
                images.add(imageView)
            }
        }
        lifecycleScope.launch {
            delay(2000)
            setClickEventToImages()
        }

        timerStart(time.toLong())


    }

    private fun timerStart(time: Long) {
        timer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                bn.tvStep.text = "${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                if (bn.tvStep.text == "0")
                    timer.cancel()
                    showFinishDialog("Lose")
            }
        }.start()
    }

    private fun setClickEventToImages() {
        images.forEachIndexed { index, view ->
            view.setOnClickListener {
                if (bn.tvStep.text != "0") {
                    if (firstOpen == -1 && index != firstOpen) {
                        // doubleClick = true
                        firstOpen = index
                        view.openCard()
                        openCount++
                    } else if (secondOpen == -1 && index != secondOpen && index != firstOpen) {
                        secondOpen = index
                        view.openCard {
                            check()
                        }
                        openCount++
                        bn.tvAttempt.text = openCount.toString()
                    }
                } else {
                    Toast.makeText(requireContext(), "You lost! CLick reload button!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun check() {
        if ((images[firstOpen].tag as CardData).id == (images[secondOpen].tag as CardData).id) {
            images[firstOpen].hideCard()
            images[secondOpen].hideCard {
                firstOpen = -1
                secondOpen = -1
            }
            trueAnswerSound()
            hideCount++
            if (hideCount == level.horCount * level.verCount / 2) {
                showFinishDialog("win")
                hideCount = 0
            }
        } else {
            closeCardSound()
            images[firstOpen].closeCard()
            images[secondOpen].closeCard {
                firstOpen = -1
                secondOpen = -1
            }
        }
    }

    private fun showFinishDialog(message: String) {
        val dialogBinding = FinishDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        val customDialog = AlertDialog.Builder(requireContext()).create()
        customDialog.apply {
            setView(dialogBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }.show()
        if (message == "win") {
            winSound()
            dialogBinding.animationView.setAnimation(R.raw.congrats)
        } else {
            dialogBinding.animationView.setAnimation(R.raw.game_over)
        }
        timer.cancel()
        dialogBinding.btnHome.setOnClickListener {
            customDialog.dismiss()
            findNavController().popBackStack()
        }

        dialogBinding.btnNext.setOnClickListener {
            timer.cancel()
            bn.tvAttempt.text = "0"
            bn.container.removeAllViews()
            images.clear()

            firstOpen = -1
            secondOpen = -1
            hideCount = 0
            openCount = 0
            viewModel.cardsLiveData.observe(viewLifecycleOwner, cardsObserver)
            viewModel.loadCardByLevel(level)
            customDialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showMenuDialog() {
        val dialog = Dialog(requireContext())
        val bnMenu = MenuDialogBinding.inflate(layoutInflater)
        dialog.setContentView(bnMenu.root)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.bg_dialog)))

        pref.timer = bn.tvStep.text.toString().toLong() * 1000
        timer.cancel()
        bnMenu.btnResume.setOnClickListener {
            timerStart(pref.timer.toLong())
            dialog.hide()
        }

        bnMenu.btnNewGame.setOnClickListener {
            timer.cancel()
            bn.tvAttempt.text = "0"
            bn.container.removeAllViews()
            images.clear()

            firstOpen = -1
            secondOpen = -1
            hideCount = 0
            openCount = 0
            viewModel.cardsLiveData.observe(viewLifecycleOwner, cardsObserver)
            viewModel.loadCardByLevel(level)
            dialog.hide()
        }

        bnMenu.btnHome.setOnClickListener {
            dialog.hide()
            findNavController().popBackStack()
        }

        if (isSound) {
            bnMenu.btnSound.text = "Sound ON"
        } else
            bnMenu.btnSound.text = "Sound OFF"

        bnMenu.btnSound.setOnClickListener {
            pref.sound = !pref.sound
            isSound = pref.sound
            if (isSound) {
                bnMenu.btnSound.text = "Sound ON"
            } else
                bnMenu.btnSound.text = "Sound OFF"
        }
        dialog.show()
    }

    private fun showReloadDialog(){
        val dialogBinding = ReloadDialogBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        val customDialog = AlertDialog.Builder(requireContext(), 0).create()
        customDialog.apply {
            setView(dialogBinding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }.show()

        dialogBinding.btnHome.setOnClickListener {
            customDialog.dismiss()
        }

        dialogBinding.btnNext.setOnClickListener {
            timer.cancel()
            bn.tvAttempt.text = "0"
            bn.container.removeAllViews()
            images.clear()
            firstOpen = -1
            secondOpen = -1
            hideCount = 0
            openCount = 0
            viewModel.cardsLiveData.observe(viewLifecycleOwner, cardsObserver)
            viewModel.loadCardByLevel(level)
            customDialog.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        winSound?.stop()
        trueAnsSound?.stop()
        closeCardSound?.stop()
    }
}