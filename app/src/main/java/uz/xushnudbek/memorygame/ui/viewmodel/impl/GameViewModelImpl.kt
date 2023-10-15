package uz.xushnudbek.memorygame.ui.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.xushnudbek.memorygame.data.CardData
import uz.xushnudbek.memorygame.data.LevelEnum
import uz.xushnudbek.memorygame.repository.AppRepository
import uz.xushnudbek.memorygame.ui.viewmodel.GameViewModel

class GameViewModelImpl(private val repository: AppRepository) : ViewModel(), GameViewModel {
    override val cardsLiveData = MutableLiveData<List<CardData>>()

    override fun loadCardByLevel(level: LevelEnum) {
        cardsLiveData.value = repository.getCardDataByLevel(level)
    }
}