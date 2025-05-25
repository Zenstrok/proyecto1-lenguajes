package edu.byorkt

class Main {
    fun run() {
        val interpreter = Interpreter()

        val codigo = """
            (define fib (lambda (n) (if (or (equal? n 0) (equal? n 1)) 1 (+ (fib (- n 1)) (fib (- n 2))))))
            (fib 5)
        """.trimIndent()
        val resultado = interpreter.run(codigo)
        println(interpreter.run(resultado))
    }
}

fun main() {
    val interpreter = Interpreter()
    println("Byorkt REPL - Escribe 'exit' para salir.")
    while (true) {
        print("> ")
        val entrada = readLine()
        if (entrada == null || entrada.trim() == "exit") {
            break
        }
        try {
            val resultado = interpreter.run(entrada)
            println(resultado)
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}
