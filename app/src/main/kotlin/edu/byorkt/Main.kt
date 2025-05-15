package edu.byorkt

class Main {
    fun run() {
        val interpreter = Interpreter()

        val code = """
            (define CERO 2)
            CERO
        """.trimIndent()

        val result = interpreter.run(code)

        println("Resultado: $result")
    }
}

fun main() {
    Main().run()
}
