package modelo

import java.time.LocalDateTime

data class Estadia(
    val idEstadia: Int = 0,
    val enCiudad: Int,
    val enHotel: Int,
    val checkin: LocalDateTime,
    val checkout: LocalDateTime,
    val cantidadHuespedes: Int
) {
}