package modelo

import java.time.LocalDate

class Ciudad(
    var idCiudad: Int = 0,
    var nombreCiudad:  String,
    var enPais: Int // llave foranea idPais
)