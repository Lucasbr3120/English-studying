package com.example.ui.screens.scenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.SceneProgressEntity
import com.example.data.model.CefrLevel
import com.example.data.model.Scene
import com.example.data.model.SceneCategory
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ScenesUiState(
    val scenes: List<Scene> = emptyList(),
    val progressMap: Map<String, SceneProgressEntity> = emptyMap(),
    val selectedLevelFilter: CefrLevel? = null,
    val selectedCategoryFilter: SceneCategory? = null,
    val searchQuery: String = ""
)

class ScenesViewModel(private val repository: AppRepository) : ViewModel() {

    private val _selectedLevelFilter = MutableStateFlow<CefrLevel?>(null)
    val selectedLevelFilter: StateFlow<CefrLevel?> = _selectedLevelFilter.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<SceneCategory?>(null)
    val selectedCategoryFilter: StateFlow<SceneCategory?> = _selectedCategoryFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val uiState: StateFlow<ScenesUiState> = combine(
        _selectedLevelFilter,
        _selectedCategoryFilter,
        _searchQuery,
        repository.getAllProgress(),
        repository.customScenes
    ) { levelFilter, categoryFilter, query, progressList, _ ->
        val allScenes = repository.getAllScenes()
        val filtered = allScenes.filter { scene ->
            val matchesLevel = levelFilter == null || scene.level == levelFilter
            val matchesCategory = categoryFilter == null || scene.category == categoryFilter
            val matchesQuery = query.isBlank() ||
                    scene.title.contains(query, ignoreCase = true) ||
                    scene.genre.contains(query, ignoreCase = true) ||
                    scene.contextDescription.contains(query, ignoreCase = true) ||
                    scene.mainVocabulary.any { it.contains(query, ignoreCase = true) } ||
                    scene.characters.any { it.contains(query, ignoreCase = true) } ||
                    scene.expressions.any { it.contains(query, ignoreCase = true) }
            matchesLevel && matchesCategory && matchesQuery
        }

        ScenesUiState(
            scenes = filtered,
            progressMap = progressList.associateBy { it.sceneId },
            selectedLevelFilter = levelFilter,
            selectedCategoryFilter = categoryFilter,
            searchQuery = query
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ScenesUiState()
    )

    fun selectLevelFilter(level: CefrLevel?) {
        _selectedLevelFilter.value = level
    }

    fun selectCategoryFilter(category: SceneCategory?) {
        _selectedCategoryFilter.value = category
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ScenesViewModel(repository) as T
        }
    }
}
