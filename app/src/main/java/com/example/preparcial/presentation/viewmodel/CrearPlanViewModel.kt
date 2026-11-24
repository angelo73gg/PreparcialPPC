package com.example.preparcial.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preparcial.domain.usecase.CrearPlanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CrearPlanUiState(
    val motivo: String = "",
    val meta: String = "",
    val meses: String = "",
    val miembrosText: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class CrearPlanViewModel(
    private val crearPlanUseCase: CrearPlanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrearPlanUiState())
    val uiState: StateFlow<CrearPlanUiState> = _uiState.asStateFlow()

    fun onMotivoChange(value: String) {
        _uiState.update { it.copy(motivo = value, error = null) }
    }

    fun onMetaChange(value: String) {
        _uiState.update { it.copy(meta = value, error = null) }
    }

    fun onMesesChange(value: String) {
        _uiState.update { it.copy(meses = value, error = null) }
    }

    fun onMiembrosChange(value: String) {
        _uiState.update { it.copy(miembrosText = value, error = null) }
    }

    fun crearPlan() {
        val currentState = _uiState.value
        val metaDouble = currentState.meta.toDoubleOrNull()
        val mesesInt = currentState.meses.toIntOrNull()

        if (currentState.motivo.isBlank()) {
            _uiState.update { it.copy(error = "Ingrese el motivo del plan") }
            return
        }
        if (metaDouble == null || metaDouble <= 0) {
            _uiState.update { it.copy(error = "Ingrese una meta válida (número mayor a 0)") }
            return
        }
        if (mesesInt == null || mesesInt <= 0) {
            _uiState.update { it.copy(error = "Ingrese el número de meses válido") }
            return
        }

        // Separa nombres por comas o saltos de línea
        val listaMiembros = currentState.miembrosText
            .split(",", "\n")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = crearPlanUseCase(
                motivo = currentState.motivo,
                meta = metaDouble,
                meses = mesesInt,
                miembros = if (listaMiembros.isEmpty()) listOf("Jorge", "Ana", "Luis") else listaMiembros
            )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { ex ->
                    _uiState.update { it.copy(isLoading = false, error = ex.message ?: "Error al crear el plan") }
                }
            )
        }
    }
}
