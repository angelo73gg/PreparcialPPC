package com.example.preparcial.domain.repository

import com.example.preparcial.domain.model.PlanAhorro
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del Repositorio de Dominio.
 * Define las operaciones abstractas para obtener y manipular datos de planes de ahorro.
 * La implementación concreta (data/repository) decidirá si los datos vienen de una API REST .NET o de Mock in-memory.
 */
interface PlanRepository {
    fun getPlanes(): Flow<List<PlanAhorro>>
    fun getPlanById(id: String): Flow<PlanAhorro?>
    suspend fun crearPlan(motivo: String, meta: Double, meses: Int, miembrosNombres: List<String>): Result<PlanAhorro>
    suspend fun registrarPago(planId: String, miembroNombre: String, monto: Double): Result<Unit>
}
