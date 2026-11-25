package com.example.preparcial.data.repository

import com.example.preparcial.domain.model.Miembro
import com.example.preparcial.domain.model.Pago
import com.example.preparcial.domain.model.PlanAhorro
import com.example.preparcial.domain.repository.PlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

/**
 * Implementación MOCK en memoria del Repositorio.
 */
class MockPlanRepositoryImpl : PlanRepository {

    private val planesFlow = MutableStateFlow<List<PlanAhorro>>(
        listOf(
            PlanAhorro(
                id = "1",
                motivo = "Ahorro para viaje familiar",
                meta = 5000000.0,
                meses = 12,
                categoria = "Viajes",
                miembros = listOf(
                    Miembro("m1", "Jorge"),
                    Miembro("m2", "Ana"),
                    Miembro("m3", "Luis")
                ),
                pagos = listOf(
                    Pago("p1", "1", "Jorge", 500000.0, "2025-01-10"),
                    Pago("p2", "1", "Ana", 500000.0, "2025-01-12")
                )
            )
        )
    )

    override fun getPlanes(): Flow<List<PlanAhorro>> = planesFlow

    override fun getPlanById(id: String): Flow<PlanAhorro?> {
        return planesFlow.map { lista -> lista.find { it.id == id } }
    }

    override fun getPagosByPlanId(planId: String): Flow<List<Pago>> {
        return planesFlow.map { lista ->
            lista.find { it.id == planId }?.pagos ?: emptyList()
        }
    }

    override suspend fun crearPlan(
        motivo: String,
        meta: Double,
        meses: Int,
        miembrosNombres: List<String>
    ): Result<PlanAhorro> {
        return try {
            val nuevosMiembros = miembrosNombres.map { nombre ->
                Miembro(id = UUID.randomUUID().toString(), nombre = nombre.trim())
            }

            val nuevoPlan = PlanAhorro(
                id = UUID.randomUUID().toString(),
                motivo = motivo,
                meta = meta,
                meses = meses,
                categoria = "General",
                miembros = nuevosMiembros,
                pagos = emptyList()
            )

            planesFlow.update { listaActual -> listaActual + nuevoPlan }
            Result.success(nuevoPlan)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registrarPago(
        planId: String,
        miembroNombre: String,
        monto: Double
    ): Result<Unit> {
        return try {
            val nuevoPago = Pago(
                id = UUID.randomUUID().toString(),
                planId = planId,
                miembroNombre = miembroNombre,
                monto = monto,
                fecha = "2025-02-23"
            )

            planesFlow.update { listaActual ->
                listaActual.map { plan ->
                    if (plan.id == planId) {
                        plan.copy(pagos = plan.pagos + nuevoPago)
                    } else {
                        plan
                    }
                }
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
