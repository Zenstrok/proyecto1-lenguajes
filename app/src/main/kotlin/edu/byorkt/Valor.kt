package edu.byorkt

/**
 * Jerarquía de clases que representa los valores en el lenguaje Byorkt.
 * Implementa el patrón de diseño Value Object para representar los valores del lenguaje.
 */
sealed class Valor {
    /**
     * Representa un valor numérico entero.
     * @property valor Valor numérico almacenado
     */
    data class Numero(val valor: Int) : Valor() {
        override fun toString() = valor.toString()
    }

    /**
     * Representa un valor booleano.
     * @property valor Valor booleano almacenado (true/false)
     */
    data class Booleano(val valor: Boolean) : Valor() {
        override fun toString() = if (valor)  {
            "#t"
        } else {
            "#f"
        }
    }

    /**
     * Representa un procedimiento definido por el usuario.
     * @property parametros Lista de nombres de parámetros
     * @property cuerpo Expresión que forma el cuerpo del procedimiento
     * @property entorno Entorno léxico donde se definió el procedimiento
     */
    data class Procedimiento(val parametros: List<String>, val cuerpo: Expresion, val entorno: Entorno) : Valor() {
        override fun toString() = "#<procedure>"
    }

    /**
     * Representa una función predefinida en el lenguaje.
     * @property nombre Nombre de la función
     * @property impl Función de implementación que recibe argumentos y devuelve un valor
     */
    data class FuncionPredefinida(val nombre: String, val impl: (List<Valor>) -> Valor) : Valor() {
        override fun toString() = "#<builtin:$nombre>"
    }

    /**
     * Representa un símbolo (referencia a un valor).
     * @property nombre Nombre del símbolo
     */
    data class Simbolo(val nombre: String) : Valor() {
        override fun toString() = nombre
    }

    /**
     * Representa un valor indefinido o nulo.
     * Se usa como valor por defecto cuando una expresión no devuelve nada.
     */
    object Indefinido : Valor() {
        override fun toString() = "undefined"
    }
}

