package com.example.preparcial.di

import com.example.preparcial.data.remote.RetrofitClient
import com.example.preparcial.data.repository.MockPlanRepositoryImpl
import com.example.preparcial.data.repository.RemotePlanRepositoryImpl
import com.example.preparcial.domain.repository.PlanRepository
import com.example.preparcial.domain.usecase.CrearPlanUseCase
import com.example.preparcial.domain.usecase.GetPlanByIdUseCase
import com.example.preparcial.domain.usecase.GetPlanesUseCase
import com.example.preparcial.domain.usecase.RegistrarPagoUseCase

/**
 * Contenedor de Inyección de Dependencias Manual (AppContainer).
 * Provee instancias de repositorios y casos de uso en toda la aplicación de manera simple y robusta.
 */
class AppContainer {

    companion object {
        /**
         * TODO: [PARCIAL] INTERRUPTOR PRINCIPAL PARA EL EXAMEN:
         * Setear en 'true'  -> Usa datos simulados (Mock) en memoria para probar la UI offline.
         * Setear en 'false' -> Usa Retrofit para conectar con FastAPI en http://10.0.2.2:8000/
         */
        private const val USE_MOCK_DATA = true
    }

    // Selecciona el repositorio según el interruptor USE_MOCK_DATA
    val planRepository: PlanRepository by lazy {
        if (USE_MOCK_DATA) {
            MockPlanRepositoryImpl()
        } else {
            RemotePlanRepositoryImpl(RetrofitClient.apiService)
        }
    }

    // Casos de uso de la capa Domain (Consumen la abstracción del repositorio)
    val getPlanesUseCase: GetPlanesUseCase by lazy {
        GetPlanesUseCase(planRepository)
    }

    val getPlanByIdUseCase: GetPlanByIdUseCase by lazy {
        GetPlanByIdUseCase(planRepository)
    }

    val crearPlanUseCase: CrearPlanUseCase by lazy {
        CrearPlanUseCase(planRepository)
    }

    val registrarPagoUseCase: RegistrarPagoUseCase by lazy {
        RegistrarPagoUseCase(planRepository)
    }
}
