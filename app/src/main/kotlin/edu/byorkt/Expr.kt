package edu.byorkt

sealed class Expr {
    data class Number(val value: Int) : Expr()
    data class Boolean(val value: kotlin.Boolean) : Expr()
    data class Symbol(val name: String) : Expr()
    data class List(val elements: kotlin.collections.List<Expr>) : Expr()
}