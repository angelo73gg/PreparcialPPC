package com.example.preparcial.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.preparcial.domain.model.PlanAhorro
import com.example.preparcial.presentation.ui.theme.BorderGray
import com.example.preparcial.presentation.ui.theme.TextPrimary
import com.example.preparcial.presentation.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

/**
 * Tarjeta para representar un Plan de Ahorro en la pantalla de lista (Home).
 */
@Composable
fun PlanCard(
    plan: PlanAhorro,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Formato de moneda simulado o número entero (e.g., 5.000.000)
    val formattedMeta = try {
        val format = NumberFormat.getInstance(Locale.getDefault())
        format.format(plan.meta)
    } catch (_: Exception) {
        plan.meta.toString()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, BorderGray),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = plan.motivo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = plan.categoria.ifBlank { "Categoria" },
                fontSize = 13.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = formattedMeta,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${plan.meses} meses",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}
