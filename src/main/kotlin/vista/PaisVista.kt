package vista

import modelo.Pais
import modelo.daos.PaisDao

class PaisVista(
    private val paisDao: PaisDao
) {
    val opciones: List<String> = listOf("anadir pais", "mostrar pais con id")

    fun mostrarMenu(): Int {
        opciones.forEachIndexed { index, opcion ->
            println("${index + 1}. $opcion")
        }
        return readln().toIntOrNull() ?: -1
    }

    fun pedirInfo(): Pais? {
        val entradaUsr = EntradaUsuario()
        val nom = "nombre del país"
        val iso = "iso3166 del país"
        val idP = "ID del país"

        // Definir los campos que se van a solicitar con sus respectivas transformaciones y validaciones.
        val campos = mapOf(
            nom to { it: String -> it },
            iso to { it: String -> it },
            idP to { it: String -> it }
        )

        // Almacenar las respuestas en un mapa.
        val respuestas = mutableMapOf<String, Any>()
        for ((campo, transformacion) in campos) {
            val valor = entradaUsr.leerDato("$campo (o escriba 'salir' para cancelar): ", transformacion) ?: return null
            respuestas[campo] = valor
        }

        val nombrePais = respuestas[nom].toString()
        val paisExistentePorNombre = paisDao.buscarConNombre(nombrePais)
        if (paisExistentePorNombre != null) {
            println("El país '$nombrePais' ya existe en la base de datos.")
            return paisExistentePorNombre
        }

        // Si el usuario ingresó un ISO, verificamos si el país ya existe
        val isoPais = respuestas[iso].toString()
        val paisExistentePorIso = paisDao.buscarConISO(isoPais)
        if (paisExistentePorIso != null) {
            println("El país con ISO '$isoPais' ya existe en la base de datos.")
            return paisExistentePorIso
        }

        // Si el usuario ingresó un ID, verificamos si el país ya existe
        val idPaisInt = respuestas[idP].toString().toIntOrNull()
        if (idPaisInt != null) {
            val paisExistentePorId = paisDao.leerconId(idPaisInt)
            if (paisExistentePorId != null) {
                println("El país con ID '$idPaisInt' ya existe en la base de datos.")
                return paisExistentePorId
            }
        }

        // Construir y retornar la Persona.
        return Pais(
            nombrePais = respuestas[nom].toString(), // as String,
            iso3166 = respuestas[iso] as String
        )
    }

    fun pedirIdPais(): Int {
        val entradaUsr = EntradaUsuario()
        return entradaUsr.leerDato("Ingrese el ID del país: ") { it.toInt() } as? Int ?: -1
    }

    fun mostrarPais(pais: Pais) {
        println(
            """
            ID: ${pais.idPais}
            Nombre: ${pais.nombrePais}
            ISO3166: ${pais.iso3166}
            """.trimIndent()
        )
    }

    fun mostrarMsg(msg: String) {
        println(msg)
    }
}