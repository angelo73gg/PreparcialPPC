package com.example.preparcial.domain.usecase

import com.example.preparcial.domain.repository.PlanRepository

/**
 * Caso de Uso: Registrar un pago de ahorro para un miembro específico dentro de un plan.
 */
class RegistrarPagoUseCase(
    private val repository: PlanRepository
) {
    suspend operator fun invoke(
        planId: String,
        miembroNombre: String,
        monto: Double
    ): Result<Unit> {
        if (planId.isBlank()) {
            return Result.failure(IllegalArgumentException("El ID del plan es requerido"))
        }
        if (miembroNombre.isBlank()) {
            return Result.failure(IllegalArgumentException("Debe seleccionar un miembro"))
        }
        if (monto <= 0) {
            return Result.failure(IllegalArgumentException("El monto debe ser mayor a 0"))
        }
        return repository.registrarPago(planId, miembroNombre, monto)
    }
}
