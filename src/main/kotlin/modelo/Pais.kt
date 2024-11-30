package modelo

data class Pais(
    val idPais: Int = 0,
    val nombrePais: String,
    val iso3166: String? = null
) {
}