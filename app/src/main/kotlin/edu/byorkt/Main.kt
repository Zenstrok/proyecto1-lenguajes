package edu.byorkt

class Main {
    fun run() {
        val interpreter = Interpreter()

        val code = """
            (define fib (lambda (n) (if (or (equal? n 0) (equal? n 1)) 1 (+ (fib (- n 1)) (fib (- n 2))))))
            (fib 5)
        """.trimIndent()

        val result = interpreter.run(code)

        println("Resultado: $result")
    }
}

fun main() {
    // Main().run()  // Para pruebas
    /* val interpreter = Interpreter()
    val scanner = Scanner(System.`in`)
    while (true) {
        print("> ")
        val input = scanner.nextLine()
        if (input == "exit") { break }
        println(interpreter.run(input))
    } */
    val interpreter = Interpreter()
    var shouldContinue = true

    while (shouldContinue) {
        print("> ")
        readLine()?.let { input ->
            shouldContinue = input != "exit"
            if (shouldContinue) {
                println(interpreter.run(input))
            }
        }
    }
}
