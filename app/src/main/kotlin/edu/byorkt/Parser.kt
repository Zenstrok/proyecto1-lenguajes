package edu.byorkt

class Parser {

    fun parse(program: String): List<Expr> {
        val tokens = tokenize(program)
        val expressions = mutableListOf<Expr>()
        val iterator = tokens.listIterator()
        while (iterator.hasNext()) {
            expressions.add(parseExpr(iterator))
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

    private fun parseExpr(tokens: Iterator<String>): Expr {
        if (!tokens.hasNext()) error("Unexpected end of input")

        val token = tokens.next()
        return when {
            token == "(" -> {
                val elements = mutableListOf<Expr>()
                while (tokens.hasNext()) {
                    val token = tokens.next()
                    if (token == ")") break
                    tokens.previous()
                    elements.add(parseExpr(tokens))
                }
                Expr.List(elements)
            }
            token == "#t" -> Expr.Boolean(true)
            token == "#f" -> Expr.Boolean(false)
            token.toIntOrNull() != null -> Expr.Number(token.toInt())
            else -> Expr.Symbol(token)
        }
    }

    // Agrega una función de extensión útil para retroceder un paso en el iterator
    private fun <T> Iterator<T>.previous() {
        if (this is ListIterator) this.previous()
        else throw UnsupportedOperationException("Need a ListIterator to go back")
    }
}