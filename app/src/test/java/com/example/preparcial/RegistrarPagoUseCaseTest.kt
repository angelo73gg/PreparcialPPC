package com.example.preparcial

import com.example.preparcial.data.repository.MockPlanRepositoryImpl
import com.example.preparcial.domain.usecase.RegistrarPagoUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Pruebas unitarias para el Caso de Uso RegistrarPagoUseCase.
 */
class RegistrarPagoUseCaseTest {

    private lateinit var registrarPagoUseCase: RegistrarPagoUseCase

    @Before
    fun setUp() {
        val mockRepository = MockPlanRepositoryImpl()
        registrarPagoUseCase = RegistrarPagoUseCase(mockRepository)
    }

    @Test
    fun registrarPagoFallaCuandoElMontoEsInvalido() = runBlocking {
        val result = registrarPagoUseCase(
            planId = "1",
            miembroNombre = "Jorge",
            monto = 0.0
        )

        assertTrue(result.isFailure)
    }

    @Test
    fun registrarPagoEsExitosoCuandoLosDatosSonValidos() = runBlocking {
        val result = registrarPagoUseCase(
            planId = "1",
            miembroNombre = "Jorge",
            monto = 200000.0
        )

        assertTrue(result.isSuccess)
    }
}
