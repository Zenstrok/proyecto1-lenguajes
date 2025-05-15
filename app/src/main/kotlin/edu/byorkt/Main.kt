package edu.byorkt

class Main {
    fun run() {
        val interpreter = Interpreter()

        val code = """
            (define identidad (lambda (x) x))
            (identidad 5)
        """.trimIndent()

        val result = interpreter.run(code)

        println("Resultado: $result")
    }
}

fun main() {
    Main().run()
}
