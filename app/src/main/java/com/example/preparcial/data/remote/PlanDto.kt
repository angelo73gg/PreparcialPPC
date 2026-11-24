package com.example.preparcial.data.remote

import com.example.preparcial.domain.model.Miembro
import com.example.preparcial.domain.model.Pago
import com.example.preparcial.domain.model.PlanAhorro

/**
 * Data Transfer Objects (DTOs) para deserializar respuestas JSON de la API REST .NET.
 *
 * TODO: Ajustar las propiedades según el contrato JSON exacto del backend en .NET (Visual Studio).
 * Ejemplo: @SerializedName("id") val id: String
 */

data class PlanDto(
    val id: String? = null,
    val motivo: String? = null,
    val meta: Double? = null,
    val meses: Int? = null,
    val categoria: String? = null,
    val miembros: List<MiembroDto>? = null,
    val pagos: List<PagoDto>? = null
) {
    fun toDomain(): PlanAhorro {
        return PlanAhorro(
            id = id ?: "",
            motivo = motivo ?: "",
            meta = meta ?: 0.0,
            meses = meses ?: 0,
            categoria = categoria ?: "General",
            miembros = miembros?.map { it.toDomain() } ?: emptyList(),
            pagos = pagos?.map { it.toDomain() } ?: emptyList()
        )
    }
}

data class MiembroDto(
    val id: String? = null,
    val nombre: String? = null
) {
    fun toDomain(): Miembro {
        return Miembro(
            id = id ?: "",
            nombre = nombre ?: ""
        )
    }
}

data class PagoDto(
    val id: String? = null,
    val planId: String? = null,
    val miembroNombre: String? = null,
    val monto: Double? = null,
    val fecha: String? = null
) {
    fun toDomain(): Pago {
        return Pago(
            id = id ?: "",
            planId = planId ?: "",
            miembroNombre = miembroNombre ?: "",
            monto = monto ?: 0.0,
            fecha = fecha ?: ""
        )
    }
}
