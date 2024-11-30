package modelo

data class Hotel(
    val idHotel: Int,
    val nombreHotel: String,
    val enCiudad: Int // foranea de IdCiudad
) {
}