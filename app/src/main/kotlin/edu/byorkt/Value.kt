package edu.byorkt

sealed class Value {
    data class Number(val value: Int) : Value() {
        override fun toString() = value.toString()
    }

    data class Boolean(val value: kotlin.Boolean) : Value() {
        override fun toString() = if (value) { "#t" } else { "#f" }
    }

    data class Procedure(val params: List<String>, val body: Expr, val env: Environment) : Value() {
        override fun toString() = "#<procedure>"
    }

    data class Builtin(val name: String, val impl: (List<Value>) -> Value) : Value() {
        override fun toString() = "#<builtin:$name>"
    }

    data class Symbol(val name: String) : Value() {
        override fun toString() = name
    }

    object Undefined : Value() {
        override fun toString() = "undefined"
    }
}
