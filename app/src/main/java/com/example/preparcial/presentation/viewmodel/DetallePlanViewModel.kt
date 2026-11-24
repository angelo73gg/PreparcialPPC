package com.example.preparcial.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preparcial.domain.model.PlanAhorro
import com.example.preparcial.domain.usecase.GetPlanByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetallePlanUiState(
    val isLoading: Boolean = true,
    val plan: PlanAhorro? = null,
    val error: String? = null
)

class DetallePlanViewModel(
    private val getPlanByIdUseCase: GetPlanByIdUseCase,
    private val planId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetallePlanUiState())
    val uiState: StateFlow<DetallePlanUiState> = _uiState.asStateFlow()

    init {
        cargarDetalle()
    }

    fun cargarDetalle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getPlanByIdUseCase(planId)
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error al cargar el detalle del plan") }
                }
                .collect { planEncontrado ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            plan = planEncontrado,
                            error = if (planEncontrado == null) "Plan no encontrado" else null
                        )
                    }
                }
        }
    }
}
