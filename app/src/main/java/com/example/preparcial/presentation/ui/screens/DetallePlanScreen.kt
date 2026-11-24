package com.example.preparcial.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.preparcial.presentation.ui.components.CustomButton
import com.example.preparcial.presentation.ui.components.MemberItem
import com.example.preparcial.presentation.ui.theme.PrimaryGreen
import com.example.preparcial.presentation.ui.theme.TextPrimary
import com.example.preparcial.presentation.ui.theme.TextSecondary
import com.example.preparcial.presentation.viewmodel.DetallePlanViewModel
import java.text.NumberFormat
import java.util.Locale

/**
 * Pantalla 3: Detalle del Plan de Ahorro.
 * Muestra el título del plan, duración, meta en formato legible, la lista de miembros y el botón "Registrar pago".
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
            val formattedMeta = try {
                val format = NumberFormat.getInstance(Locale.getDefault())
                format.format(plan.meta)
            } catch (_: Exception) {
                plan.meta.toString()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Título del plan con botón de regreso
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

                Spacer(modifier = Modifier.height(14.dp))

                // Plazo
                Text(
                    text = "${plan.meses} meses",
                    fontSize = 15.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Meta numérica grande
                Text(
                    text = formattedMeta,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Subencabezado Miembros
                Text(
                    text = "Miembros",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Lista de miembros
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(plan.miembros, key = { it.id }) { miembro ->
                        MemberItem(miembro = miembro)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón "Registrar pago"
                CustomButton(
                    text = "Registrar pago",
                    onClick = { onNavigateToRegistrarPago(plan.id) }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
