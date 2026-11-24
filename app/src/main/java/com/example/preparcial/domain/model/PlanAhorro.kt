package com.example.preparcial.domain.model

/**
 * Modelo de Dominio principal que representa un Plan de Ahorro Familiar.
 */
data class PlanAhorro(
    val id: String,
    val motivo: String,
    val meta: Double,
    val meses: Int,
    val categoria: String = "General",
    val miembros: List<Miembro> = emptyList(),
    val pagos: List<Pago> = emptyList()
) {
    /**
     * Calcula la suma total de los pagos registrados para este plan.
     */
    val totalRecaudado: Double
        get() = pagos.sumOf { it.monto }
}
