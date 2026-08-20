package com.example.ui.screens.youtube

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.CefrLevel
import com.example.data.model.YouTubeCategory
import com.example.data.model.YouTubeSearchFilter
import com.example.data.model.YouTubeVideoItem
import com.example.data.repository.YouTubeRepository
import com.example.data.repository.YouTubeSearchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class YouTubeSearchUiState(
    val query: String = "",
    val selectedLevel: CefrLevel? = null,
    val selectedCategory: YouTubeCategory = YouTubeCategory.CONVERSATION,
    val creativeCommonsOnly: Boolean = false,
    val isLoading: Boolean = false,
    val isLiveApi: Boolean = false,
    val videos: List<YouTubeVideoItem> = emptyList(),
    val errorMessage: String? = null,
    val isQuotaExceeded: Boolean = false,
    val noticeMessage: String? = null
)

class YouTubeSearchViewModel(
    private val repository: YouTubeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(YouTubeSearchUiState())
    val uiState: StateFlow<YouTubeSearchUiState> = _uiState.asStateFlow()

    init {
        performSearch()
    }

    fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
    }

    fun onLevelSelected(level: CefrLevel?) {
        _uiState.update { it.copy(selectedLevel = level) }
        performSearch()
    }

    fun onCategorySelected(category: YouTubeCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
        performSearch()
    }

    fun onToggleCreativeCommons(enabled: Boolean) {
        _uiState.update { it.copy(creativeCommonsOnly = enabled) }
        performSearch()
    }

    fun performSearch() {
        val currentState = _uiState.value
        val filter = YouTubeSearchFilter(
            query = currentState.query,
            level = currentState.selectedLevel,
            category = currentState.selectedCategory,
            creativeCommonsOnly = currentState.creativeCommonsOnly
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, noticeMessage = null) }
            val result = repository.searchYouTubeVideos(filter)
            when (result) {
                is YouTubeSearchResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            videos = result.videos,
                            isLiveApi = result.isLiveApi,
                            noticeMessage = result.notice,
                            errorMessage = null
                        )
                    }
                }
                is YouTubeSearchResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message,
                            isQuotaExceeded = result.isQuotaExceeded,
                            videos = result.fallbackVideos
                        )
                    }
                }
            }
        }
    }

    class Factory(private val repository: YouTubeRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return YouTubeSearchViewModel(repository) as T
        }
    }
}
