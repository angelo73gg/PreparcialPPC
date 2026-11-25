import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle

// ---------- 1. MODELO: qué es un viaje ----------
data class Viaje(
    val id: Int,
    val nombre: String,
    val destino: String,
    val fechaInicio: LocalDate,
    val fechaFin: LocalDate
)

// ---------- 2. RESULTADO: salió bien o salió con errores ----------
sealed class Resultado {
    data class Exito(val viaje: Viaje) : Resultado()
    data class Error(val mensajes: List<String>) : Resultado()
}

// ---------- 3. CONVERTIR TEXTO A FECHA ----------
// Formato dd/MM/aaaa. STRICT rechaza fechas imposibles como 31/02/2026
private val formatoFecha = DateTimeFormatter
    .ofPattern("dd/MM/uuuu")
    .withResolverStyle(ResolverStyle.STRICT)

fun String.aFecha(): LocalDate? =
    try {
        LocalDate.parse(this.trim(), formatoFecha)
    } catch (e: DateTimeParseException) {
        null // el texto no era una fecha válida
    }

// ---------- 4. ALMACÉN: guarda los viajes en memoria ----------
object ViajeRepositorio {
    private val viajes = mutableListOf<Viaje>()
    private var siguienteId = 1

    fun guardar(viaje: Viaje): Viaje {
        val nuevo = viaje.copy(id = siguienteId++)
        viajes.add(nuevo)
        return nuevo
    }

    fun todos(): List<Viaje> = viajes.toList()
}

// ---------- 5. LÓGICA: crear viaje con validaciones ----------
fun crearViaje(
    nombre: String,
    destino: String,
    inicioTexto: String,
    finTexto: String
): Resultado {
    val errores = mutableListOf<String>()

    // Campos obligatorios
    if (nombre.isBlank()) errores.add("El nombre del viaje es obligatorio.")
    if (destino.isBlank()) errores.add("El destino es obligatorio.")

    // Convertir las fechas y validar su formato
    val inicio = inicioTexto.aFecha()
    val fin = finTexto.aFecha()

    if (inicioTexto.isBlank()) {
        errores.add("La fecha de inicio es obligatoria.")
    } else if (inicio == null) {
        errores.add("La fecha de inicio debe tener formato dd/MM/aaaa.")
    }

    if (finTexto.isBlank()) {
        errores.add("La fecha de finalización es obligatoria.")
    } else if (fin == null) {
        errores.add("La fecha de finalización debe tener formato dd/MM/aaaa.")
    }

    // Regla: el fin no puede ser antes del inicio
    if (inicio != null && fin != null && fin.isBefore(inicio)) {
        errores.add("La fecha de finalización no puede ser anterior a la de inicio.")
    }

    // Si hubo errores, no se guarda nada
    if (errores.isNotEmpty()) return Resultado.Error(errores)

    // Todo válido: se crea y se guarda
    val viaje = Viaje(
        id = 0, // el repositorio asigna el id real
        nombre = nombre.trim(),
        destino = destino.trim(),
        fechaInicio = inicio!!,
        fechaFin = fin!!
    )
    return Resultado.Exito(ViajeRepositorio.guardar(viaje))
}

// ---------- 6. PRUEBA POR CONSOLA ----------
fun main() {
    println("=== CREAR VIAJE ===")

    print("Nombre del viaje: ")
    val nombre = readlnOrNull() ?: ""

    print("Destino: ")
    val destino = readlnOrNull() ?: ""

    print("Fecha de inicio (dd/MM/aaaa): ")
    val inicio = readlnOrNull() ?: ""

    print("Fecha de finalización (dd/MM/aaaa): ")
    val fin = readlnOrNull() ?: ""

    when (val resultado = crearViaje(nombre, destino, inicio, fin)) {
        is Resultado.Exito -> {
            val v = resultado.viaje
            println("\nViaje creado correctamente")
            println("ID: ${v.id}")
            println("Nombre: ${v.nombre}")
            println("Destino: ${v.destino}")
            println("Del ${v.fechaInicio} al ${v.fechaFin}")
        }
        is Resultado.Error -> {
            println("\nNo se pudo crear el viaje:")
            resultado.mensajes.forEach { println("- $it") }
        }
    }
}
