package vista

class EntradaUsuario {
    fun leerDato(prompt: String, transformacion: (String) -> Any): Any? {
        while (true) {
            try {
                print(prompt)
                val input = readln()
                if (input.lowercase() == "salir") return null
                return transformacion(input)
            } catch (e: Exception) {
                println("Entrada inválida. Intente nuevamente.")
            }
        }
    }
}