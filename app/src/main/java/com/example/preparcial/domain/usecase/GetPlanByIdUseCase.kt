package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.PlanAhorro
import com.example.preparcial.domain.repository.PlanRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de Uso: Obtener un plan de ahorro específico por su ID.
 */
class GetPlanByIdUseCase(
    private val repository: PlanRepository
) {
    operator fun invoke(id: String): Flow<PlanAhorro?> {
        return repository.getPlanById(id)
    }
}
