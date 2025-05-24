package edu.byorkt

class InvalidApplicationException(func: Value) :
    RuntimeException("No se puede aplicar: $func")

class Evaluator {
    private val global = Environment()

    init {
        loadBuiltins()
    }

    fun evalAll(expressions: List<Expression>): Value {
        var result: Value = Value.Undefined
        for (expression in expressions) {
            result = eval(expression, global)
        }
        return result
    }

    private fun eval(expression: Expression, env: Environment): Value = when (expression) {
        is Expression.Number -> Value.Number(expression.value)
        is Expression.Boolean -> Value.Boolean(expression.value)
        is Expression.Symbol -> env.get(expression.name)
        is Expression.ListExpr -> evalList(expression, env)
    }

    private fun evalList(expression: Expression.ListExpr, env: Environment): Value {
        if (expression.elements.isEmpty()) {
            throw EmptyListException()
        }

        val head = expression.elements[0]
        val args = expression.elements.drop(1)

        return when {
            head is Expression.Symbol -> handleSpecialForms(head, args, env)
            else -> {
                val func = eval(head, env)
                val argVals = args.map { eval(it, env) }
                Applier.apply(func, argVals)
            }
        }
    }

    private fun handleSpecialForms(head: Expression.Symbol, args: List<Expression>, env: Environment): Value =
        when (head.name) {
            "define" -> {
                val name = (args[0] as Expression.Symbol).name
                val value = eval(args[1], env)
                env.define(name, value)
                Value.Symbol(name)
            }
            "lambda" -> {
                val paramsExpression = args[0] as Expression.ListExpr
                val params = paramsExpression.elements.map { (it as Expression.Symbol).name }
                val body = args[1]
                Value.Procedure(params, body, env)
            }
            "if" -> {
                handleIf(args, env)
            }
            else -> {
                val func = eval(head, env)
                val argVals = args.map { eval(it, env) }
                Applier.apply(func, argVals)
            }
        }

    private fun handleIf(args: List<Expression>, env: Environment): Value {
        val condition = eval(args[0], env)
        return when (condition) {
            is Value.Boolean -> {
                if (condition.value) {
                    eval(args[1], env)
                } else {
                    eval(args[2], env)
                }
            }
            else -> throw NotBooleanIfException(condition)
        }
    }

    private fun loadBuiltins() {
        loadArithmeticLogicalBuiltins()
        loadComparisonBuiltins()
        loadLogicalBuiltins()
    }

    private fun loadArithmeticLogicalBuiltins() {
        global.define(
            "+",
            Value.Builtin("+") {
                val sum = it.sumOf { v -> (v as Value.Number).value }
                Value.Number(sum)
            }
        )
        global.define(
            "-",
            Value.Builtin("-") {
                if (it.size == 1) {
                    Value.Number(-(it[0] as Value.Number).value)
                } else {
                    val head = (it[0] as Value.Number).value
                    val rest = it.drop(1).map { v -> (v as Value.Number).value }
                    Value.Number(rest.fold(head) { acc, v -> acc - v })
                }
            }
        )
        global.define(
            "*",
            Value.Builtin("*") {
                val product = it.map { v -> (v as Value.Number).value }.reduce { acc, v -> acc * v }
                Value.Number(product)
            }
        )
        global.define(
            "/",
            Value.Builtin("/") {
                val head = (it[0] as Value.Number).value
                val rest = it.drop(1).map { v -> (v as Value.Number).value }
                Value.Number(rest.fold(head) { acc, v -> acc / v })
            }
        )
        global.define(
            "%",
            Value.Builtin("%") {
                val a = (it[0] as Value.Number).value
                val b = (it[1] as Value.Number).value
                Value.Number(a % b)
            }
        )
    }

    private fun loadComparisonBuiltins() {
        global.define(
            "<",
            Value.Builtin("<") {
                val (a, b) = it.map { v -> (v as Value.Number).value }
                Value.Boolean(a < b)
            }
        )
        global.define(
            ">",
            Value.Builtin(">") {
                val (a, b) = it.map { v -> (v as Value.Number).value }
                Value.Boolean(a > b)
            }
        )
        global.define(
            "<=",
            Value.Builtin("<=") {
                val (a, b) = it.map { v -> (v as Value.Number).value }
                Value.Boolean(a <= b)
            }
        )
        global.define(
            ">=",
            Value.Builtin(">=") {
                val (a, b) = it.map { v -> (v as Value.Number).value }
                Value.Boolean(a >= b)
            }
        )
        global.define(
            "equal?",
            Value.Builtin("equal?") {
                val (a, b) = it
                Value.Boolean(a == b)
            }
        )
    }

    private fun loadLogicalBuiltins() {
        global.define(
            "and",
            Value.Builtin("and") {
                val result = it.all { v -> (v as Value.Boolean).value }
                Value.Boolean(result)
            }
        )
        global.define(
            "or",
            Value.Builtin("or") {
                val result = it.any { v -> (v as Value.Boolean).value }
                Value.Boolean(result)
            }
        )
        global.define(
            "not",
            Value.Builtin("not") {
                val v = (it[0] as Value.Boolean).value
                Value.Boolean(!v)
            }
        )
    }
}
