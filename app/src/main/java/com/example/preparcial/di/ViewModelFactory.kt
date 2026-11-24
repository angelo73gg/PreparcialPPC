package com.example.preparcial.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.preparcial.presentation.viewmodel.CrearPlanViewModel
import com.example.preparcial.presentation.viewmodel.DetallePlanViewModel
import com.example.preparcial.presentation.viewmodel.HomeViewModel
import com.example.preparcial.presentation.viewmodel.RegistrarPagoViewModel

/**
 * ViewModelProvider.Factory personalizado para instanciar ViewModels con inyección manual de dependencias.
 */
class ViewModelFactory(
    private val container: AppContainer,
    private val planId: String? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(container.getPlanesUseCase) as T
            }
            modelClass.isAssignableFrom(CrearPlanViewModel::class.java) -> {
                CrearPlanViewModel(container.crearPlanUseCase) as T
            }
            modelClass.isAssignableFrom(DetallePlanViewModel::class.java) -> {
                requireNotNull(planId) { "planId no puede ser nulo para DetallePlanViewModel" }
                DetallePlanViewModel(container.getPlanByIdUseCase, planId) as T
            }
            modelClass.isAssignableFrom(RegistrarPagoViewModel::class.java) -> {
                requireNotNull(planId) { "planId no puede ser nulo para RegistrarPagoViewModel" }
                RegistrarPagoViewModel(
                    container.getPlanByIdUseCase,
                    container.registrarPagoUseCase,
                    planId
                ) as T
            }
            else -> throw IllegalArgumentException("ViewModel desaprobado / no reconocido: ${modelClass.name}")
        }
    }
}
