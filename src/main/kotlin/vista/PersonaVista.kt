package vista

import modelo.Persona
import java.time.LocalDate

class PersonaVista {
    val opciones: List<String> = listOf("crear persona", "mostrar persona con id")

    fun mostrarMenu(): Int {
        opciones.forEachIndexed { index, opcion ->
            println("${index + 1}. $opcion")
        }
        return readln().toIntOrNull() ?: -1
    }

    fun pedirInfo(): Persona? {
        val entradaUsr = EntradaUsuario()
        val anioStr = "año de nacimiento"
        val mesStr = "mes de nacimiento"
        val diaStr = "día del mes"

        // Definir los campos que se van a solicitar con sus respectivas transformaciones y validaciones.
        val campos = mapOf(
            "nombre" to { it: String -> it },
            "apellido" to { it: String -> it },
            anioStr to { it: String ->
                val anio = it.toInt()
                require(anio in 1900..LocalDate.now().year) { "El año debe estar entre 1900 y el año actual." }
                anio
            },
            mesStr to { it: String ->
                val mes = it.toInt()
                require(mes in 1..12) { "El mes debe estar entre 1 y 12." }
                mes
            },
            diaStr to { it: String ->
                val dia = it.toInt()
                require(dia in 1..31) { "El día debe estar entre 1 y 31." }
                dia
            }
        )

        // Almacenar las respuestas en un mapa.
        val respuestas = mutableMapOf<String, Any>()
        for ((campo, transformacion) in campos) {
            val valor = entradaUsr.leerDato("$campo (o escriba 'salir' para cancelar): ", transformacion) ?: return null
            respuestas[campo] = valor
        }

        // Construir la fecha de nacimiento.
        val fechaNacimiento = try {
            LocalDate.of(
                respuestas[anioStr] as Int,
                respuestas[diaStr] as Int,
                respuestas[mesStr] as Int
            )
        } catch (e: NumberFormatException) {
            println("Fecha de nacimiento inválida. Intente nuevamente.")
            e.printStackTrace()
            println( "causa: ${e.cause}")
            return null
        }

        // Construir y retornar la Persona.
        return Persona(
            nombre = respuestas["nombre"] as String,
            apellido = respuestas["apellido"] as String,
            fechaNacimiento = fechaNacimiento
        )
    }

    fun pedirIdPersona(): Int {
        val entradaUsr = EntradaUsuario()
        return entradaUsr.leerDato("Ingrese el ID de la persona: ") { it.toInt() } as? Int ?: -1
    }

    fun mostrarPersona(persona: Persona) {
        println(
            """
            ID: ${persona.idPersona}
            Nombre: ${persona.nombre}
            Apellido: ${persona.apellido}
            Fecha de Nacimiento: ${persona.fechaNacimiento}
            """.trimIndent()
        )
    }

    fun mostrarMsg(msg: String) {
        println(msg)
    }

    fun pedirConfirmacionEliminacion(): Boolean? {
        println("confirma que quiere eliminar la información?(s/n)")
        val inp = readln()
        return when (inp){ "s"-> true "n"->false
            else -> {
                println("opción no válida")
                null
            }
        }
    }
}
