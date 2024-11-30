import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import modelo.Persona
import modelo.Viaje
import modelo.daos.CiudadDao
import modelo.daos.PaisDao
import modelo.daos.PersonaDao
import modelo.daos.ViajeDao
import java.sql.Connection
import java.sql.DriverManager
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!"
        }) {
            Text(text)
        }
    }
}

fun main() = application {
    /*Window(onCloseRequest = ::exitApplication) {
        App()
    }*/
    val connection: Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "password")

    val personaDao = PersonaDao(connection)
    val viajeDao = ViajeDao(connection)
    val paisDao = PaisDao(connection)
    val ciudadDao = CiudadDao(connection)

    // Pruebas básicas:
    // Insertar una persona
    val persona = Persona(nombre = "Juan", apellido = "Pérez", fechaNacimiento = LocalDate.of(1990, 5, 20))
    val idPersona = personaDao.guardar(persona)
    println("Persona guardada con ID: $idPersona")

    // Leer todas las personas
    val personas = personaDao.leerTodos()
    println("Personas en la base de datos: $personas")

    // Insertar un viaje
    val viaje = Viaje( origen = "Ciudad A", destino = "Ciudad B", salida = LocalDateTime.now(), llegada = LocalDateTime.now().plusHours(5))
    val idViaje = viajeDao.guardar(viaje)
    println("Viaje guardado con ID: $idViaje")

    // Leer todos los viajes
    val viajes = viajeDao.leerViajes()
    println("Viajes en la base de datos: $viajes")

    // Actualizar datos de una persona
    val personaActualizada = Persona(idPersona = idPersona, nombre = "Juan Carlos", apellido = "Pérez", fechaNacimiento = LocalDate.of(1990, 5, 20))
    val actualizado = personaDao.actualizarDatos(personaActualizada)
    println("Persona actualizada: $actualizado")

    // Eliminar un viaje
    val eliminado = viajeDao.eliminarViaje(idViaje!!)
    println("Viaje eliminado: $eliminado")

    connection.close()

}
