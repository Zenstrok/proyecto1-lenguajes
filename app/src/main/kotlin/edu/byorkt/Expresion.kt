package edu.byorkt

/**
 * Excepción lanzada cuando se intenta evaluar una lista vacía.
 */
class ExcepcionListaVacia :
    RuntimeException("Lista vacía no evaluable")

/**
 * Excepción lanzada cuando la condición de un 'if' no es un valor booleano.
 * @property condicion Valor que se usó como condición
 */
class ExcepcionCondicionalNoBooleana(condicion: Valor) :
    RuntimeException("La condición de 'if' no es booleana: $condicion")

/**
 * Jerarquía de clases que representa las expresiones del lenguaje Byorkt.
 * Implementa el patrón de diseño Interpreter para evaluar expresiones.
 */
sealed class Expresion {
    /**
     * Evalúa la expresión en el entorno dado.
     * @param entorno Entorno de evaluación actual
     * @return Valor resultante de la evaluación
     */
    abstract fun evaluar(entorno: Entorno): Valor

    /**
     * Representa un valor numérico entero.
     * @property valor Valor numérico
     */
    data class Numero(val valor: Int) : Expresion() {
        override fun evaluar(entorno: Entorno) = Valor.Numero(valor)
    }

    /**
     * Representa un valor booleano.
     * @property valor Valor booleano
     */
    data class Booleano(val valor: Boolean) : Expresion() {
        override fun evaluar(entorno: Entorno) = Valor.Booleano(valor)
    }

    /**
     * Representa un símbolo (variable o identificador).
     * @property nombre Nombre del símbolo
     */
    data class Simbolo(val nombre: String) : Expresion() {
        override fun evaluar(entorno: Entorno) = entorno.obtener(nombre)
    }

    /**
     * Representa una lista de expresiones, que puede ser una llamada a función o una forma especial.
     * @property elementos Lista de expresiones que componen la lista
     */
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

        // Evalúa una forma especial del lenguaje si el nombre coincide con alguna.
        // @param nombre Nombre de la forma especial
        // @param args Argumentos de la forma especial
        // @param entorno Entorno de evaluación
        // @return Resultado de evaluar la forma especial, o null si no es una forma especial
        private fun evaluarFormaEspecial(nombre: String, args: List<Expresion>, entorno: Entorno): Valor? {
            return when (nombre) {
                "define" -> evaluarDefine(args, entorno)
                "lambda" -> evaluarLambda(args, entorno)
                "if" -> evaluarIf(args, entorno)
                else -> null
            }
        }

        // Evalúa la forma especial 'define' que define una nueva variable.
        // @param args Argumentos [nombre, valor]
        // @param entorno Entorno donde se definirá la variable
        // @return Símbolo con el nombre de la variable definida
        private fun evaluarDefine(args: List<Expresion>, entorno: Entorno): Valor {
            val nombre = (args[0] as Simbolo).nombre
            val valor = args[1].evaluar(entorno)
            entorno.definir(nombre, valor)
            return Valor.Simbolo(nombre)
        }

        // Evalúa la forma especial 'lambda' que crea un procedimiento.
        // @param args Argumentos [parámetros, cuerpo]
        // @param entorno Entorno de cierre léxico
        // @return Nuevo procedimiento
        private fun evaluarLambda(args: List<Expresion>, entorno: Entorno): Valor {
            val parametrosExpr = (args[0] as Lista).elementos
            val parametros = parametrosExpr.map { (it as Simbolo).nombre }
            val cuerpo = args[1]
            return Valor.Procedimiento(parametros, cuerpo, entorno)
        }

        // Evalúa la forma especial 'if' (condicional).
        // @param args Argumentos [condición, entonces, sino]
        // @param entorno Entorno de evaluación
        // @return Resultado de evaluar la rama correspondiente
        // @throws ExcepcionCondicionalNoBooleana si la condición no es booleana
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

        // Evalúa una llamada a función normal.
        // @param funcExpr Expresión que evalúa a la función a llamar
        // @param args Argumentos a pasar a la función
        // @param entorno Entorno de evaluación
        // @return Resultado de aplicar la función a los argumentos
        private fun evaluarLlamadaFuncion(funcExpr: Expresion, args: List<Expresion>, entorno: Entorno): Valor {
            val funcion = funcExpr.evaluar(entorno)
            val valoresArgs = args.map { it.evaluar(entorno) }
            return Aplicador.aplicar(funcion, valoresArgs)
        }
    }
}
