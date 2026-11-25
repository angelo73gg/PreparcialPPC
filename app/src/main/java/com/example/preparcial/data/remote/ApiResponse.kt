package com.example.preparcial.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Envoltorio genérico estándar para respuestas HTTP de FastAPI / Pydantic v2.
 *
 * TODO: [PARCIAL] Si tu API en FastAPI devuelve los objetos o listas directamente sin envoltorio
 * (ejemplo: List<PlanDto> en lugar de {"status": true, "data": [...]}), ajusta los retornos
 * en ApiService.kt eliminando ApiResponse<T>.
 */
data class ApiResponse<T>(
    @SerializedName("status") val status: Boolean = true,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null
)
