package com.example.ui.screens.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.PhraseHistoryEntity
import com.example.data.local.UserMistakeEntity
import com.example.data.local.UserStatsEntity
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ProgressUiState(
    val stats: UserStatsEntity = UserStatsEntity(),
    val totalWordsLearned: Int = 0,
    val masteredWordsCount: Int = 0,
    val recentHistory: List<PhraseHistoryEntity> = emptyList(),
    val topMistakes: List<UserMistakeEntity> = emptyList(),
    val completedScenesCount: Int = 0,
    val totalScenesCount: Int = 0
)

class ProgressViewModel(private val repository: AppRepository) : ViewModel() {

    val uiState: StateFlow<ProgressUiState> = combine(
        repository.getUserStats(),
        repository.getAllVocabulary(),
        repository.getAllProgress(),
        repository.getRecentHistory(),
        repository.getTopMistakes()
    ) { stats, vocabList, progressList, history, mistakes ->
        val safeStats = stats ?: UserStatsEntity()
        val allScenes = repository.getAllScenes()
        val completedScenes = progressList.count { it.isCompleted }

        ProgressUiState(
            stats = safeStats,
            totalWordsLearned = vocabList.size,
            masteredWordsCount = vocabList.count { it.isMastered },
            recentHistory = history,
            topMistakes = mistakes,
            completedScenesCount = completedScenes,
            totalScenesCount = allScenes.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProgressUiState()
    )

    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProgressViewModel(repository) as T
        }
    }
}
