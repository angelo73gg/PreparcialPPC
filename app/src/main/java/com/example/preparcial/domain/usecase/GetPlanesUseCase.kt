package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.PlanAhorro
import com.example.preparcial.domain.repository.PlanRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de Uso: Obtener el flujo reactivo con la lista de planes de ahorro.
 */
class GetPlanesUseCase(
    private val repository: PlanRepository
) {
    operator fun invoke(): Flow<List<PlanAhorro>> {
        return repository.getPlanes()
    }
}
