package com.example.preparcial.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preparcial.domain.model.PlanAhorro
import com.example.preparcial.domain.usecase.GetPlanesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = false,
    val planes: List<PlanAhorro> = emptyList(),
    val error: String? = null
)

class HomeViewModel(
    private val getPlanesUseCase: GetPlanesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        cargarPlanes()
    }

    fun cargarPlanes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getPlanesUseCase()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al cargar planes") }
                }
                .collect { listaPlanes ->
                    _uiState.update { it.copy(isLoading = false, planes = listaPlanes, error = null) }
                }
        }
    }
}
