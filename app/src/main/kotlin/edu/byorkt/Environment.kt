package edu.byorkt

class Environment(
    private val parent: Environment? = null,
    private val values: MutableMap<String, Value> = mutableMapOf()
) {

    fun define(name: String, value: Value) {
        values[name] = value
    }

    fun set(name: String, value: Value) {
        if (values.containsKey(name)) {
            values[name] = value
        } else {
            parent?.set(name, value)
                ?: throw RuntimeException("Variable '$name' no definida")
        }
    }

    fun get(name: String): Value {
        return values[name] ?: parent?.get(name)
        ?: throw RuntimeException("Variable '$name' no definida")
    }
}
