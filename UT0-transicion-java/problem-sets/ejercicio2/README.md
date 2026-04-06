# Ejercicio 2 – Operadores, expresiones y conversiones básicas

## Descripción

Este ejercicio consiste en analizar el programa `ArithmeticDemo`, identificar mejoras usando **operadores de asignación compuesta**, estudiar el **orden de evaluación de expresiones**, y crear una variante que reciba **argumentos desde la línea de comandos**, los convierta a números y realice operaciones aritméticas básicas.

---

# Uso de asignaciones compuestas

En el programa original se utilizan asignaciones simples como:

```
result = result - 1;
result = result * 2;
result = result / 2;
result = result + 8;
result = result % 7;
```

Estas pueden simplificarse utilizando **operadores de asignación compuesta**, que hacen el código más corto y legible.

| Expresión original    | Forma compuesta |
| --------------------- | --------------- |
| `result = result - 1` | `result -= 1`   |
| `result = result * 2` | `result *= 2`   |
| `result = result / 2` | `result /= 2`   |
| `result = result + 8` | `result += 8`   |
| `result = result % 7` | `result %= 7`   |

---

# Explicación de la instrucción

```
int a = 5;
int i = 3;
a += ++i;
```

## Paso a paso

Estado inicial:

```
a = 5
i = 3
```

El operador `++i` es un **pre-incremento**, lo que significa que primero se incrementa el valor de `i` y luego se utiliza en la expresión.

```
++i  →  i = i + 1
```

Por lo tanto:

```
i pasa de 3 a 4
```

Luego se ejecuta la asignación compuesta:

```
a += ++i
```

que es equivalente a:

```
a = a + (++i)
```

Reemplazando los valores:

```
a = 5 + 4
```

Resultado final:

```
a = 9
i = 4
```

---

# Orden de evaluación de expresiones

## Expresión 1

```
int result = 1 + 2;
```

Orden de evaluación:

1. Se evalúa la expresión `1 + 2`.
2. El resultado `3` se asigna a la variable `result`.

Resultado:

```
result = 3
```

---

## Expresión 2

```
result *= 2;
```

Esta instrucción es equivalente a:

```
result = result * 2;
```

Orden:

1. Se toma el valor actual de `result`.
2. Se multiplica por `2`.
3. El resultado se guarda nuevamente en `result`.

---

## Expresión 3

```
a += ++i;
```

Orden:

1. Se ejecuta el **pre-incremento** `++i`.
2. Se obtiene el nuevo valor de `i`.
3. Se suma al valor actual de `a`.
4. El resultado se almacena en `a`.

---

# Compilación y ejecución

## Compilar

```
javac ArithmeticDemo.java
```

## Ejecutar

```
java ArithmeticDemo 10 5
```

---

# Ejemplo de salida

```
Resultado final: 4
Valor de a: 9
Valor de i: 4
```

---

# Bibliografía

* Oracle Java Tutorial – Operators
* Oracle Java Tutorial – Expressions
* Oracle Java Tutorial – Converting Between Numbers and Strings
* Handbook de Java, secciones 5 y 20
