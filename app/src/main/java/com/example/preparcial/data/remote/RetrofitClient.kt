package com.example.preparcial.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Retrofit configurado para conectar con el backend de FastAPI en VS Code.
 */
object RetrofitClient {

    // TODO: [PARCIAL] Si usas emulador Android Studio, la URL es "http://10.0.2.2:8000/"
    // TODO: [PARCIAL] Si usas dispositivo físico con 'adb reverse tcp:8000 tcp:8000', cambia a "http://localhost:8000/"
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
