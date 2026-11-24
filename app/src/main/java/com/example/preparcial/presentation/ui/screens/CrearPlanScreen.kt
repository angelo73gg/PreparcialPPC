package com.example.preparcial.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.preparcial.presentation.ui.components.CustomButton
import com.example.preparcial.presentation.ui.components.CustomTextField
import com.example.preparcial.presentation.ui.theme.PrimaryGreen
import com.example.preparcial.presentation.ui.theme.TextPrimary
import com.example.preparcial.presentation.viewmodel.CrearPlanViewModel

/**
 * Pantalla 2: Formulario para crear un nuevo Plan de Ahorro.
 * Campos: Motivo, Meta, Meses, Miembros. Botón: Crear.
 */
@Composable
fun CrearPlanScreen(
    viewModel: CrearPlanViewModel,
    onPlanCreado: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Si la creación fue exitosa, regresar a la pantalla anterior
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onPlanCreado()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

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
                text = "Crear plan",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Campo 1: Motivo
        CustomTextField(
            label = "Motivo",
            value = uiState.motivo,
            onValueChange = viewModel::onMotivoChange,
            placeholder = "Ej: Ahorro para viaje familiar"
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo 2: Meta
        CustomTextField(
            label = "Meta",
            value = uiState.meta,
            onValueChange = viewModel::onMetaChange,
            placeholder = "Ej: 5000000",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo 3: Meses
        CustomTextField(
            label = "Meses",
            value = uiState.meses,
            onValueChange = viewModel::onMesesChange,
            placeholder = "Ej: 12",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo 4: Miembros
        CustomTextField(
            label = "Miembros",
            value = uiState.miembrosText,
            onValueChange = viewModel::onMiembrosChange,
            placeholder = "Ej: Jorge, Ana, Luis",
            singleLine = false
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (uiState.error != null) {
            Text(
                text = uiState.error ?: "",
                color = Color.Red,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        if (uiState.isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = PrimaryGreen)
            }
        } else {
            CustomButton(
                text = "Crear",
                onClick = viewModel::crearPlan
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
