package edu.byorkt

sealed class Valor {
    data class Numero(val valor: Int) : Valor() {
        override fun toString() = valor.toString()
    }

    data class Booleano(val valor: Boolean) : Valor() {
        override fun toString() = if (valor)  {
            "#t"
        } else {
            "#f"
        }
    }

    data class Procedimiento(val parametros: List<String>, val cuerpo: Expresion, val entorno: Entorno) : Valor() {
        override fun toString() = "#<procedure>"
    }

    data class FuncionPredefinida(val nombre: String, val impl: (List<Valor>) -> Valor) : Valor() {
        override fun toString() = "#<builtin:$nombre>"
    }

    data class Simbolo(val nombre: String) : Valor() {
        override fun toString() = nombre
    }

    object Indefinido : Valor() {
        override fun toString() = "undefined"
    }
}

