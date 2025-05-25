package edu.byorkt

class Interpreter {
    private val parser = Parser()
    private val evaluator = Evaluator()

    fun run(input: String): String {
        val exprs = read(input)
        val result = eval(exprs)
        return print(result)
    }

    private fun read(input: String): List<Expression> {
        return parser.parse(input)
    }
    private fun eval(expressions: List<Expression>): Value {
        return evaluator.evalAll(expressions)
    }
    private fun print(value: Value): String {
        return value.toString()
    }
}
