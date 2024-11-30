package modelo.daos

import modelo.Persona
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class PersonaDao(
    val connection: Connection
) {



    val tabla = "persona"
    val numParam = parametrosPersona().size
    fun parametrosPersona(): List<String> = mutableListOf("idPersona","nombre", "apellido", "fechaNacimiento")

    fun getInsert(): String  =
        "INSERT INTO $tabla (${parametrosPersona().subList(1,numParam).joinToString(", ")}) VALUES (${parametrosPersona().subList(1,numParam).joinToString{"? "}});"
    fun getSelectPorId(): String = "SELECT * FROM $tabla WHERE idPersona = ?;"
    fun getSelectTodos(): String = "SELECT * FROM $tabla;"
    fun getUpdate(): String = "UPDATE $tabla SET ${parametrosPersona().subList(1,numParam).joinToString(", "){"$it = ?"}} WHERE idPersona = ?;"
    fun getDelete(): String = "DELETE FROM $tabla WHERE idPersona = ?;"

    fun pasarParametrosAlStatement(preparedStatement: PreparedStatement, persona: Persona) {
        preparedStatement.setString(1, persona.nombre)
        preparedStatement.setString(2, persona.apellido)
        preparedStatement.setObject(3, persona.fechaNacimiento)
    }

    fun guardar(persona: Persona): Int {
        var idGenerado: Int = -1
        val statement: PreparedStatement  = connection.prepareStatement(getInsert(), Statement.RETURN_GENERATED_KEYS)
        pasarParametrosAlStatement(statement, persona);
        if (statement.executeUpdate() > 0) {
            val result = statement.generatedKeys
            if (result.next()) idGenerado = result.getInt(1)
        }
        return idGenerado
    }

    fun leerconId(idPersona: Int): Persona? {
        val statement = connection.prepareStatement(getSelectPorId())
        statement.setInt(1, idPersona)
        val resultSet: ResultSet = statement.executeQuery()
        return if (resultSet.next()) {
            Persona (
                idPersona = idPersona,
                resultSet.getString("nombre"),
                resultSet.getString("apellido"),
                resultSet.getDate(4).toLocalDate()
            )
        } else {
            null
        }
    }

    fun leerTodos(): List<Persona>? {
        val personas = mutableListOf<Persona>()
        val statement = connection.prepareStatement(getSelectTodos())
        val resultSet: ResultSet = statement.executeQuery()

        while (resultSet.next()) {
            val persona = Persona(
                idPersona = resultSet.getInt("idPersona"),
                nombre = resultSet.getString("nombre"),
                apellido = resultSet.getString("apellido"),
                fechaNacimiento = resultSet.getDate("fechaNac").toLocalDate()
            )
            personas.add(persona)
        }
        return personas
    }

    fun actualizarDatos(persona: Persona): Boolean {
        val query = getUpdate()
        val statement = connection.prepareStatement(query)
        pasarParametrosAlStatement(statement, persona)
        statement.setInt(numParam, persona.idPersona!!)
        return statement.executeUpdate() > 0
    }

    fun eliminar(idPersona: Int): Boolean {
        val statement = connection.prepareStatement(getDelete())
        statement.setInt(1, idPersona)
        return statement.executeUpdate() > 0
    }
}