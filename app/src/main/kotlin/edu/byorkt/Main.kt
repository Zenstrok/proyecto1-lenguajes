package edu.byorkt

// Excepción para cuando el intérprete no puede procesar la entrada.
class ExcepcionErrorInterpretacion(entrada: String) :
    RuntimeException("Error: La entrada '$entrada' no puede ser procesada por el interprete")

/**
 * Clase principal que demuestra el uso del intérprete Byorkt.
 * Contiene ejemplos de código y el punto de entrada principal.
 */
class Main {
    /**
     * Ejecuta un ejemplo de programa en Byorkt.
     * Este método muestra cómo usar el intérprete con un ejemplo de cálculo de Fibonacci.
     */
    fun run() {
        val interprete = Interprete()

        val codigo = """
            (define fib (lambda (n) (if (or (equal? n 0) (equal? n 1)) 1 (+ (fib (- n 1)) (fib (- n 2))))))
            (fib 5)
        """.trimIndent()

        val resultado = interprete.run(codigo)
        println(interprete.run(resultado))
    }
}

/**
 * Punto de entrada principal del intérprete Byorkt.
 * Implementa un bucle REPL (Read-Eval-Print Loop) interactivo.
 */
fun main() {
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
