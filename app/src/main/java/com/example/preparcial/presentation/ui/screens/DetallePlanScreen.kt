package com.example.preparcial.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.preparcial.presentation.ui.components.CustomButton
import com.example.preparcial.presentation.ui.components.MemberItem
import com.example.preparcial.presentation.ui.components.PagoItem
import com.example.preparcial.presentation.ui.theme.BorderGray
import com.example.preparcial.presentation.ui.theme.PrimaryGreen
import com.example.preparcial.presentation.ui.theme.TextPrimary
import com.example.preparcial.presentation.ui.theme.TextSecondary
import com.example.preparcial.presentation.viewmodel.DetallePlanViewModel
import java.text.NumberFormat
import java.util.Locale

/**
 * Pantalla 3: Detalle del Plan de Ahorro.
 * Muestra información completa (meta, meses, motivo), lista de miembros, pagos registrados y botón "Registrar pago".
 */
@Composable
fun DetallePlanScreen(
    viewModel: DetallePlanViewModel,
    onNavigateToRegistrarPago: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        }
        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error ?: "Error al cargar el detalle",
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
        }
        uiState.plan != null -> {
            val plan = uiState.plan!!
            val format = NumberFormat.getInstance(Locale.getDefault())
            val formattedMeta = try { format.format(plan.meta) } catch (_: Exception) { plan.meta.toString() }
            val formattedRecaudado = try { format.format(plan.totalRecaudado) } catch (_: Exception) { plan.totalRecaudado.toString() }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Encabezado con botón de regreso
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = TextPrimary
                        )
                    }
                    Text(
                        text = plan.motivo,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 38.sp,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Duración en meses
                Text(
                    text = "${plan.meses} meses",
                    fontSize = 15.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Meta numérica
                Text(
                    text = formattedMeta,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Barra de progreso de ahorro y total recaudado vs meta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recaudado: $$formattedRecaudado",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryGreen
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${(plan.porcentajeProgreso * 100).toInt()}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { plan.porcentajeProgreso },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = PrimaryGreen,
                    trackColor = BorderGray
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Contenido desplazable (Miembros y Pagos registrados)
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    // Sección 1: Miembros
                    item {
                        Text(
                            text = "Miembros",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    items(plan.miembros, key = { "m_" + it.id }) { miembro ->
                        MemberItem(miembro = miembro)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Sección 2: Pagos Registrados (Consultar pagos del plan)
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Pagos Registrados",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        if (plan.pagos.isEmpty()) {
                            Text(
                                text = "No hay pagos registrados aún.",
                                fontSize = 14.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }

                    items(plan.pagos, key = { "p_" + it.id }) { pago ->
                        PagoItem(pago = pago)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Botón "Registrar pago"
                CustomButton(
                    text = "Registrar pago",
                    onClick = { onNavigateToRegistrarPago(plan.id) }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
