package uz.xushnudbek.memorygame.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.xushnudbek.memorygame.repository.AppRepository
import uz.xushnudbek.memorygame.ui.viewmodel.impl.GameViewModelImpl

class GameViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((GameViewModelImpl::class.java)))
            return GameViewModelImpl(AppRepository.getInstance()) as T

        throw java.lang.IllegalArgumentException()
    }
}