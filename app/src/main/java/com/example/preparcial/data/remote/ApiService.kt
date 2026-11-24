package com.example.preparcial.data.remote

/**
 * Plantilla de interfaz Retrofit para consumir el servicio web .NET en Visual Studio.
 *
 * TODO (Pasos para conectar en el examen sin IA):
 * 1. Asegurarse de tener Retrofit y GsonConverter en build.gradle.kts:
 *    implementation("com.squareup.retrofit2:retrofit:2.11.0")
 *    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
 * 2. Agregar permiso de internet en AndroidManifest.xml:
 *    <uses-permission android:name="android.permission.INTERNET" />
 * 3. Si se prueba en local (Emulator), usar BASE_URL = "http://10.0.2.2:5000/" (o el puerto de tu API .NET IIS Express/Kestrel).
 * 4. Descomentar los endpoints y las anotaciones Retrofit abajo.
 */

/*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("api/planes")
    suspend fun getPlanes(): Response<List<PlanDto>>

    @GET("api/planes/{id}")
    suspend fun getPlanById(@Path("id") id: String): Response<PlanDto>

    @POST("api/planes")
    suspend fun crearPlan(@Body planDto: PlanDto): Response<PlanDto>

    @POST("api/planes/{id}/pagos")
    suspend fun registrarPago(@Path("id") planId: String, @Body pagoDto: PagoDto): Response<Unit>
}
*/

interface ApiService {
    // Definición vacía por defecto para compilar en modo offline sin conexión Retrofit
}
