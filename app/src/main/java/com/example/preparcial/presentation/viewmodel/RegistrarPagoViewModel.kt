package com.example.preparcial.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.preparcial.domain.model.PlanAhorro
import com.example.preparcial.domain.usecase.GetPlanByIdUseCase
import com.example.preparcial.domain.usecase.RegistrarPagoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrarPagoUiState(
    val plan: PlanAhorro? = null,
    val miembroSeleccionado: String = "",
    val monto: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class RegistrarPagoViewModel(
    private val getPlanByIdUseCase: GetPlanByIdUseCase,
    private val registrarPagoUseCase: RegistrarPagoUseCase,
    private val planId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrarPagoUiState(isLoading = true))
    val uiState: StateFlow<RegistrarPagoUiState> = _uiState.asStateFlow()

    init {
        cargarPlan()
    }

    private fun cargarPlan() {
        viewModelScope.launch {
            getPlanByIdUseCase(planId).collect { plan ->
                _uiState.update {
                    it.copy(
                        plan = plan,
                        miembroSeleccionado = plan?.miembros?.firstOrNull()?.nombre ?: "",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onMiembroSeleccionadoChange(nombre: String) {
        _uiState.update { it.copy(miembroSeleccionado = nombre, error = null) }
    }

    fun onMontoChange(value: String) {
        _uiState.update { it.copy(monto = value, error = null) }
    }

    fun registrarPago() {
        val currentState = _uiState.value
        val montoDouble = currentState.monto.toDoubleOrNull()

        if (currentState.miembroSeleccionado.isBlank()) {
            _uiState.update { it.copy(error = "Seleccione un miembro") }
            return
        }

        if (montoDouble == null || montoDouble <= 0) {
            _uiState.update { it.copy(error = "Ingrese un monto válido mayor a 0") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = registrarPagoUseCase(
                planId = planId,
                miembroNombre = currentState.miembroSeleccionado,
                monto = montoDouble
            )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { ex ->
                    _uiState.update { it.copy(isLoading = false, error = ex.message ?: "Error al registrar el pago") }
                }
            )
        }
    }
}
