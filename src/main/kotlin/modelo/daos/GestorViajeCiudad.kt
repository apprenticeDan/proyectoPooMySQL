package modelo.daos

import modelo.Ciudad
import modelo.Pais
import modelo.Viaje
import java.time.LocalDateTime

class GestorViajeCiudad(
    private val ciudadDao: CiudadDao, // DAO para manejar ciudades
    private val viajeDao: ViajeDao    // DAO para manejar viajes
    private val paisDao: PaisDao    // DAO para manejar viajes
) {
    fun definirViaje(origenP: String, destinoP: String,
                     origenC: String, destinoC: String,
                     salida: LocalDateTime, llegada: LocalDateTime): Viaje {
        val paisOrigen = paisDao.buscarConNombre(origenP)
            ?:  paisDao.guardar(Pais(nombrePais = origenP))
        val idPaisOrigen = paisOrigen
        val ciudadOrigen = ciudadDao.leerConNombre(origenC)
            ?: ciudadDao.guardar(Ciudad(
                nombreCiudad = origenC, enPais = idPaisOrigen
            ))
        val ciudadDestino = ciudadDao.leerConNombre(origenC)
            ?: ciudadDao.guardar(Ciudad(nombreCiudad = destinoC))





        // Crear el viaje con las referencias a las ciudades
        val viaje = Viaje(
            origenId = ciudadOrigen.id,
            destinoId = ciudadDestino.id,
            salida = salida,
            llegada = llegada
        )

        // Guardar el viaje en la base de datos
        val idViaje = viajeDao.guardar(viaje)

        // Asociar el ID generado al objeto Viaje antes de devolverlo
        viaje.id = idViaje

        return viaje
    }
}