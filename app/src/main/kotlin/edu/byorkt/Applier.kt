package edu.byorkt

object Applier {
    fun apply(func: Value, args: List<Value>): Value {
        return when (func) {
            is Value.Builtin -> func.impl(args)
            is Value.Procedure -> {
                val localEnv = Environment(func.env)
                func.params.zip(args).forEach { (param, arg) ->
                    localEnv.define(param, arg)
                }
                func.body.eval(localEnv)
            }
            else -> throw InvalidApplicationException(func)
        }
    }
}
