ejemplo:
El servidor responde con JSON:
[
  {
    "id": 1,
    "tipo": "Apartamento",
    "ciudad": "Bogotá",
    "precio": 250000000
  },
  {
    "id": 2,
    "tipo": "Casa",
    "ciudad": "Cali",
    "precio": 320000000
  }
]
1. Agregar el permiso de Internet
En AndroidManifest.xml, antes de <application>, agrega:
<uses-permission android:name="android.permission.INTERNET" />
Sin ese permiso, la aplicación no puede conectarse a la red.
2. Agregar las librerías
En el Gradle del módulo app, agrega Retrofit, el convertidor Gson y las librerías de ViewModel/coroutines. Usa versiones compatibles con el proyecto:
dependencies {
    implementation("com.squareup.retrofit2:retrofit:VERSION")
    implementation("com.squareup.retrofit2:converter-gson:VERSION")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:VERSION")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:VERSION")
}
VERSION es un marcador: en el proyecto real se reemplaza por la versión que esté usando el curso. Luego sincroniza Gradle.
3. Crear el modelo que representa un elemento
Crea Propiedad.kt:
data class Propiedad(
    val id: Int,
    val tipo: String,
    val ciudad: String,
    val precio: Long
)
Cada propiedad del JSON corresponde a un campo de la clase. Por ejemplo, "ciudad": "Bogotá" se convierte en ciudad: String.
4. Describir la llamada al servicio
Crea PropiedadApi.kt:
import retrofit2.http.GET

interface PropiedadApi {
    @GET("propiedades")
    suspend fun obtenerPropiedades(): List<Propiedad>
}
Aquí le dices a Retrofit: “cuando llame a obtenerPropiedades, haz un GET a la ruta propiedades y convierte la lista recibida en objetos Propiedad”.

suspend indica que la llamada puede tardar y debe hacerse desde una coroutine. No tienes que escribir la coroutine en esta interfaz; la iniciaremos desde el ViewModel.
5. Configurar Retrofit con la URL del servidor
Crea RetrofitClient.kt:
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.ejemplo.com/api/"

    val api: PropiedadApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PropiedadApi::class.java)
    }
}
Gson es el convertidor: toma el JSON que llegó del servidor y lo transforma en una List<Propiedad>.

La URL base termina en /; Retrofit agrega la ruta de @GET. Juntas forman https://api.ejemplo.com/api/propiedades.
6. Crear el Repository
Crea PropiedadRepository.kt:
class PropiedadRepository(
    private val api: PropiedadApi
) {
    suspend fun obtenerPropiedades(): List<Propiedad> {
        return api.obtenerPropiedades()
    }
}
El Repository es el intermediario entre el ViewModel y Retrofit. Por ahora solo reenvía la petición; sirve para que el ViewModel no dependa directamente del cliente HTTP.
7. Crear el ViewModel con estado de carga, datos y error
Crea PropiedadViewModel.kt:
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EstadoPantalla(
    val cargando: Boolean = false,
    val propiedades: List<Propiedad> = emptyList(),
    val error: String? = null
)

class PropiedadViewModel(
    private val repository: PropiedadRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoPantalla())
    val estado: StateFlow<EstadoPantalla> = _estado

    fun cargarPropiedades() {
        viewModelScope.launch {
            _estado.value = EstadoPantalla(cargando = true)

            try {
                val lista = repository.obtenerPropiedades()
                _estado.value = EstadoPantalla(propiedades = lista)
            } catch (e: Exception) {
                _estado.value = EstadoPantalla(
                    error = "No se pudieron cargar las propiedades."
                )
            }
        }
    }
}
Cuando la pantalla pide cargar los datos, viewModelScope.launch inicia una coroutine. La app puede seguir respondiendo mientras espera al servidor. Al terminar, el ViewModel cambia el estado: queda la lista o un mensaje de error.
8. Conectar el ViewModel y mostrar los resultados en la Activity
Para mantener el ejemplo enfocado, la Activity crea sus vistas en código y presenta cada propiedad como texto. Crea MainActivity.kt:
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: PropiedadViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = PropiedadRepository(RetrofitClient.api)
                return PropiedadViewModel(repository) as T
            }
        }
    }

    private lateinit var resultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultado = TextView(this).apply {
            textSize = 18f
            setPadding(24, 24, 24, 24)
        }

        val boton = Button(this).apply {
            text = "Cargar propiedades"
            setOnClickListener {
                viewModel.cargarPropiedades()
            }
        }

        val contenido = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            addView(boton)
            addView(resultado)
        }

        setContentView(ScrollView(this).apply {
            addView(contenido)
        })

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.estado.collect { estado ->
                    resultado.text = when {
                        estado.cargando ->
                            "Cargando..."

                        estado.error != null ->
                            estado.error

                        estado.propiedades.isEmpty() ->
                            "Presiona el botón para cargar propiedades."

                        else ->
                            estado.propiedades.joinToString(
                                separator = "\n\n"
                            ) { propiedad ->
                                "${propiedad.tipo} en ${propiedad.ciudad}\n" +
                                    "Precio: $${propiedad.precio}"
                            }
                    }
                }
            }
        }

        viewModel.cargarPropiedades()
    }
}
La Activity observa el estado del ViewModel. Cuando llegan los datos, los convierte en texto y los presenta. Si la llamada falla, muestra el mensaje de error.
Este ejemplo usa AppCompatActivity. Si el proyecto de clase hereda de ComponentActivity o usa Jetpack Compose, la pantalla se escribe de otra forma, pero las partes de Retrofit, API, modelo y Repository mantienen la misma idea.

¿Y si el parcial también pide construir el backend?
Ahí sí necesitas las dos herramientas:
- FastAPI corre en el backend y entrega JSON.
- Retrofit corre en Android y solicita ese JSON.
Por ejemplo, un backend mínimo podría publicar los datos así:
from fastapi import FastAPI

app = FastAPI()

@app.get("/api/propiedades")
def obtener_propiedades():
    return [
        {
            "id": 1,
            "tipo": "Apartamento",
            "ciudad": "Bogotá",
            "precio": 250000000
        },
        {
            "id": 2,
            "tipo": "Casa",
            "ciudad": "Cali",
            "precio": 320000000
        }
    ]
