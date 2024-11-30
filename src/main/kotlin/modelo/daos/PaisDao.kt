package modelo.daos

import modelo.Pais
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class PaisDao(
    val connection: Connection
) {



    val tabla = "pais"
    val numParam = parametrosPais().size
    fun parametrosPais(): List<String> = mutableListOf("idPais","nombrePais", "iso3166")

    fun getInsert(): String  =
        "INSERT INTO $tabla (${parametrosPais().subList(1,numParam).joinToString(", ")}) VALUES (${parametrosPais().subList(1,numParam).joinToString{"? "}});"
    fun getSelectPorId(): String = "SELECT * FROM $tabla WHERE ${parametrosPais()[0]} = ?;"
    fun getSelectPorNombre(): String = "SELECT * FROM $tabla WHERE ${parametrosPais()[1]} = ?;"
    fun getSelectTodos(): String = "SELECT * FROM $tabla;"
    fun getUpdate(): String = "UPDATE $tabla SET ${parametrosPais().subList(1,numParam).joinToString(", "){"$it = ?"}} WHERE ${parametrosPais()[0]} = ?;"
    fun getDelete(): String = "DELETE FROM $tabla WHERE ${parametrosPais()[0]} = ?;"

    fun pasarParametrosAlStatement(preparedStatement: PreparedStatement, pais: Pais) {
        preparedStatement.setString(1, pais.nombrePais)
        preparedStatement.setString(2, pais.iso3166)
    }

    fun guardar(pais: Pais): Int {
        var idGenerado: Int = -1
        val statement: PreparedStatement = connection.prepareStatement(getInsert(), Statement.RETURN_GENERATED_KEYS)
        pasarParametrosAlStatement(statement, pais);
        if (statement.executeUpdate() > 0) {
            val result = statement.generatedKeys
            if (result.next()) idGenerado = result.getInt(1)
        }
        return idGenerado
    }

    fun leerconId(idPais: Int): Pais? {
        val statement = connection.prepareStatement(getSelectPorId())
        statement.setInt(1, idPais)
        val resultSet: ResultSet = statement.executeQuery()
        return if (resultSet.next()) {
            Pais (
                idPais,
                resultSet.getString("nombrePais"),
                resultSet.getString("iso3166")
            )
        } else {
            null
        }
    }

    fun buscarConNombre(nombrePais: String): Pais? {
        val sql = "SELECT * FROM $tabla WHERE ${parametrosPais()[1]} = ?;"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, nombrePais.trim())
        val  resultSet = statement.executeQuery()
        return if (resultSet.next()) {
            Pais(
                resultSet.getInt("idPais"),
                resultSet.getString("nombrePais"),
                resultSet.getString("iso3166")
            ) } else { null }
    }

    fun buscarConISO(iso: String): Pais? {
        val sql = "SELECT * FROM $tabla WHERE ${parametrosPais()[2]} = ?;"
        val statement = connection.prepareStatement(sql)
        statement.setString(1, iso.trim())
        val  resultSet = statement.executeQuery()
        return if (resultSet.next())
            Pais(
                resultSet.getInt("idPais"),
                resultSet.getString("nombrePais"),
                resultSet.getString("iso3166")
            ) else
                null
    }

    fun leerTodos(): List<Pais>? {
        val paises = mutableListOf<Pais>()
        val statement = connection.prepareStatement(getSelectTodos())
        val resultSet: ResultSet = statement.executeQuery()

        while (resultSet.next()) {
            val pais = Pais(
                idPais = resultSet.getInt("idPais"),
                nombrePais = resultSet.getString("nombrePais"),
                iso3166 = resultSet.getString("iso3166")
            )
            paises.add(pais)
        }
        return paises
    }

    fun actualizarDatos(pais: Pais): Boolean {
        val query = getUpdate()
        val statement = connection.prepareStatement(query)

        // Pasamos los parámetros
        pasarParametrosAlStatement(statement, pais)
        statement.setInt(numParam, pais.idPais)
        return statement.executeUpdate() > 0
    }


    fun eliminar(idPais: Int): Boolean {
        val statement = connection.prepareStatement(getDelete())
        statement.setInt(1, idPais)
        return statement.executeUpdate() > 0
    }

    /*fun leerConNombre(nombreP: String): Pais? {

    }*/
}