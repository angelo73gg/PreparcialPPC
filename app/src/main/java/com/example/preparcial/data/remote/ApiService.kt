package com.example.preparcial.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz de Retrofit para consumir la API REST en FastAPI (Python / VS Code).
 */
interface ApiService {

    @GET("api/planes")
    suspend fun getPlanes(): Response<ApiResponse<List<PlanDto>>>

    @GET("api/planes/{id}")
    suspend fun getPlanById(@Path("id") id: String): Response<ApiResponse<PlanDto>>

    @GET("api/planes/{id}/pagos")
    suspend fun getPagosByPlanId(@Path("id") planId: String): Response<ApiResponse<List<PagoDto>>>

    @POST("api/planes")
    suspend fun crearPlan(@Body request: CrearPlanRequestDto): Response<ApiResponse<PlanDto>>

    @POST("api/planes/{id}/pagos")
    suspend fun registrarPago(
        @Path("id") planId: String,
        @Body request: RegistrarPagoRequestDto
    ): Response<ApiResponse<Unit>>
}
