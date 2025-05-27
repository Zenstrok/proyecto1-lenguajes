package edu.byorkt

class Analizador {
    fun analizar(programa: String): List<Expresion> {
        val tokens = tokenizar(programa)
        val expresiones = mutableListOf<Expresion>()
        val iterador = tokens.listIterator()
        while (iterador.hasNext()) {
            expresiones.add(analizarExpresion(iterador))
        }
        return expresiones
    }

    private fun tokenizar(entrada: String): List<String> {
        return entrada
            .replace("(", " ( ")
            .replace(")", " ) ")
            .split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
    }

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
                    if (tokenInterno == ")") break
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

    // Extensión para retroceder en el iterador
    private fun <T> Iterator<T>.retroceder() {
        if (this is ListIterator) {
            this.previous()
        } else {
            throw UnsupportedOperationException("Se necesita un ListIterator para retroceder")
        }
    }
}
