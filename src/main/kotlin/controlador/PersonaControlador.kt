package controlador

import modelo.daos.PersonaDao
import vista.PersonaVista
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException

class PersonaControlador(
    val dao: PersonaDao,
    val vistaP: PersonaVista
) {

    fun start() {
        var opcion: Int = -1
        do{
            opcion = vistaP.mostrarMenu()
            when(opcion){
                1->guardar()
                2->leerPersona()
                3-> actualizarPersona()
                4-> eliminarPersona()
                5-> despedirse()
                else-> println("opción no reconocida")
            }
        }while (opcion != 5)
    }

    fun despedirse() = vistaP.mostrarMsg("gracias por su preferencia!")

    fun guardar(){
        val persona = vistaP.pedirInfo()
        try{
        if (persona != null) {
            var idgenerado = dao.guardar(persona)
            if(idgenerado !=- 1){
                vistaP.mostrarMsg("Datos almacenados exitosamente.")
                vistaP.mostrarPersona(persona.copy(idPersona=idgenerado))
            }
        } else{
            println("No pudo almacenarse la información.")
        }
    } catch (e: SQLSyntaxErrorException) {
        vistaP.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
    } catch (e: SQLException) {
        vistaP.mostrarMsg("Error de base de datos: ${e.message}")
    }


        println(persona.toString())
    }

    fun leerPersona(){
        val idP = vistaP.pedirIdPersona()
        try{
            val persona = dao.leerconId(idP)
            if ( persona != null) {
                vistaP.mostrarPersona(persona)
            } else {
                vistaP.mostrarMsg("No se encontró a la persona con id: $idP")
            }
        } catch (e: SQLSyntaxErrorException) {
            vistaP.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
        } catch (e: SQLException) {
            vistaP.mostrarMsg("Error de base de datos: ${e.message}")
        }
    }

    fun actualizarPersona() {
        val idP = vistaP.pedirIdPersona()
        try {
            val personaExistente = dao.leerconId(idP)
            if (personaExistente != null) {
                val nuevaPersona = vistaP.pedirInfo()
                if (nuevaPersona != null) {
                    nuevaPersona.idPersona = idP // Establecer el ID de la persona para actualizar
                    val actualizado = dao.actualizarDatos(nuevaPersona)
                    if (actualizado) {
                        vistaP.mostrarMsg("Datos actualizados correctamente.")
                        vistaP.mostrarPersona(nuevaPersona)
                    } else {
                        vistaP.mostrarMsg("Error al actualizar los datos.")
                    }
                } else {
                    vistaP.mostrarMsg("No se pudo obtener los nuevos datos.")
                }
            } else {
                vistaP.mostrarMsg("No se encontró la persona con ID: $idP")
            }
        } catch (e: SQLSyntaxErrorException) {
            vistaP.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
        } catch (e: SQLException) {
            vistaP.mostrarMsg("Error de base de datos: ${e.message}")
        }
    }

    fun eliminarPersona() {
        val idP = vistaP.pedirIdPersona()
        try {
            val personaExistente = dao.leerconId(idP)
            if (personaExistente != null) {

                val confirmacion = vistaP.pedirConfirmacionEliminacion()

                /*if (confirmacion) {*/
                    val eliminado = dao.eliminar(idP)
                    if (eliminado) {
                        vistaP.mostrarMsg("Persona eliminada exitosamente.")
                    } else {
                        vistaP.mostrarMsg("Error al eliminar la persona.")
                    }
                /*} else {
                    vistaP.mostrarMsg("Eliminación cancelada.")
                }*/
            } else {
                vistaP.mostrarMsg("No se encontró la persona con ID: $idP")
            }
        } catch (e: SQLSyntaxErrorException) {
            vistaP.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
        } catch (e: SQLException) {
            vistaP.mostrarMsg("Error de base de datos: ${e.message}")
        }
    }
}