package modelo

import java.time.LocalDateTime

data class Viaje(
    val idViaje: Int = 0,
    val origen: Int, // ID de la tabla Ciudad
    val destino: Int, // ID de la tabla Ciudad
    val salida: LocalDateTime,
    val llegada: LocalDateTime,
    // val cantidadPasajeros: Int
) {
}