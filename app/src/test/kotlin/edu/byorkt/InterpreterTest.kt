package edu.byorkt

import kotlin.test.Test
import kotlin.test.assertNotNull

class InterpreterTest {
    @Test
    fun runWithNumber() {
        // given
        val interpreter = Interpreter()
        val code = "5"

        // when
        val result = interpreter.run(code)

        // then
        assertNotNull(result, "result is not null")
        assert(result == "5")
    }

    @Test
    fun runWithBoolean() {
        // given
        val interpreter = Interpreter()
        val code = "#t"

        // when
        val result = interpreter.run(code)

        // then
        assertNotNull(result, "result is not null")
        assert(result == "#t")
    }

    @Test
    fun runWithDefine() {
        // given
        val interpreter = Interpreter()
        val code = "(define CERO 0)"

        // when
        val result = interpreter.run(code)

        // then
        assertNotNull(result, "result is not null")
        assert(result == "CERO")
    }

    @Test
    fun runWithSymbol() {
        // given
        val interpreter = Interpreter()
        val code = """
            (define CERO 0)
            CERO
        """.trimIndent()

        // when
        val result = interpreter.run(code)

        // then
        assertNotNull(result, "result is not null")
        assert(result == "0")
    }

    @Test
    fun runWithIdentidad() {
        // given
        val interpreter = Interpreter()
        val code = """
            (define identidad (lambda (x) x))
            (identidad 5)
        """.trimIndent()

        // when
        val result = interpreter.run(code)

        // then
        assertNotNull(result, "result is not null")
        assert(result == "5")
    }

    @Test
    fun runWithFactorial() {
        // given
        val interpreter = Interpreter()
        val code = """
            (define fact (lambda (n) (if (equal? n 0) 1 (* n (fact (- n 1))))))
            (fact 5)
        """.trimIndent()

        // when
        val result = interpreter.run(code)

        // then
        assertNotNull(result, "result is not null")
        assert(result == "120")
    }

    @Test
    fun runWithFibonacci() {
        // given
        val interpreter = Interpreter()
        val code = """
            (define fib (lambda (n) (if (or (equal? n 0) (equal? n 1)) 1 (+ (fib (- n 1)) (fib (- n 2))))))
            (fib 5)
        """.trimIndent()

        // when
        val result = interpreter.run(code)

        // then
        assertNotNull(result, "result is not null")
        assert(result == "8")
    }
}
