package com.example.preparcial.data.repository

import com.example.preparcial.data.remote.ApiService
import com.example.preparcial.data.remote.CrearPlanRequestDto
import com.example.preparcial.data.remote.RegistrarPagoRequestDto
import com.example.preparcial.domain.model.Pago
import com.example.preparcial.domain.model.PlanAhorro
import com.example.preparcial.domain.repository.PlanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementación Remota del Repositorio consumiendo la API REST de FastAPI mediante Retrofit.
 */
class RemotePlanRepositoryImpl(
    private val apiService: ApiService
) : PlanRepository {

    override fun getPlanes(): Flow<List<PlanAhorro>> = flow {
        try {
            val response = apiService.getPlanes()
            if (response.isSuccessful) {
                val apiResponse = response.body()
                val listaDomain = apiResponse?.data?.map { it.toDomain() } ?: emptyList()
                emit(listaDomain)
            } else {
                throw Exception("Error HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Error de red/servidor al obtener planes: ${e.localizedMessage}")
        }
    }

    override fun getPlanById(id: String): Flow<PlanAhorro?> = flow {
        try {
            val response = apiService.getPlanById(id)
            if (response.isSuccessful) {
                val planDto = response.body()?.data
                emit(planDto?.toDomain())
            } else {
                throw Exception("Error HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Error al obtener detalle del plan: ${e.localizedMessage}")
        }
    }

    override fun getPagosByPlanId(planId: String): Flow<List<Pago>> = flow {
        try {
            val response = apiService.getPagosByPlanId(planId)
            if (response.isSuccessful) {
                val pagosDto = response.body()?.data
                emit(pagosDto?.map { it.toDomain() } ?: emptyList())
            } else {
                // Fallback si la API incluye los pagos directamente en el detalle del plan
                val planResponse = apiService.getPlanById(planId)
                val pagos = planResponse.body()?.data?.pagos?.map { it.toDomain() } ?: emptyList()
                emit(pagos)
            }
        } catch (_: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun crearPlan(
        motivo: String,
        meta: Double,
        meses: Int,
        miembrosNombres: List<String>
    ): Result<PlanAhorro> {
        return try {
            val request = CrearPlanRequestDto(
                motivo = motivo,
                meta = meta,
                meses = meses,
                miembros = miembrosNombres
            )
            val response = apiService.crearPlan(request)
            if (response.isSuccessful) {
                val planCreado = response.body()?.data?.toDomain()
                if (planCreado != null) {
                    Result.success(planCreado)
                } else {
                    Result.failure(Exception("La respuesta del servidor no devolvió datos del plan"))
                }
            } else {
                Result.failure(Exception("Error al crear plan (${response.code()}): ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión al crear plan: ${e.localizedMessage}"))
        }
    }

    override suspend fun registrarPago(
        planId: String,
        miembroNombre: String,
        monto: Double
    ): Result<Unit> {
        return try {
            val request = RegistrarPagoRequestDto(
                miembroNombre = miembroNombre,
                monto = monto
            )
            val response = apiService.registrarPago(planId, request)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al registrar pago (${response.code()}): ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión al registrar pago: ${e.localizedMessage}"))
        }
    }
}
