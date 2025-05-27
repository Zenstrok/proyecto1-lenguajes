package edu.byorkt

// Excepción para cuando el intérprete no puede procesar la entrada.
class ExcepcionErrorInterpretacion(entrada: String) :
    RuntimeException("Error: La entrada '$entrada' no puede ser procesada por el interprete")

class Main {
    /**
     * Funcion de REPL
     */
    fun repl() {
        val interprete = Interprete()
        println("Byorkt REPL - Escribe 'salir' para terminar.")
        while (true) {
            print("> ")
            val entrada = readLine()
            if (entrada == null || entrada.trim() == "salir") {
                break
            }
            try {
                val resultado = interprete.run(entrada)
                println(resultado)
            } catch (e: ExcepcionErrorInterpretacion) {
                println("Error: ${e.message}")
            }
        }
    }
}

/**
 * Funcion principal que demuestra el uso del intérprete Byorkt.
 */
fun main() {
    val interprete = Interprete()
    val modoRepl = false // Cambiarlo a "true" si se quiere hacer REPL
    if (modoRepl) {
        Main().repl()
    } else {
        /**
         * Ejecuta un ejemplo de programa en Byorkt.
         * Este método muestra cómo usar el intérprete con un ejemplo que esta en el repositorio
         */
        val codigo = """
        (define fact (lambda (n) (if (equal? n 0) 1 (* n (fact (- n 1))))))
        (fact 5)
        """.trimIndent()

        val resultado = interprete.run(codigo)
        println(interprete.run(resultado))
    }
}
