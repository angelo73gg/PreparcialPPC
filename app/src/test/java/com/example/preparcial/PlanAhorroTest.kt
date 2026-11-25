package com.example.preparcial

import com.example.preparcial.domain.model.Pago
import com.example.preparcial.domain.model.PlanAhorro
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pruebas unitarias para las reglas de cálculo en la entidad PlanAhorro.
 */
class PlanAhorroTest {

    @Test
    fun `calcular totalRecaudado suma correctamente los montos de los pagos`() {
        val plan = PlanAhorro(
            id = "1",
            motivo = "Viaje familiar",
            meta = 1000.0,
            meses = 5,
            pagos = listOf(
                Pago("p1", "1", "Jorge", 200.0),
                Pago("p2", "1", "Ana", 300.0)
            )
        )

        assertEquals(500.0, plan.totalRecaudado, 0.001)
    }

    @Test
    fun `porcentajeProgreso se calcula correctamente relativo a la meta`() {
        val plan = PlanAhorro(
            id = "1",
            motivo = "Viaje familiar",
            meta = 1000.0,
            meses = 5,
            pagos = listOf(
                Pago("p1", "1", "Jorge", 500.0)
            )
        )

        assertEquals(0.5f, plan.porcentajeProgreso, 0.001f)
    }
}
