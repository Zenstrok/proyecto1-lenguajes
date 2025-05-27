package edu.byorkt

/**
 * Excepción lanzada cuando se intenta acceder a una variable que no está definida.
 * @property nombre Nombre de la variable no definida
 */
class ExcepcionVariableNoDefinida(nombre: String) :
    RuntimeException("Variable '$nombre' no definida")

/**
 * Representa un entorno de evaluación con soporte para ámbitos anidados.
 * Permite definir, asignar y obtener valores de variables en el ámbito actual o en ámbitos padres.
 *
 * @property padre Entorno padre (para implementar el alcance léxico)
 * @property valores Mapa de variables definidas en este ámbito
 */
class Entorno(
    private val padre: Entorno? = null,
    private val valores: MutableMap<String, Valor> = mutableMapOf()
) {
    /**
     * Define una nueva variable en el ámbito actual.
     * @param nombre Nombre de la variable
     * @param valor Valor a asignar
     */
    fun definir(nombre: String, valor: Valor) {
        valores[nombre] = valor
    }

    /**
     * Asigna un nuevo valor a una variable existente.
     * Busca la variable en el ámbito actual y, si no la encuentra, en los ámbitos padres.
     * @param nombre Nombre de la variable
     * @param valor Nuevo valor a asignar
     * @throws ExcepcionVariableNoDefinida si la variable no existe en ningún ámbito
     */
    fun asignar(nombre: String, valor: Valor) {
        if (valores.containsKey(nombre)) {
            valores[nombre] = valor
        } else {
            padre?.asignar(nombre, valor)
                ?: throw ExcepcionVariableNoDefinida(nombre)
        }
    }

    /**
     * Obtiene el valor de una variable.
     * Busca la variable en el ámbito actual y, si no la encuentra, en los ámbitos padres.
     * @param nombre Nombre de la variable
     * @return Valor de la variable
     * @throws ExcepcionVariableNoDefinida si la variable no existe en ningún ámbito
     */
    fun obtener(nombre: String): Valor {
        return valores[nombre] ?: padre?.obtener(nombre)
        ?: throw ExcepcionVariableNoDefinida(nombre)
    }
}
