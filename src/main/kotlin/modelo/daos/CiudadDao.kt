package modelo.daos

import modelo.Ciudad
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement

class CiudadDao(
    val connection: Connection
) {
    val tabla = "ciudad"
    val numParam = parametrosCiudad().size

    fun parametrosCiudad(): List<String> = mutableListOf("idCiudad", "nombreCiudad", "idPais")

    fun getInsert(): String =
        "INSERT INTO $tabla (${
            parametrosCiudad().subList(1, numParam).joinToString(", ")
        }) VALUES (${parametrosCiudad().subList(1, numParam).joinToString { "? " }});"

    fun getSelectPorId(): String = "SELECT * FROM $tabla WHERE ${parametrosCiudad()[0]} = ?;"
    fun getSelectPorNombre(): String = "SELECT * FROM $tabla WHERE ${parametrosCiudad()[1]} = ?;"

    fun getSelectTodos(): String = "SELECT * FROM $tabla;"

    fun getUpdate(): String = "UPDATE $tabla SET ${
        parametrosCiudad().subList(1, numParam).joinToString(", ") { "$it = ?" }
    } WHERE ${parametrosCiudad()[0]} = ?;"

    fun getDelete(): String = "DELETE FROM $tabla WHERE ${parametrosCiudad()[0]} = ?;"

    fun pasarParametrosAlStatement(preparedStatement: PreparedStatement, ciudad: Ciudad) {
        preparedStatement.setString(1, ciudad.nombreCiudad)
        preparedStatement.setInt(2, ciudad.enPais) // Relacion con el ID del país
    }

    fun guardar(ciudad: Ciudad): Int {
        var idGenerado: Int = -1
        val statement: PreparedStatement = connection.prepareStatement(getInsert(), Statement.RETURN_GENERATED_KEYS)
        pasarParametrosAlStatement(statement, ciudad)
        if (statement.executeUpdate() > 0) {
            val result = statement.generatedKeys
            if (result.next()) idGenerado = result.getInt(1)
        }
        return idGenerado
    }

    fun leerConId(idCiudad: Int): Ciudad? {
        val statement = connection.prepareStatement(getSelectPorId())
        statement.setInt(1, idCiudad)
        val resultSet = statement.executeQuery()
        return if (resultSet.next()) {
            Ciudad(
                idCiudad = resultSet.getInt("idCiudad"),
                nombreCiudad = resultSet.getString("nombreCiudad"),
                enPais = resultSet.getInt("enPais")
            )
        } else {
            null
        }
    }

    fun leerConNombre(ciudadNombre: String): Ciudad? {
        val statement = connection.prepareStatement(getSelectPorId())
        statement.setString(1, ciudadNombre)
        val resultSet = statement.executeQuery()
        return if (resultSet.next()) {
            Ciudad(
                idCiudad = resultSet.getInt("idCiudad"),
                nombreCiudad = resultSet.getString("nombreCiudad"),
                enPais = resultSet.getInt("enPais")
            )
        } else {
            null
        }
    }

    fun leerTodos(): List<Ciudad>? {
        val ciudades = mutableListOf<Ciudad>()
        val statement = connection.prepareStatement(getSelectTodos())
        val resultSet = statement.executeQuery()

        while (resultSet.next()) {
            val ciudad = Ciudad(
                idCiudad = resultSet.getInt("idCiudad"),
                nombreCiudad = resultSet.getString("nombreCiudad"),
                enPais = resultSet.getInt("enPais")
                //PaisDao(connection).leerPaisconId(resultSet.getInt("idPais")) ?: Pais()  // Obtén el país relacionado
            )
            ciudades.add(ciudad)
        }
        return ciudades
    }

    fun actualizar(ciudad: Ciudad): Boolean {
        val query = getUpdate()
        val statement = connection.prepareStatement(query)
        pasarParametrosAlStatement(statement, ciudad)
        statement.setInt(numParam, ciudad.idCiudad)
        return statement.executeUpdate() > 0
    }

    fun eliminar(idCiudad: Int): Boolean {
        val statement = connection.prepareStatement(getDelete())
        statement.setInt(1, idCiudad)
        return statement.executeUpdate() > 0
    }
}