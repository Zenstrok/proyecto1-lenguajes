package edu.byorkt

class Parser {

    fun parse(program: String): List<Expression> {
        val tokens = tokenize(program)
        val expressions = mutableListOf<Expression>()
        val iterator = tokens.listIterator()
        while (iterator.hasNext()) {
            expressions.add(parseExpression(iterator))
        }
        return expressions
    }

    private fun tokenize(input: String): List<String> {
        return input
            .replace("(", " ( ")
            .replace(")", " ) ")
            .split(Regex("\\s+"))
            .filter { it.isNotEmpty() }
    }

    private fun parseExpression(tokens: Iterator<String>): Expression {
        if (!tokens.hasNext()) {
            error("Unexpected end of input")
        }

        val token = tokens.next()
        return when {
            token == "(" -> {
                val elements = mutableListOf<Expression>()
                while (tokens.hasNext()) {
                    val token = tokens.next()
                    if (token == ")") {
                        break
                    }
                    tokens.previous()
                    elements.add(parseExpression(tokens))
                }
                Expression.ListExpr(elements)
            }
            token == "#t" -> Expression.Boolean(true)
            token == "#f" -> Expression.Boolean(false)
            token.toIntOrNull() != null -> Expression.Number(token.toInt())
            else -> Expression.Symbol(token)
        }
    }

    // Agrega una función de extensión útil para retroceder un paso en el iterator
    private fun <T> Iterator<T>.previous() {
        if (this is ListIterator) {
            this.previous()
        } else {
            throw UnsupportedOperationException("Need a ListIterator to go back")
        }
    }
}
