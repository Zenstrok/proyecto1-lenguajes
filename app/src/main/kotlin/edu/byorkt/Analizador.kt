package edu.byorkt

/**
 * Analizador léxico y sintáctico para el lenguaje Byorkt.
 * Se encarga de transformar cadenas de texto en un árbol de sintaxis abstracta (AST).
 */

class Analizador {
    // Analiza un programa completo dividiéndolo en expresiones.
    // @param programa Código fuente a analizar
    // @return Lista de expresiones analizadas
    fun analizar(programa: String): List<Expresion> {
        val tokens = tokenizar(programa)
        val expresiones = mutableListOf<Expresion>()
        val iterador = tokens.listIterator()
        while (iterador.hasNext()) {
            expresiones.add(analizarExpresion(iterador))
        }
        return expresiones
    }

    // Divide la cadena de entrada en tokens individuales.
    // @param entrada Cadena de entrada a tokenizar
    // @return Lista de tokens
    private fun tokenizar(entrada: String): List<String> {
        return entrada
            .replace("(", " ( ")
            .replace(")", " ) ")
            .split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
    }

    // Analiza una expresión a partir de una secuencia de tokens.
    // @param tokens Iterador de tokens a analizar
    // @return Expresión analizada
    // @throws RuntimeException si la entrada es inválida o está incompleta
    private fun analizarExpresion(tokens: Iterator<String>): Expresion {
        if (!tokens.hasNext()) {
            error("Fin inesperado de la entrada")
        }

        val token = tokens.next()
        return when {
            token == "(" -> {
                val elementos = mutableListOf<Expresion>()
                while (tokens.hasNext()) {
                    val tokenInterno = tokens.next()
                    if (tokenInterno == ")") {
                        break
                    }
                    tokens.retroceder()
                    elementos.add(analizarExpresion(tokens))
                }
                Expresion.Lista(elementos)
            }
            token == "#t" -> Expresion.Booleano(true)
            token == "#f" -> Expresion.Booleano(false)
            token.toIntOrNull() != null -> Expresion.Numero(token.toInt())
            else -> Expresion.Simbolo(token)
        }
    }

    // Extensión para retroceder en un iterador.
    // @throws UnsupportedOperationException si el iterador no soporta retroceso
    private fun <T> Iterator<T>.retroceder() {
        if (this is ListIterator) {
            this.previous()
        } else {
            throw UnsupportedOperationException("Se necesita un ListIterator para retroceder")
        }
    }
}
