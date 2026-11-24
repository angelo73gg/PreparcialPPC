package com.example.preparcial.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.preparcial.presentation.ui.components.PlanCard
import com.example.preparcial.presentation.ui.theme.PrimaryGreen
import com.example.preparcial.presentation.ui.theme.TextPrimary
import com.example.preparcial.presentation.viewmodel.HomeViewModel

/**
 * Pantalla 1: Lista de Planes de Ahorro (Home).
 * Muestra el título principal, el botón para crear plan y la lista de tarjetas de planes.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCrearPlan: () -> Unit,
    onNavigateToDetallePlan: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Título Principal (con salto de línea como en la imagen)
        Text(
            text = "Plan Familiar\nde Ahorro",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 38.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Botón verde "Crear plan"
        CustomButton(
            text = "Crear plan",
            onClick = onNavigateToCrearPlan
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Subtítulo de sección
        Text(
            text = "Planes de ahorro",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Estado de Carga / Lista / Error
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryGreen)
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.error ?: "Error al cargar los datos",
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }
            }
            uiState.planes.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay planes de ahorro registrados.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.planes, key = { it.id }) { plan ->
                        PlanCard(
                            plan = plan,
                            onClick = { onNavigateToDetallePlan(plan.id) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
