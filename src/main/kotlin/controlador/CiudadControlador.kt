package controlador

import modelo.daos.CiudadDao
import vista.CiudadVista
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException

class CiudadControlador (
    val dao: CiudadDao,
    val vistaC: CiudadVista,
    val paisControlador: PaisControlador
) {

    fun start() {
        var opcion: Int = -1
        do{
            opcion = vistaC.mostrarMenu()
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

    fun despedirse() = vistaC.mostrarMsg("gracias por su preferencia!")

    fun guardar(){
        val ciudad = vistaC.pedirInfo()
        try{
            if (ciudad != null) {
                var idgenerado = dao.guardar(ciudad)
                if(idgenerado !=- 1){
                    vistaC.mostrarMsg("Datos almacenados exitosamente.")
                    vistaC.mostrarCiudad(ciudad)
                }
            } else{
                println("No pudo almacenarse la información.")
            }
        } catch (e: SQLSyntaxErrorException) {
            vistaC.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
        } catch (e: SQLException) {
            vistaC.mostrarMsg("Error de base de datos: ${e.message}")
        }


        println(ciudad.toString())
    }

    fun leer(){
        val idP = vistaC.pedirIdCiudad()
        try{
            val ciudad = dao.leerConId(idP)
            if ( ciudad != null) {
                vistaC.mostrarCiudad(ciudad)
            } else {
                vistaC.mostrarMsg("No se encontró al país con id: $idP")
            }
        } catch (e: SQLSyntaxErrorException) {
            vistaC.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
        } catch (e: SQLException) {
            vistaC.mostrarMsg("Error de base de datos: ${e.message}")
        }
    }

    fun actualizar() {
        val idP = vistaC.pedirIdCiudad()
        try {
            val ciudadExistente = dao.leerConId(idP)
            if (ciudadExistente != null) {
                var ciudad = vistaC.pedirInfo()
                if (ciudad != null) {
                    ciudad.idCiudad = idP
                    val actualizado = dao.actualizar(ciudad)
                    if (actualizado) {
                        vistaC.mostrarMsg("Datos actualizados correctamente.")
                        vistaC.mostrarCiudad(ciudad)
                    } else {
                        vistaC.mostrarMsg("Error al actualizar los datos.")
                    }
                } else {
                    vistaC.mostrarMsg("No se pudo obtener los nuevos datos.")
                }
            } else {
                vistaC.mostrarMsg("No se encontró la persona con ID: $idP")
            }
        } catch (e: SQLSyntaxErrorException) {
            vistaC.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
        } catch (e: SQLException) {
            vistaC.mostrarMsg("Error de base de datos: ${e.message}")
        }
    }

    fun eliminar() {
        val idP = vistaC.pedirIdCiudad()
        try {
            val pais = dao.leerConId(idP)
            if (pais != null) {

                //val confirmacion = vistaP.pedirConfirmacionEliminacion()

                /*if (confirmacion) {*/
                val eliminado = dao.eliminar(idP)
                if (eliminado) {
                    vistaC.mostrarMsg("Persona eliminada exitosamente.")
                } else {
                    vistaC.mostrarMsg("Error al eliminar la persona.")
                }
                /*} else {
                    vistaP.mostrarMsg("Eliminación cancelada.")
                }*/
            } else {
                vistaC.mostrarMsg("No se encontró la persona con ID: $idP")
            }
        } catch (e: SQLSyntaxErrorException) {
            vistaC.mostrarMsg("Error de sintaxis en la consulta SQL: ${e.message}")
        } catch (e: SQLException) {
            vistaC.mostrarMsg("Error de base de datos: ${e.message}")
        }
    }
}