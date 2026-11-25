package com.example.preparcial.data.remote

import com.example.preparcial.domain.model.Miembro
import com.example.preparcial.domain.model.Pago
import com.example.preparcial.domain.model.PlanAhorro
import com.google.gson.annotations.SerializedName

/**
 * DTOs (Data Transfer Objects) para deserializar JSON devuelto por FastAPI (Pydantic v2).
 *
 * TODO: [PARCIAL] Ajustar los nombres de los atributos en @SerializedName(...) si la API de tu parcial
 * utiliza nombres en inglés o snake_case (ejemplo: @SerializedName("plan_id") val id: String?).
 */

data class PlanDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("motivo") val motivo: String? = null,
    @SerializedName("meta") val meta: Double? = null,
    @SerializedName("meses") val meses: Int? = null,
    @SerializedName("categoria") val categoria: String? = null,
    @SerializedName("miembros") val miembros: List<MiembroDto>? = null,
    @SerializedName("pagos") val pagos: List<PagoDto>? = null
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
    @SerializedName("id") val id: String? = null,
    @SerializedName("nombre") val nombre: String? = null
) {
    fun toDomain(): Miembro {
        return Miembro(
            id = id ?: "",
            nombre = nombre ?: ""
        )
    }
}

data class PagoDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("planId") val planId: String? = null,
    @SerializedName("miembroNombre") val miembroNombre: String? = null,
    @SerializedName("monto") val monto: Double? = null,
    @SerializedName("fecha") val fecha: String? = null
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

/**
 * Payload JSON enviado en POST para crear un plan.
 */
data class CrearPlanRequestDto(
    @SerializedName("motivo") val motivo: String,
    @SerializedName("meta") val meta: Double,
    @SerializedName("meses") val meses: Int,
    @SerializedName("miembros") val miembros: List<String>
)

/**
 * Payload JSON enviado en POST para registrar un pago.
 */
data class RegistrarPagoRequestDto(
    @SerializedName("miembroNombre") val miembroNombre: String,
    @SerializedName("monto") val monto: Double
)
