package modelo.daos

import modelo.Viaje
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.time.LocalDateTime

class ViajeDao (
    val connection: Connection
) {

    val tabla = "viaje"
    val numParam = parametrosViaje().size
    fun parametrosViaje(): List<String> =
        mutableListOf("idViaje","origen", "destino", "salida", "llegada")

    fun getInsert(): String  =
        "INSERT INTO $tabla (${parametrosViaje().subList(1,numParam).joinToString(", ")}) VALUES (${parametrosViaje().subList(1,numParam).joinToString{"? "}});"
    fun getSelectPorId(): String = "SELECT * FROM $tabla WHERE ${parametrosViaje()[0]} = ?;"
    fun getSelectPorOrigen(): String = "SELECT * FROM $tabla WHERE ${parametrosViaje()[1]} = ?;"
    fun getSelectPorDestino(): String = "SELECT * FROM $tabla WHERE ${parametrosViaje()[2]} = ?;"
    fun getSelectPorSalida(): String = "SELECT * FROM $tabla WHERE ${parametrosViaje()[3]} = ?;"
    fun getSelectTodos(): String = "SELECT * FROM $tabla;"
    fun getUpdate(): String = "UPDATE $tabla SET ${parametrosViaje().subList(1,numParam).joinToString(", "){"$it = ?"}} WHERE idPersona = ?;"
    fun getDelete(): String = "DELETE FROM $tabla WHERE ${parametrosViaje()[0]} = ?;"

    fun pasarParametrosAlStatement(preparedStatement: PreparedStatement, viaje: Viaje) {
        preparedStatement.setString(1, viaje.origen.toString())
        preparedStatement.setString(2, viaje.destino.toString())
        preparedStatement.setObject(3, viaje.salida)
        preparedStatement.setObject(4, viaje.llegada)
    }

    fun guardar(viaje: Viaje): Int {
        var idGenerado: Int = -1
        val statement: PreparedStatement = connection.prepareStatement(getInsert(), Statement.RETURN_GENERATED_KEYS)
        pasarParametrosAlStatement(statement, viaje);
        if (statement.executeUpdate() > 0) {
            val result = statement.generatedKeys
            if (result.next()) idGenerado = result.getInt(1)
        }
        return idGenerado
    }

    fun leerViajeconId(idViaje: Int): Viaje? {
        val statement = connection.prepareStatement(getSelectPorId())
        statement.setInt(1, idViaje)
        val resultSet: ResultSet = statement.executeQuery()
        return if (resultSet.next()) {
            Viaje (
                idViaje = idViaje,
                resultSet.getInt("origen"),
                resultSet.getInt("destino"),
                resultSet.getObject("salida") as LocalDateTime,
                resultSet.getObject("llegada") as LocalDateTime,
            )
        } else {
            null
        }
    }

    fun leerViajes(): List<Viaje>? {
        val viajes = mutableListOf<Viaje>()
        val statement = connection.prepareStatement(getSelectTodos())
        val resultSet: ResultSet = statement.executeQuery()

        while (resultSet.next()) {
            val viaje = Viaje(
                idViaje = resultSet.getInt("idPersona"),
                resultSet.getInt("origen"),
                resultSet.getInt("destino"),
                resultSet.getObject("salida") as LocalDateTime,
                resultSet.getObject("llegada") as LocalDateTime,
            )
            viajes.add(viaje)
        }
        return viajes
    }

    fun leerViajesConOrigen(): List<Viaje>? {
        val viajes = mutableListOf<Viaje>()
        val statement = connection.prepareStatement(getSelectPorOrigen())
        val resultSet: ResultSet = statement.executeQuery()
        while (resultSet.next()) {
            val viaje = Viaje(
                idViaje = resultSet.getInt("idPersona"),
                resultSet.getInt("origen"),
                resultSet.getInt("destino"),
                resultSet.getObject("salida") as LocalDateTime,
                resultSet.getObject("llegada") as LocalDateTime,
            )
            viajes.add(viaje)
        }
        return viajes
    }

    fun leerViajesConDestino(): List<Viaje>? {
        val viajes = mutableListOf<Viaje>()
        val statement = connection.prepareStatement(getSelectPorDestino())
        val resultSet: ResultSet = statement.executeQuery()
        while (resultSet.next()) {
            val viaje = Viaje(
                idViaje = resultSet.getInt("idPersona"),
                resultSet.getInt("origen"),
                resultSet.getInt("destino"),
                resultSet.getObject("salida") as LocalDateTime,
                resultSet.getObject("llegada") as LocalDateTime,
            )
            viajes.add(viaje)
        }
        return viajes
    }

    fun actualizarDatosViaje(viaje: Viaje): Boolean {
        val query = getUpdate()
        val statement = connection.prepareStatement(query)
        pasarParametrosAlStatement(statement, viaje)
        statement.setInt(numParam, viaje.idViaje!!)
        return statement.executeUpdate() > 0
    }

    fun eliminarViaje(idViaje: Int): Boolean {
        val statement = connection.prepareStatement(getDelete())
        statement.setInt(1, idViaje)
        return statement.executeUpdate() > 0
    }
}