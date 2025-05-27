package edu.byorkt

class ExcepcionAplicacionInvalida(func: Valor) :
    RuntimeException("No se puede aplicar: $func")

class Evaluador {
    private val entornoGlobal = Entorno()

    init {
        cargarFuncionesIncorporadas()
    }

    fun evaluarTodas(expresiones: List<Expresion>): Valor {
        var resultado: Valor = Valor.Indefinido
        for (expresion in expresiones) {
            resultado = evaluar(expresion, entornoGlobal)
        }
        return resultado
    }

    private fun evaluar(expresion: Expresion, entorno: Entorno): Valor = when (expresion) {
        is Expresion.Numero -> Valor.Numero(expresion.valor)
        is Expresion.Booleano -> Valor.Booleano(expresion.valor)
        is Expresion.Simbolo -> entorno.obtener(expresion.nombre)
        is Expresion.Lista -> evaluarLista(expresion, entorno)
    }

    private fun evaluarLista(expresion: Expresion.Lista, entorno: Entorno): Valor {
        if (expresion.elementos.isEmpty()) {
            throw ExcepcionListaVacia()
        }

        val cabeza = expresion.elementos[0]
        val argumentos = expresion.elementos.drop(1)

        return when {
            cabeza is Expresion.Simbolo -> manejarFormasEspeciales(cabeza, argumentos, entorno)
            else -> {
                val funcion = evaluar(cabeza, entorno)
                val valoresArgumentos = argumentos.map { evaluar(it, entorno) }
                Aplicador.aplicar(funcion, valoresArgumentos)
            }
        }
    }

    private fun manejarFormasEspeciales(cabeza: Expresion.Simbolo, argumentos: List<Expresion>, entorno: Entorno): Valor =
        when (cabeza.nombre) {
            "define" -> {
                val nombre = (argumentos[0] as Expresion.Simbolo).nombre
                val valor = evaluar(argumentos[1], entorno)
                entorno.definir(nombre, valor)
                Valor.Simbolo(nombre)
            }
            "lambda" -> {
                val expresionParametros = argumentos[0] as Expresion.Lista
                val parametros = expresionParametros.elementos.map { (it as Expresion.Simbolo).nombre }
                val cuerpo = argumentos[1]
                Valor.Procedimiento(parametros, cuerpo, entorno)
            }
            "if" -> {
                manejarCondicional(argumentos, entorno)
            }
            else -> {
                val funcion = evaluar(cabeza, entorno)
                val valoresArgumentos = argumentos.map { evaluar(it, entorno) }
                Aplicador.aplicar(funcion, valoresArgumentos)
            }
        }

    private fun manejarCondicional(argumentos: List<Expresion>, entorno: Entorno): Valor {
        val condicion = evaluar(argumentos[0], entorno)
        return when (condicion) {
            is Valor.Booleano -> {
                if (condicion.valor) {
                    evaluar(argumentos[1], entorno)
                } else {
                    evaluar(argumentos[2], entorno)
                }
            }
            else -> throw ExcepcionCondicionalNoBooleana(condicion)
        }
    }

    private fun cargarFuncionesIncorporadas() {
        cargarAritmeticas()
        cargarComparaciones()
        cargarLogicas()
    }

    private fun cargarAritmeticas() {
        entornoGlobal.definir(
            "+",
            Valor.FuncionPredefinida("+") {
                val suma = it.sumOf { v -> (v as Valor.Numero).valor }
                Valor.Numero(suma)
            }
        )
        entornoGlobal.definir(
            "-",
            Valor.FuncionPredefinida("-") {
                if (it.size == 1) {
                    Valor.Numero(-(it[0] as Valor.Numero).valor)
                } else {
                    val cabeza = (it[0] as Valor.Numero).valor
                    val resto = it.drop(1).map { v -> (v as Valor.Numero).valor }
                    Valor.Numero(resto.fold(cabeza) { acc, v -> acc - v })
                }
            }
        )
        entornoGlobal.definir(
            "*",
            Valor.FuncionPredefinida("*") {
                val producto = it.map { v -> (v as Valor.Numero).valor }.reduce { acc, v -> acc * v }
                Valor.Numero(producto)
            }
        )
        entornoGlobal.definir(
            "/",
            Valor.FuncionPredefinida("/") {
                val cabeza = (it[0] as Valor.Numero).valor
                val resto = it.drop(1).map { v -> (v as Valor.Numero).valor }
                Valor.Numero(resto.fold(cabeza) { acc, v -> acc / v })
            }
        )
        entornoGlobal.definir(
            "%",
            Valor.FuncionPredefinida("%") {
                val a = (it[0] as Valor.Numero).valor
                val b = (it[1] as Valor.Numero).valor
                Valor.Numero(a % b)
            }
        )
    }

    private fun cargarComparaciones() {
        entornoGlobal.definir(
            "<",
            Valor.FuncionPredefinida("<") {
                val (a, b) = it.map { v -> (v as Valor.Numero).valor }
                Valor.Booleano(a < b)
            }
        )
        entornoGlobal.definir(
            ">",
            Valor.FuncionPredefinida(">") {
                val (a, b) = it.map { v -> (v as Valor.Numero).valor }
                Valor.Booleano(a > b)
            }
        )
        entornoGlobal.definir(
            "<=",
            Valor.FuncionPredefinida("<=") {
                val (a, b) = it.map { v -> (v as Valor.Numero).valor }
                Valor.Booleano(a <= b)
            }
        )
        entornoGlobal.definir(
            ">=",
            Valor.FuncionPredefinida(">=") {
                val (a, b) = it.map { v -> (v as Valor.Numero).valor }
                Valor.Booleano(a >= b)
            }
        )
        entornoGlobal.definir(
            "equal?",
            Valor.FuncionPredefinida("equal?") {
                val (a, b) = it
                Valor.Booleano(a == b)
            }
        )
    }

    private fun cargarLogicas() {
        entornoGlobal.definir(
            "and",
            Valor.FuncionPredefinida("and") {
                val resultado = it.all { v -> (v as Valor.Booleano).valor }
                Valor.Booleano(resultado)
            }
        )
        entornoGlobal.definir(
            "or",
            Valor.FuncionPredefinida("or") {
                val resultado = it.any { v -> (v as Valor.Booleano).valor }
                Valor.Booleano(resultado)
            }
        )
        entornoGlobal.definir(
            "not",
            Valor.FuncionPredefinida("not") {
                val v = (it[0] as Valor.Booleano).valor
                Valor.Booleano(!v)
            }
        )
    }
}
