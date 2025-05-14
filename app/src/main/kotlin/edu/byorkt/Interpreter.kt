package edu.byorkt

class Interpreter {

    private val parser = Parser()
    private val evaluator = Evaluator()

    fun run(program: String): String {
        val expressions = parser.parse(program)
        val result = evaluator.evalAll(expressions)
        return result.toString()
    }
}
