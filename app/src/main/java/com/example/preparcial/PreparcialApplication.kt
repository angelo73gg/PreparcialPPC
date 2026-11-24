package com.example.preparcial

import android.app.Application
import com.example.preparcial.di.AppContainer

/**
 * Clase Application principal.
 * Inicializa el contenedor de inyección de dependencias manual (AppContainer).
 */
class PreparcialApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer()
    }
}
