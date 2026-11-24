package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.model.PlanAhorro
import com.example.preparcial.domain.repository.PlanRepository

/**
 * Caso de Uso: Crear un nuevo plan de ahorro con validaciones básicas de negocio.
 */
class CrearPlanUseCase(
    private val repository: PlanRepository
) {
    suspend operator fun invoke(
        motivo: String,
        meta: Double,
        meses: Int,
        miembros: List<String>
    ): Result<PlanAhorro> {
        // Validaciones de regla de negocio
        if (motivo.isBlank()) {
            return Result.failure(IllegalArgumentException("El motivo no puede estar vacío"))
        }
        if (meta <= 0) {
            return Result.failure(IllegalArgumentException("La meta debe ser un monto mayor a 0"))
        }
        if (meses <= 0) {
            return Result.failure(IllegalArgumentException("El plazo en meses debe ser mayor a 0"))
        }
        return repository.crearPlan(motivo.trim(), meta, meses, miembros)
    }
}
