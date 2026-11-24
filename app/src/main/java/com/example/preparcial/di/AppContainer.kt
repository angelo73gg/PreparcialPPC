package com.example.preparcial.di

import com.example.preparcial.data.repository.PlanRepositoryImpl
import com.example.preparcial.domain.repository.PlanRepository
import com.example.preparcial.domain.usecase.CrearPlanUseCase
import com.example.preparcial.domain.usecase.GetPlanByIdUseCase
import com.example.preparcial.domain.usecase.GetPlanesUseCase
import com.example.preparcial.domain.usecase.RegistrarPagoUseCase

/**
 * Contenedor de Inyección de Dependencias Manual (AppContainer).
 * Provee instancias de repositorios y casos de uso en toda la aplicación de manera simple y robusta.
 * No requiere plugins ni dependencias externas de Gradle.
 */
class AppContainer {

    // Repositorio principal (Actualmente Mock in-memory)
    // TODO: Si se conecta Retrofit, pasar ApiService a PlanRepositoryImpl(apiService)
    val planRepository: PlanRepository by lazy {
        PlanRepositoryImpl()
    }

    // Casos de uso de la capa Domain
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
