package modelo

import java.time.LocalDate

data class Persona(
    var idPersona: Int = 0,
    var nombre:  String,
    var apellido: String,
    var fechaNacimiento: LocalDate
) {

}