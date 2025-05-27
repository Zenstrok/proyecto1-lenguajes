package edu.byorkt

class Interprete {
    private val analizador = Analizador()
    private val evaluador = Evaluador()

    fun ejecutar(entrada: String): String {
        val expresiones = leer(entrada)
        val resultado = evaluar(expresiones)
        return imprimir(resultado)
    }

    private fun leer(entrada: String): List<Expresion> {
        return analizador.analizar(entrada)
    }

    private fun evaluar(expresiones: List<Expresion>): Valor {
        return evaluador.evaluarTodas(expresiones)
    }

    private fun imprimir(valor: Valor): String {
        return valor.toString()
    }
}
