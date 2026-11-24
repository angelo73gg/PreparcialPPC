package com.example.preparcial.presentation.ui.navigation

/**
 * Rutas de navegación de la aplicación usando una Sealed Class.
 * Permite una navegación rápida, segura y reactiva entre pantallas.
 */
sealed class Screen {
    data object Home : Screen()
    data object CrearPlan : Screen()
    data class DetallePlan(val planId: String) : Screen()
    data class RegistrarPago(val planId: String) : Screen()
}
