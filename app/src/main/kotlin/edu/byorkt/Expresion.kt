package edu.byorkt

class ExcepcionListaVacia :
    RuntimeException("Lista vacía no evaluable")

class ExcepcionCondicionalNoBooleana(condicion: Valor) :
    RuntimeException("La condición de 'if' no es booleana: $condicion")

sealed class Expresion {
    abstract fun evaluar(entorno: Entorno): Valor

    data class Numero(val valor: Int) : Expresion() {
        override fun evaluar(entorno: Entorno) = Valor.Numero(valor)
    }

    data class Booleano(val valor: Boolean) : Expresion() {
        override fun evaluar(entorno: Entorno) = Valor.Booleano(valor)
    }

    data class Simbolo(val nombre: String) : Expresion() {
        override fun evaluar(entorno: Entorno) = entorno.obtener(nombre)
    }

    class Lista(val elementos: List<Expresion>) : Expresion() {
        override fun evaluar(entorno: Entorno): Valor {
            if (elementos.isEmpty()) {
                throw ExcepcionListaVacia()
            }

            val cabeza = elementos[0]
            val argumentos = elementos.drop(1)

            return if (cabeza is Simbolo) {
                evaluarFormaEspecial(cabeza.nombre, argumentos, entorno)
                    ?: evaluarLlamadaFuncion(cabeza, argumentos, entorno)
            } else {
                evaluarLlamadaFuncion(cabeza, argumentos, entorno)
            }
        }

        private fun evaluarFormaEspecial(nombre: String, args: List<Expresion>, entorno: Entorno): Valor? {
            return when (nombre) {
                "define" -> evaluarDefine(args, entorno)
                "lambda" -> evaluarLambda(args, entorno)
                "if" -> evaluarIf(args, entorno)
                else -> null
            }
        }

        private fun evaluarDefine(args: List<Expresion>, entorno: Entorno): Valor {
            val nombre = (args[0] as Simbolo).nombre
            val valor = args[1].evaluar(entorno)
            entorno.definir(nombre, valor)
            return Valor.Simbolo(nombre)
        }

        private fun evaluarLambda(args: List<Expresion>, entorno: Entorno): Valor {
            val parametrosExpr = (args[0] as Lista).elementos
            val parametros = parametrosExpr.map { (it as Simbolo).nombre }
            val cuerpo = args[1]
            return Valor.Procedimiento(parametros, cuerpo, entorno)
        }

        private fun evaluarIf(args: List<Expresion>, entorno: Entorno): Valor {
            val condicion = args[0].evaluar(entorno)
            if (condicion is Valor.Booleano) {
                return if (condicion.valor) {
                    args[1].evaluar(entorno)
                } else {
                    args[2].evaluar(entorno)
                }
            } else {
                throw ExcepcionCondicionalNoBooleana(condicion)
            }
        }

        private fun evaluarLlamadaFuncion(funcExpr: Expresion, args: List<Expresion>, entorno: Entorno): Valor {
            val funcion = funcExpr.evaluar(entorno)
            val valoresArgs = args.map { it.evaluar(entorno) }
            return Aplicador.aplicar(funcion, valoresArgs)
        }
    }
}

