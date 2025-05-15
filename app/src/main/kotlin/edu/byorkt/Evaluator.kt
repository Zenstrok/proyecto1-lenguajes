package edu.byorkt

class InvalidApplicationException(func: Value) :
    RuntimeException("No se puede aplicar: $func")

class EmptyListException :
    RuntimeException("Lista vacía no evaluable")

class DirectIfCallException :
    RuntimeException("'if' no debe ser llamado directamente")

class Evaluator {

    private val global = Environment()

    init {
        loadBuiltins()
    }

    fun evalAll(exprs: List<Expr>): Value {
        var result: Value = Value.Undefined
        for (expr in exprs) {
            result = eval(expr, global)
        }
        return result
    }

    private fun eval(expr: Expr, env: Environment): Value = when (expr) {
        is Expr.Number -> Value.Number(expr.value)
        is Expr.Boolean -> Value.Boolean(expr.value)
        is Expr.Symbol -> env.get(expr.name)
        is Expr.List -> evalList(expr, env)
    }

    private fun evalList(expr: Expr.List, env: Environment): Value {
        if (expr.elements.isEmpty()) {
            throw EmptyListException()
        }

        val head = expr.elements[0]
        val args = expr.elements.drop(1)

        if (head is Expr.Symbol) {
            return when (head.name) {
                "define" -> {
                    val name = (args[0] as Expr.Symbol).name
                    val value = eval(args[1], env)
                    env.define(name, value)
                    Value.Symbol(name)
                }
                else -> {
                    val func = eval(head, env)
                    val argVals = args.map { eval(it, env) }
                    apply(func, argVals)
                }
            }
        }

        val func = eval(head, env)
        val argVals = args.map { eval(it, env) }
        return apply(func, argVals)
    }

    private fun apply(func: Value, args: List<Value>): Value {
        return when (func) {
            is Value.Builtin -> func.impl(args)
            is Value.Procedure -> {
                val localEnv = Environment(func.env)
                func.params.zip(args).forEach { (param, arg) ->
                    localEnv.define(param, arg)
                }
                eval(func.body, localEnv)
            }
            else -> throw InvalidApplicationException(func)
        }
    }

    private fun loadBuiltins() {
        global.define(
            "+",
            Value.Builtin("+") {
                val sum = it.sumOf { v -> (v as Value.Number).value }
                Value.Number(sum)
            }
        )
        global.define(
            "equal?",
            Value.Builtin("equal?") {
                val (a, b) = it
                Value.Boolean(a == b)
            }
        )
        global.define(
            "if",
            Value.Builtin("if") {
                throw DirectIfCallException()
            }
        )
    }
}
