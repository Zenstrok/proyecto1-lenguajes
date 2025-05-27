package edu.byorkt

// Para cumplir con detekt
class ExcepcionVariableNoDefinida(nombre: String) :
    RuntimeException("Variable '$nombre' no definida")

class Entorno(
    private val padre: Entorno? = null,
    private val valores: MutableMap<String, Valor> = mutableMapOf()
) {
    fun definir(nombre: String, valor: Valor) {
        valores[nombre] = valor
    }

    fun asignar(nombre: String, valor: Valor) {
        if (valores.containsKey(nombre)) {
            valores[nombre] = valor
        } else {
            padre?.asignar(nombre, valor)
                ?: throw ExcepcionVariableNoDefinida(nombre)
        }
    }

    fun obtener(nombre: String): Valor {
        return valores[nombre] ?: padre?.obtener(nombre)
        ?: throw ExcepcionVariableNoDefinida(nombre)
    }
}
