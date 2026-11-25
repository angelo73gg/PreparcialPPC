package com.example.preparcial.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.preparcial.domain.model.Pago
import com.example.preparcial.presentation.ui.theme.BorderGray
import com.example.preparcial.presentation.ui.theme.PrimaryGreen
import com.example.preparcial.presentation.ui.theme.TextPrimary
import com.example.preparcial.presentation.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

/**
 * Componente para mostrar un pago registrado en la consulta de pagos del plan.
 */
@Composable
fun PagoItem(
    pago: Pago,
    modifier: Modifier = Modifier
) {
    val formattedMonto = try {
        val format = NumberFormat.getInstance(Locale.getDefault())
        "$" + format.format(pago.monto)
    } catch (_: Exception) {
        "$${pago.monto}"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderGray),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pago.miembroNombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formattedMonto,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryGreen
                )
            }
            if (pago.fecha.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Fecha: ${pago.fecha}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        }
    }
}
