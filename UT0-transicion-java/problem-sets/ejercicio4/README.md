# Ejercicio 4 – Factorial, primalidad y sumas condicionales

## Descripción

Este ejercicio consiste en implementar tres funcionalidades matemáticas básicas utilizando Java:

* Cálculo del **factorial** de un número usando exclusivamente un bucle `for`.
* Determinación de si un número es **primo**.
* Cálculo de una **suma condicional** basada en si el número es primo o no.

Las implementaciones deben contemplar **casos borde**, utilizar **estructuras de control adecuadas**, y estar organizadas en **métodos reutilizables y bien nombrados**.

---

# Estructura de la solución

La solución se organiza en tres métodos principales:

* `calculateFactorial(int num)`
  Calcula el factorial de un número utilizando únicamente un bucle `for`.

* `isPrime(long n)`
  Determina si un número es primo verificando si tiene divisores distintos de 1 y de sí mismo.

* `conditionalSum(int n)`
  Calcula una suma entre `0` y `n` dependiendo del resultado de `isPrime`:

  * Si el número es **primo**, se suman los **números pares**.
  * Si el número **no es primo**, se suman los **números impares**.

Para esta última operación se utiliza exclusivamente un bucle `while`, tal como solicita el ejercicio.

---

# Casos borde considerados

Durante la implementación se contemplaron varios casos especiales:

### Factorial

| Entrada         | Resultado         |
| --------------- | ----------------- |
| `0`             | 1                 |
| `1`             | 1                 |
| número negativo | error / excepción |

El factorial no está definido para números negativos, por lo que el programa lanza una excepción en ese caso.

Además, el resultado se almacena en un tipo `long`, ya que los factoriales crecen muy rápidamente y pueden superar la capacidad de un `int`.

---

### Primalidad

| Entrada         | Resultado                |
| --------------- | ------------------------ |
| `n ≤ 1`         | no primo                 |
| `2`             | primo                    |
| números mayores | depende de divisibilidad |

Los números menores o iguales a 1 no se consideran primos.

---

# Juego mínimo de pruebas manuales

Las siguientes pruebas se utilizaron para verificar el comportamiento del programa.

### Pruebas de factorial

| Entrada | Salida esperada |
| ------- | --------------- |
| `0`     | 1               |
| `1`     | 1               |
| `5`     | 120             |
| `10`    | 3628800         |

---

### Pruebas de primalidad

| Entrada | Resultado esperado |
| ------- | ------------------ |
| `2`     | primo              |
| `7`     | primo              |
| `8`     | no primo           |
| `1`     | no primo           |

---

### Pruebas de suma condicional

| Entrada | ¿Primo? | Operación       | Resultado esperado |
| ------- | ------- | --------------- | ------------------ |
| `7`     | sí      | suma de pares   | 12                 |
| `8`     | no      | suma de impares | 16                 |
| `2`     | sí      | suma de pares   | 2                  |
| `5`     | sí      | suma de pares   | 6                  |

---

# Decisiones de diseño

## 1. Separación de responsabilidades en métodos

La solución se dividió en métodos independientes para cada operación matemática:

* cálculo del factorial
* verificación de primalidad
* cálculo de la suma condicional

Esta separación mejora:

* **legibilidad**
* **reutilización del código**
* **facilidad de mantenimiento**

Además, permite reutilizar el método `isPrime` dentro del método `conditionalSum`.

---

## 2. Optimización del algoritmo de primalidad

Para determinar si un número es primo no es necesario probar todos los números hasta `n`.

En cambio, se verifica divisibilidad únicamente hasta la **raíz cuadrada del número**.
Si un número tiene un divisor mayor que su raíz cuadrada, necesariamente tendrá otro menor que ella.

Esto reduce significativamente la cantidad de operaciones necesarias y mejora la eficiencia del algoritmo.

---

# Conclusión

El programa implementa correctamente:

* el cálculo de factorial utilizando un bucle `for`
* la verificación de números primos
* la suma condicional basada en primalidad utilizando un bucle `while`

Además, se consideraron **casos borde**, se separaron las responsabilidades en métodos reutilizables y se aplicó una **optimización simple para la verificación de primalidad**, lo que mejora la eficiencia del programa.
