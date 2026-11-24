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
 * Implementación del Repositorio.
 * Actualmente funciona con datos SIMULADOS en memoria (Mock) para desarrollo/examen offline.
 *
 * TODO: Para conectar la API REST real en .NET:
 * 1. Inyectar `private val apiService: ApiService` en el constructor.
 * 2. Reemplazar la manipulación del `planesFlow` por llamadas `apiService.getPlanes()`, `apiService.crearPlan(...)`, etc.
 */
class PlanRepositoryImpl : PlanRepository {

    // Simulación de base de datos en memoria usando Flow reactivo
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

    override fun getPlanes(): Flow<List<PlanAhorro>> {
        // TODO: Retornar flow { emit(apiService.getPlanes().body()?.map { it.toDomain() } ?: emptyList()) }
        return planesFlow
    }

    override fun getPlanById(id: String): Flow<PlanAhorro?> {
        // TODO: Llamar a apiService.getPlanById(id)
        return planesFlow.map { lista -> lista.find { it.id == id } }
    }

    override suspend fun crearPlan(
        motivo: String,
        meta: Double,
        meses: Int,
        miembrosNombres: List<String>
    ): Result<PlanAhorro> {
        return try {
            // TODO: Enviar PlanDto a apiService.crearPlan(dto)
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

            planesFlow.update { listaActual ->
                listaActual + nuevoPlan
            }

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
            // TODO: Enviar PagoDto a apiService.registrarPago(planId, pagoDto)
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
