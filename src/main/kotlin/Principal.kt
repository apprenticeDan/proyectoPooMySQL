import controlador.PersonaControlador
import modelo.daos.PersonaDao
import vista.PersonaVista
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun main() {
    println("Hello World!")
    val url = "jdbc:mysql://localhost:3306/agencia"
    val user = "root"
    val pass = "my-pass"

    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, user, pass)
    } catch (e: SQLException) {
        println("Error al conectar a la base de datos: ${e.message}")
    }

    connection?.let { conn ->
        val dao = PersonaDao(conn)
        val vista = PersonaVista()
        val controlador = PersonaControlador(dao, vista)
        controlador.start()
    } ?: run {
        println("\nNo se pudo establecer la conexión a la base de datos.")
    }
}

