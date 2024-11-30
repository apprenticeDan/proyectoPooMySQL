package vista

import controlador.PaisControlador
import modelo.Ciudad

class CiudadVista (
    val paisControlador: PaisControlador
) {
    val opciones: List<String> = listOf("anadir ciudad", "mostrar ciudad con id")

    fun mostrarMenu(): Int {
        opciones.forEachIndexed { index, opcion ->
            println("${index + 1}. $opcion")
        }
        return readln().toIntOrNull() ?: -1
    }

    fun pedirInfo(): Ciudad? {
        val entradaUsr = EntradaUsuario()
        val nom = "nombre de la ciudad"
        val enPais = "ubicada en el país"

        // Definir los campos que se van a solicitar con sus respectivas transformaciones y validaciones.
        val campos = mapOf(
            nom to { it: String -> it },
            enPais to { it: String -> it }
        )

        // Almacenar las respuestas en un mapa.
        val respuestas = mutableMapOf<String, Any>()
        for ((campo, transformacion) in campos) {
            val valor = entradaUsr.leerDato("$campo (o escriba 'salir' para cancelar): ", transformacion) ?: return null
            respuestas[campo] = valor
        }


        return Ciudad(
            nombreCiudad = respuestas[nom].toString(), // as String,
            enPais = respuestas[enPais] as Int
        )
    }

    fun pedirIdCiudad(): Int {
        val entradaUsr = EntradaUsuario()
        return entradaUsr.leerDato("Ingrese el ID de la ciudad: ") { it.toInt() } as? Int ?: -1
    }

    fun mostrarCiudad(ciudad: Ciudad) {
        println(
            """
            ID: ${ciudad.idCiudad}
            Nombre: ${ciudad.nombreCiudad}
            enPais: ${paisControlador.leerPaisConId(ciudad.enPais)}
            """.trimIndent()
        )
    }

    fun mostrarMsg(msg: String) {
        println(msg)
    }
}
