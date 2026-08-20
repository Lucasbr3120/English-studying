package com.example.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.SceneProgressEntity
import com.example.data.local.UserStatsEntity
import com.example.data.model.CefrLevel
import com.example.data.model.Scene
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val userStats: UserStatsEntity = UserStatsEntity(),
    val selectedLevel: CefrLevel = CefrLevel.A1,
    val scenesForLevel: List<Scene> = emptyList(),
    val progressMap: Map<String, SceneProgressEntity> = emptyMap(),
    val lastStudiedScene: Scene? = null,
    val overallProgressPercent: Int = 0,
    val totalAvailablePhrases: Int = 0
)

class HomeViewModel(private val repository: AppRepository) : ViewModel() {

    private val _selectedLevel = MutableStateFlow(CefrLevel.A1)
    val selectedLevel: StateFlow<CefrLevel> = _selectedLevel.asStateFlow()

    val uiState: StateFlow<HomeUiState> = combine(
        repository.getUserStats(),
        repository.getAllProgress(),
        _selectedLevel,
        repository.customScenes
    ) { stats, progressList, level, _ ->
        val safeStats = stats ?: UserStatsEntity()
        val allScenes = repository.getAllScenes()
        val scenesForLevel = allScenes.filter { it.level == level }
        val progMap = progressList.associateBy { it.sceneId }

        val totalPhrasesInApp = allScenes.sumOf { it.phrases.size }
        val totalCompleted = progressList.sumOf { it.completedPhrases }
        val overallPercent = if (totalPhrasesInApp > 0) {
            ((totalCompleted.toFloat() / totalPhrasesInApp.toFloat()) * 100).toInt().coerceIn(0, 100)
        } else 0

        val lastProgress = progressList.maxByOrNull { it.lastStudiedTimestamp }
        val lastScene = lastProgress?.let { prog -> allScenes.firstOrNull { it.id == prog.sceneId } }
            ?: allScenes.firstOrNull()

        HomeUiState(
            userStats = safeStats,
            selectedLevel = level,
            scenesForLevel = scenesForLevel,
            progressMap = progMap,
            lastStudiedScene = lastScene,
            overallProgressPercent = overallPercent,
            totalAvailablePhrases = totalPhrasesInApp
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun selectLevel(level: CefrLevel) {
        _selectedLevel.value = level
        viewModelScope.launch {
            repository.updateUserLevel(level.code)
        }
    }

    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository) as T
        }
    }
}
