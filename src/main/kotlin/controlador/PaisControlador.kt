package controlador

import modelo.daos.PaisDao
import vista.PaisVista

import java.sql.SQLException
import java.sql.SQLSyntaxErrorException

class PaisControlador (
    val dao: PaisDao,
    val vistaP: PaisVista
) {

    fun start() {
        var opcion: Int = -1
        do{
            opcion = vistaP.mostrarMenu()
            when(opcion){
                1->guardar()
                2->leer()
                3-> actualizar()
                4-> eliminar()
                5-> despedirse()
                else-> println("opción no reconocida")
            }
        }while (opcion != 5)
    }

    fun despedirse() = vistaP.mostrarMsg("gracias por su preferencia!")

    fun guardar(){
        val pais = vistaP.pedirInfo()
        try{
            if (pais != null) {
                var idgenerado = dao.guardar(pais)
                if(idgenerado !=- 1){
                    vistaP.mostrarMsg("Datos almacenados exitosamente.")
                    vistaP.mostrarPais(pais.copy(idPais = idgenerado))
                }
            } else{
                println("No pudo almacenarse la información.")
            }
        } catch (e: SQLSyntaxErrorException) {
            vistaP.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
        } catch (e: SQLException) {
            vistaP.mostrarMsg("Error de base de datos: ${e.message}")
        }


        println(pais.toString())
    }

    fun leerPaisConId(idPais: Int) {
        try{
            val pais = dao.leerconId(idPais)
            if ( pais != null) {
                vistaP.mostrarPais(pais)
            } else {
                vistaP.mostrarMsg("No se encontró al país con id: $idPais")
            }
        } catch (e: SQLSyntaxErrorException) {
            vistaP.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
        } catch (e: SQLException) {
            vistaP.mostrarMsg("Error de base de datos: ${e.message}")
        }
    }

    fun leer(){
        val idP = vistaP.pedirIdPais()
        try{
            val pais = dao.leerconId(idP)
            if ( pais != null) {
                vistaP.mostrarPais(pais)
            } else {
                vistaP.mostrarMsg("No se encontró al país con id: $idP")
            }
        } catch (e: SQLSyntaxErrorException) {
            vistaP.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
        } catch (e: SQLException) {
            vistaP.mostrarMsg("Error de base de datos: ${e.message}")
        }
    }

    fun actualizar() {
        val idP = vistaP.pedirIdPais()
        try {
            val paisExistente = dao.leerconId(idP)
            if (paisExistente != null) {
                var nuevoPais = vistaP.pedirInfo()
                if (nuevoPais != null) {
                    nuevoPais = nuevoPais.copy(idPais=idP) // Establecer el ID de la persona para actualizar
                    val actualizado = dao.actualizarDatos(nuevoPais)
                    if (actualizado) {
                        vistaP.mostrarMsg("Datos actualizados correctamente.")
                        vistaP.mostrarPais(nuevoPais)
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

    fun eliminar() {
        val idP = vistaP.pedirIdPais()
        try {
            val pais = dao.leerconId(idP)
            if (pais != null) {

                //val confirmacion = vistaP.pedirConfirmacionEliminacion()

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