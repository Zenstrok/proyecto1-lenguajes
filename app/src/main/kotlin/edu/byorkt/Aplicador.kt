package edu.byorkt

/**
 * Objeto encargado de aplicar funciones a sus argumentos.
 * Maneja tanto funciones predefinidas como procedimientos definidos por el usuario.
 */

object Aplicador {
    /**
     * Aplica una función a una lista de argumentos.
     * @param funcion Función a aplicar (puede ser predefinida o un procedimiento)
     * @param argumentos Lista de argumentos para la función
     * @return Resultado de la aplicación de la función
     * @throws ExcepcionAplicacionInvalida si la función no es aplicable
     */
    fun aplicar(funcion: Valor, argumentos: List<Valor>): Valor {
        return when (funcion) {
            is Valor.FuncionPredefinida -> funcion.impl(argumentos)
            is Valor.Procedimiento -> {
                val entornoLocal = Entorno(funcion.entorno)
                funcion.parametros.zip(argumentos).forEach { (parametro, argumento) ->
                    entornoLocal.definir(parametro, argumento)
                }
                funcion.cuerpo.evaluar(entornoLocal)
            }
            else -> throw ExcepcionAplicacionInvalida(funcion)
        }
    }
}
