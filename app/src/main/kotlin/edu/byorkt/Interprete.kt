package edu.byorkt

/**
 * Intérprete principal del lenguaje Byorkt.
 * Coordina el análisis y evaluación de código fuente.
 */
class Interprete {
    /** Analizador léxico y sintáctico */
    private val analizador = Analizador()
    
    /** Motor de evaluación */
    private val evaluador = Evaluador()

    /**
     * Ejecuta código fuente y devuelve el resultado como cadena.
     * @param entrada Código fuente a ejecutar
     * @return Representación en cadena del resultado
     */
    fun run(entrada: String): String {
        val expresiones = leer(entrada)
        val resultado = evaluar(expresiones)
        return imprimir(resultado)
    }

    /**
     * Fase de lectura: convierte texto en expresiones.
     * @param entrada Texto de entrada
     * @return Lista de expresiones analizadas
     */
    private fun leer(entrada: String): List<Expresion> {
        return analizador.analizar(entrada)
    }

    /**
     * Fase de evaluación: procesa las expresiones.
     * @param expresiones Expresiones a evaluar
     * @return Resultado de la última expresión
     */
    private fun evaluar(expresiones: List<Expresion>): Valor {
        return evaluador.evaluarTodas(expresiones)
    }

    /**
     * Fase de impresión: convierte el resultado a cadena.
     * @param valor Valor a imprimir
     * @return Representación en cadena del valor
     */
    private fun imprimir(valor: Valor): String {
        return valor.toString()
    }
}
