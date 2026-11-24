package com.example.preparcial.presentation.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.preparcial.di.AppContainer
import com.example.preparcial.di.ViewModelFactory
import com.example.preparcial.presentation.ui.screens.CrearPlanScreen
import com.example.preparcial.presentation.ui.screens.DetallePlanScreen
import com.example.preparcial.presentation.ui.screens.HomeScreen
import com.example.preparcial.presentation.ui.screens.RegistrarPagoScreen
import com.example.preparcial.presentation.viewmodel.CrearPlanViewModel
import com.example.preparcial.presentation.viewmodel.DetallePlanViewModel
import com.example.preparcial.presentation.viewmodel.HomeViewModel
import com.example.preparcial.presentation.viewmodel.RegistrarPagoViewModel

/**
 * Gestor de Navegación de la Aplicación.
 * Maneja la pila de navegación (backstack) de forma reactiva y limpia.
 */
@Composable
fun AppNavGraph(
    container: AppContainer
) {
    // Pila de pantallas (Backstack) iniciada en Home
    val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }
    val currentScreen = backStack.lastOrNull() ?: Screen.Home

    // Manejo de botón 'Atrás' del sistema
    BackHandler(enabled = backStack.size > 1) {
        backStack.removeAt(backStack.size - 1)
    }

    when (currentScreen) {
        is Screen.Home -> {
            val viewModel: HomeViewModel = viewModel(
                factory = ViewModelFactory(container)
            )
            HomeScreen(
                viewModel = viewModel,
                onNavigateToCrearPlan = {
                    backStack.add(Screen.CrearPlan)
                },
                onNavigateToDetallePlan = { planId ->
                    backStack.add(Screen.DetallePlan(planId))
                }
            )
        }
        is Screen.CrearPlan -> {
            val viewModel: CrearPlanViewModel = viewModel(
                factory = ViewModelFactory(container)
            )
            CrearPlanScreen(
                viewModel = viewModel,
                onPlanCreado = {
                    backStack.removeAt(backStack.size - 1)
                },
                onNavigateBack = {
                    backStack.removeAt(backStack.size - 1)
                }
            )
        }
        is Screen.DetallePlan -> {
            val planId = currentScreen.planId
            val viewModel: DetallePlanViewModel = viewModel(
                key = "DetallePlanViewModel_$planId",
                factory = ViewModelFactory(container, planId = planId)
            )
            DetallePlanScreen(
                viewModel = viewModel,
                onNavigateToRegistrarPago = { targetPlanId ->
                    backStack.add(Screen.RegistrarPago(targetPlanId))
                },
                onNavigateBack = {
                    backStack.removeAt(backStack.size - 1)
                }
            )
        }
        is Screen.RegistrarPago -> {
            val planId = currentScreen.planId
            val viewModel: RegistrarPagoViewModel = viewModel(
                key = "RegistrarPagoViewModel_$planId",
                factory = ViewModelFactory(container, planId = planId)
            )
            RegistrarPagoScreen(
                viewModel = viewModel,
                onPagoRegistrado = {
                    backStack.removeAt(backStack.size - 1)
                },
                onNavigateBack = {
                    backStack.removeAt(backStack.size - 1)
                }
            )
        }
    }
}
