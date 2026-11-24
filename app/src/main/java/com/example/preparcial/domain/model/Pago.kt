package com.example.preparcial.domain.model

/**
 * Modelo de Dominio que representa un pago registrado en un plan de ahorro.
 */
data class Pago(
    val id: String,
    val planId: String,
    val miembroNombre: String,
    val monto: Double,
    val fecha: String = ""
)
