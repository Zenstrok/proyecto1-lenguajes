package edu.byorkt

class Main {
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
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}
