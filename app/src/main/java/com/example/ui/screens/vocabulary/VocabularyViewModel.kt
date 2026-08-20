package com.example.ui.screens.vocabulary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.VocabularyEntity
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class VocabularyUiState(
    val items: List<VocabularyEntity> = emptyList(),
    val searchQuery: String = "",
    val selectedTypeFilter: String = "ALL", // "ALL", "CONTRACTION", "EXPRESSION", "MASTERED"
    val totalCount: Int = 0,
    val masteredCount: Int = 0
)

class VocabularyViewModel(private val repository: AppRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedTypeFilter = MutableStateFlow("ALL")
    val selectedTypeFilter: StateFlow<String> = _selectedTypeFilter.asStateFlow()

    val uiState: StateFlow<VocabularyUiState> = combine(
        repository.getAllVocabulary(),
        _searchQuery,
        _selectedTypeFilter
    ) { allItems, query, filter ->
        val filtered = allItems.filter { item ->
            val matchesQuery = query.isBlank() ||
                    item.term.contains(query, ignoreCase = true) ||
                    item.meaning.contains(query, ignoreCase = true) ||
                    item.exampleSentence.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "CONTRACTION" -> item.itemType == "CONTRACTION"
                "EXPRESSION" -> item.itemType == "EXPRESSION"
                "MASTERED" -> item.isMastered
                else -> true
            }

            matchesQuery && matchesFilter
        }

        VocabularyUiState(
            items = filtered,
            searchQuery = query,
            selectedTypeFilter = filter,
            totalCount = allItems.size,
            masteredCount = allItems.count { it.isMastered }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = VocabularyUiState()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun selectTypeFilter(filter: String) {
        _selectedTypeFilter.value = filter
    }

    fun toggleMastered(item: VocabularyEntity) {
        viewModelScope.launch {
            repository.toggleVocabularyMastered(item.id, item.isMastered)
        }
    }

    class Factory(private val repository: AppRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VocabularyViewModel(repository) as T
        }
    }
}
