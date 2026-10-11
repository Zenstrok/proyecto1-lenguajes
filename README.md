# byorkt — A Racket Interpreter in Kotlin

An interpreter for a subset of the functional language **Racket**, built in **Kotlin** for the Programming Languages course (IC-4700) at Instituto Tecnológico de Costa Rica. It runs as an interactive **REPL** and also exposes an `Interpreter.run()` API that evaluates a whole program.

```racket
> (define square (lambda (x) (* x x)))
'square
> (square 5)
25
```

## Supported language
- Integers and booleans
- Arithmetic (`+ - * /`) and comparisons (`< > <= >= equal?`)
- Logic (`and`, `or`, `not`) and conditionals (`if`)
- `define` and `lambda` with **closures** and lexical scoping

## Architecture
| Component | Role |
|-----------|------|
| `Analizador` | Parser: turns source text into an S-expression AST (`Expresion`). |
| `Evaluador` | Evaluates expressions inside an environment. |
| `Aplicador` | Applies user-defined procedures and built-in functions. |
| `Entorno` | Chained environments that implement lexical scope and closures. |
| `Valor` | Runtime values: numbers, booleans, symbols and procedures. |

Errors such as undefined variables or invalid applications are reported with custom exceptions.

**Quality:** JUnit 5 unit tests · detekt static analysis · GitHub Actions CI

## Run
```bash
./gradlew run    # start the REPL
./gradlew test   # run the tests
```

# Original assignment (Spanish)

[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/YG3US8kh)
IC-4700 Lenguajes de programación  
Prof. Diego Munguia Molina  
IC-AL
---
# Proyecto Racket en Kotlin

## Objetivos de aprendizaje

1. Modelar el funcionamiento interno de los principios de programación funcional en un lenguaje de programación (III).
2. Programar soluciones a problemas computacionales utilizando el lenguaje de programación Kotlin (III).

## Descripción

Queremos desarrollar una implementación en Kotlin de un intérprete para un subconjunto del lenguaje de programación funcional 
Racket que llamaremos `byorkt`.

## Requerimientos funcionales

Nuestro intérprete debe funcionar como un REPL donde la persona usuaria ingresa S-expressions y el 
intérprete las evalúa para producir un valor que es impreso en pantalla como resultado.

```racket
> 1
1
> (define a 1)
'a
> (* 2 3)
6
>
```

Vamos a asumir que la entrada siempre es correcta y está bien formada. No es necesario hacer validaciones sintácticas o semánticas.

Adicionalmente, el proyecto debe ofrecer una clase llamada `edu.byorkt.Interpreter` que contenga un método `run` que recibe una hilera que contiene código de nuestro lenguaje `byorkt` y que retorna una hilera que representa el valor de la última expresión evaluada.

La hilera que recibe `run` representa un programa que contiene una expresión a evaluar por cada línea de la hilera, por ejemplo:

```kotlin
val código = """
(define identidad (lambda (x) x))
(identidad 5)
""".trimIndent()
```

Cuando se ejecuta `run` 

```kotlin
val interpreter = Interpreter()
println(interpreter.run(código))
```

Se debería imprimir en pantalla

```
5
```

Otro ejemplo:

```kotlin
val código = """
(define fact (lambda (n) (if (equal? n 0) 1 (* n (fact (- n 1))))))
(fact 5)
""".trimIndent()
```

Resultado de `println(interpreter.run(código))`

```
120
```

## Lenguaje

Especificamos a continuación los elementos del lenguaje que vamos a implementar.

### Valores

Números enteros

```racket
> 1
1
> -3
-3
```

Valores de verdad

```racket
> #t
#t
> #f
#f
```

### Primitivas

`lambda` para construir funciones.

```racket
> (lambda (x) x)
#<procedure>
> (lambda (x y) (+ x y))
#<procedure>
```

`define` para definir asociaciones entre nombres y valores con un alcance global.

```racket
> (define flag #t)
> flag
#t
> (define inc (lambda (n) (+ n 1)))
> inc
#<procedure>
> (inc 2)
3
```

### Funciones predefinidas *(built-ins)*

`if` para evaluar una expresión condicionalmente

```racket
> (if #t 1 0)
1
> (if #f 1 0)
0
```

`equal?` para determinar si dos valores son iguales.

```racket
> (equal? 1 1)
#t
> (equal? 1 2)
#f
> (equal? #f #f)
#t
> (define CERO 0)
CERO
> (equal? CERO 0)
#t
```

Operadores relacionales `<`, `>`, `<=`, `>=`

```racket
> (>= 1 1)
#t
> (> 1 1)
#f
> (< 0 1)
#t
> (<= 1 1)
#t
```

Operadores lógicos `and`, `or`, `not`

```racket
> (and (> 1 1) (equal? 1 1))
#f
> (or (> 1 1) (equal? 1 1))
#t
> (not (> 1 1))
#t
```

Operadores de aritmética entera `+` suma, `-` resta, `*` multiplicación, `/` cociente y `%` residudo.

```racket
> (+ 1 1)
2
> (* 2 3)
6
> (- 1 2)
-1
> (/ 4 2)
2
> (% 3 2)
1
```

### Casos de prueba

```racket
> (define identidad (lambda (x) x))
identidad
> (identidad 5)
5
> (define fact (lambda (n) (if (equal? n 0) 1 (* n (fact (- n 1))))))
fact
> (fact 5)
120
> (define fib (lambda (n) (if (or (equal? n 0) (equal? n 1)) 1 (+ (fib (- n 1)) (fib (- n 2))))))
fib
> (fib 5)
8
```

## Ambiente de desarrollo

Compilar el código y análisis estático (*linter*)
```bash
./gradlew clean build 
```
Correr las pruebas
```bash
./gradlew test
```

Correr el main
```bash
./gradlew run
```

## Metodología

* El proyecto se trabajará en equipos de entre una y tres personas.
* El proyecto se desarrollará en el transcurso de dos semanas.
* El proyecto se desarrollará en Kotlin siguiendo el paradigma orientado a objetos.
* El proyecto se entregará en el repositorio de git facilitado por la persona docente.
* El proyecto será evaluado a través de pruebas automatizadas aplicadas al código entregado y a través de una prueba 
escrita individual aplicada a todas las personas miembros del equipo.

## Rúbricas de evaluación

**Código producido** (50%)  
- Hay producción de código para implementar los requerimientos funcionales (15%)
- El código producido se apega al paradigma de orientación a objetos (15%)
- El código producido pasa los casos de prueba (20%)

**Defensa del proyecto** (50%)
- Prueba escrita de defensa del proyecto (50%)


