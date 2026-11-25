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

    /**
     * Porcentaje del progreso alcanzado (de 0.0 a 1.0) para la barra de progreso en la UI.
     */
    val porcentajeProgreso: Float
        get() = if (meta > 0) (totalRecaudado / meta).coerceIn(0.0, 1.0).toFloat() else 0f
}
