package uz.xushnudbek.memorygame.ui.viewmodel

import androidx.lifecycle.LiveData
import uz.xushnudbek.memorygame.data.CardData
import uz.xushnudbek.memorygame.data.LevelEnum

interface GameViewModel {
    val cardsLiveData: LiveData<List<CardData>>

    fun loadCardByLevel(level: LevelEnum)
}