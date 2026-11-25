package com.example.preparcial.domain.repository

import com.example.preparcial.domain.model.Pago
import com.example.preparcial.domain.model.PlanAhorro
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del Repositorio de Dominio.
 * Define las operaciones abstractas para obtener y manipular datos de planes de ahorro.
 */
interface PlanRepository {
    fun getPlanes(): Flow<List<PlanAhorro>>
    fun getPlanById(id: String): Flow<PlanAhorro?>
    fun getPagosByPlanId(planId: String): Flow<List<Pago>>
    suspend fun crearPlan(motivo: String, meta: Double, meses: Int, miembrosNombres: List<String>): Result<PlanAhorro>
    suspend fun registrarPago(planId: String, miembroNombre: String, monto: Double): Result<Unit>
}
