# Justificación Técnica de Arquitectura y Diseño

## 1. Arquitectura MVVM + Clean Architecture

El proyecto está estructurado en tres capas independientes con responsabilidades estrictamente separadas:

- **Domain (Dominio)**: Contiene los modelos de negocio (`PlanAhorro`, `Miembro`, `Pago`), las interfaces de repositorio y los Casos de Uso (`GetPlanesUseCase`, `CrearPlanUseCase`, `RegistrarPagoUseCase`). Es la capa central y **pura**, sin dependencias de Android ni de librerías externas de red.
- **Data (Datos)**: Encargada de la persistencia y la comunicación con servicios externos. Contiene los DTOs de Retrofit (`PlanDto`), las interfaces de API (`ApiService`), y la implementación del repositorio (`RemotePlanRepositoryImpl` y `MockPlanRepositoryImpl`).
- **Presentation (UI y ViewModels)**: Contiene la interfaz gráfica declarativa basada en **Jetpack Compose** y los **ViewModels**. Los ViewModels exponen el estado a la vista mediante `StateFlow`, asegurando la desacoplación total entre la UI y la lógica de negocio.

---

## 2. Patrón Repository (Patrón Repositorio)

El patrón **Repository** actúa como un intermediario o fachada entre la capa de dominio/ViewModels y las fuentes de datos reales (API REST de FastAPI o Mock local).

- **Ventajas principales**:
  1. Permite alternar mediante un único punto (`AppContainer.USE_MOCK_DATA`) entre datos simulados en memoria y el backend de Retrofit sin cambiar una sola línea de código en la UI o en los ViewModels.
  2. Facilita las **pruebas unitarias** aislando los datos y permitiendo inyectar repositorios *Mock* para validar casos de uso y ViewModels de manera determinista.

---

## 3. Consumo de Servicios Web REST con Retrofit y Gson

Para la comunicación con el backend en FastAPI (Python / VS Code):

- **Retrofit**: Se utiliza como el cliente HTTP estándar de la industria en Android nativo gracias a su integración nativa con *Corrutinas* de Kotlin mediante funciones `suspend` y tipado seguro.
- **Gson**: Convierte automáticamente las respuestas JSON devueltas por FastAPI / Pydantic v2 a Objetos de Transferencia de Datos (`DTO`), desacoplando el formato de transporte del modelo de dominio interno.

---

## 4. Estado de UI Reactivo con StateFlow y Jetpack Compose

- **StateFlow**: Proporciona un manejo de estado reactivo y seguro ante cambios de configuración (como rotación de pantalla).
- **Lógica fuera de la UI**: La Activity y los Composables se limitan a observar estados (`Loading`, `Success`, `Error`) y emitir eventos del usuario hacia los ViewModels, cumpliendo con los principios de *Single Responsibility* y *Unidirectional Data Flow (UDF)*.
