package edu.byorkt

object Aplicador {
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
