package com.example.preparcial

import com.example.preparcial.data.repository.MockPlanRepositoryImpl
import com.example.preparcial.domain.usecase.CrearPlanUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para las validaciones del Caso de Uso CrearPlanUseCase.
 */
class CrearPlanUseCaseTest {

    private lateinit var crearPlanUseCase: CrearPlanUseCase

    @Before
    fun setUp() {
        val mockRepository = MockPlanRepositoryImpl()
        crearPlanUseCase = CrearPlanUseCase(mockRepository)
    }

    @Test
    fun crearPlanFallaCuandoElMotivoEstaVacio() = runBlocking {
        val result = crearPlanUseCase(
            motivo = "",
            meta = 500000.0,
            meses = 6,
            miembros = listOf("Jorge")
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun crearPlanFallaCuandoLaMetaEsMenorOIgualACero() = runBlocking {
        val result = crearPlanUseCase(
            motivo = "Ahorro",
            meta = 0.0,
            meses = 6,
            miembros = listOf("Jorge")
        )

        assertTrue(result.isFailure)
    }

    @Test
    fun crearPlanEsExitosoCuandoLosDatosSonValidos() = runBlocking {
        val result = crearPlanUseCase(
            motivo = "Comprar auto",
            meta = 1000000.0,
            meses = 12,
            miembros = listOf("Jorge", "Ana")
        )

        assertTrue(result.isSuccess)
        val planCreado = result.getOrNull()
        assertTrue(planCreado != null)
        assertTrue(planCreado?.motivo == "Comprar auto")
    }
}
