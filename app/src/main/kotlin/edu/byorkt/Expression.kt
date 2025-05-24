package edu.byorkt

class EmptyListException :
    RuntimeException("Lista vacía no evaluable")

class NotBooleanIfException(condition: Value) :
    RuntimeException("La condición de 'if' no es booleana: $condition")

sealed class Expression {
    abstract fun eval(env: Environment): Value

    data class Number(val value: Int) : Expression() {
        override fun eval(env: Environment) = Value.Number(value)
    }

    data class Boolean(val value: kotlin.Boolean) : Expression() {
        override fun eval(env: Environment) = Value.Boolean(value)
    }

    data class Symbol(val name: String) : Expression() {
        override fun eval(env: Environment) = env.get(name)
    }

    class ListExpr(val elements: kotlin.collections.List<Expression>) : Expression() {
        override fun eval(env: Environment): Value {
            if (elements.isEmpty()) {
                throw EmptyListException()
            }

            val head = elements[0]
            val args = elements.drop(1)

            return if (head is Symbol) {
                evalSpecialForm(head.name, args, env) ?: evalFunctionCall(head, args, env)
            } else {
                evalFunctionCall(head, args, env)
            }
        }

        private fun evalSpecialForm(name: String, args: List<Expression>, env: Environment): Value? {
            return when (name) {
                "define" -> evalDefine(args, env)
                "lambda" -> evalLambda(args, env)
                "if" -> evalIf(args, env)
                else -> null
            }
        }

        private fun evalDefine(args: List<Expression>, env: Environment): Value {
            val name = (args[0] as Symbol).name
            val value = args[1].eval(env)
            env.define(name, value)
            return Value.Symbol(name)
        }

        private fun evalLambda(args: List<Expression>, env: Environment): Value {
            val paramExprs = (args[0] as ListExpr).elements
            val params = paramExprs.map { expr -> (expr as Symbol).name }
            val body = args[1]
            return Value.Procedure(params, body, env)
        }

        private fun evalIf(args: List<Expression>, env: Environment): Value {
            val condition = args[0].eval(env)
            if (condition is Value.Boolean) {
                return if (condition.value) {
                    args[1].eval(env)
                } else {
                    args[2].eval(env)
                }
            } else {
                throw NotBooleanIfException(condition)
            }
        }

        private fun evalFunctionCall(funcExpr: Expression, args: List<Expression>, env: Environment): Value {
            val func = funcExpr.eval(env)
            val argVals = args.map { it.eval(env) }
            return Applier.apply(func, argVals)
        }
    }
}
