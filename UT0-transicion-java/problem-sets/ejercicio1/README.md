# Valores por defecto de los atributos

En Java, los **atributos de instancia** reciben automáticamente un valor por defecto si no se inicializan.

| Tipo                | Valor por defecto        |
| ------------------- | ------------------------ |
| int                 | 0                        |
| boolean             | false                    |
| double              | 0.0                      |
| char                | '\u0000' (carácter nulo) |
| referencia (String) | null                     |

Esto ocurre porque los atributos pertenecen a un **objeto**, y la JVM se encarga de inicializarlos al crearlo.

---

# Diferencia entre atributos y variables locales

## Atributos de instancia

* Se declaran dentro de la clase.
* Pertenecen a un objeto.
* Java les asigna **valores por defecto automáticamente**.

Ejemplo:

```java
int age;
boolean subscribed;
```

Estos valores se inicializan automáticamente cuando se crea el objeto.

---

## Variables locales

* Se declaran dentro de métodos.
* **No tienen valores por defecto**.
* El compilador obliga a inicializarlas antes de usarlas.

Ejemplo incorrecto:

```java
int x;
System.out.println(x); // Error de compilación
```

Error típico del compilador:

```
variable x might not have been initialized
```

Ejemplo correcto:

```java
int x;
x = 10;
System.out.println(x);
```

---

# Nombres de variables válidos e inválidos

## Ejemplos válidos

```java
int edad;
int edadUsuario;
int _contador;
int $precio;
```

Reglas:

* Pueden contener letras, números, `_` o `$`
* No pueden empezar con números
* No pueden ser palabras reservadas de Java

---

## Ejemplos inválidos

```java
int 2edad;      // comienza con número
int class;      // palabra reservada
int peso-total; // no se permiten guiones
int nombre usuario; // no se permiten espacios
```

Errores comunes del compilador:

```
identifier expected
not a statement
```

---

# Comentarios en Java

## Comentario de una línea

```java
// Este es un comentario de una línea
```

## Comentario de varias líneas

```java
/*
Este es un comentario
de varias líneas
en Java
*/
```

Los comentarios se utilizan para **documentar el código** y facilitar su comprensión.

---

# JVM, JRE y JDK

## JVM (Java Virtual Machine)

La **JVM** es la máquina virtual que ejecuta los programas Java.
Se encarga de interpretar el **bytecode** generado por el compilador y ejecutarlo en el sistema operativo.

Esto permite que Java sea **multiplataforma**.

---

## JRE (Java Runtime Environment)

El **JRE** es el entorno necesario para **ejecutar programas Java**.

Incluye:

* la JVM
* las bibliotecas estándar de Java

Un usuario que solo necesita ejecutar aplicaciones Java necesita el JRE.

---

## JDK (Java Development Kit)

El **JDK** es el kit de desarrollo de Java.

Incluye:

* el compilador `javac`
* herramientas de desarrollo
* el JRE
* bibliotecas de desarrollo

Se utiliza para **crear, compilar y ejecutar programas Java**.

---

# Tabla de tipos utilizados

| Tipo    | Categoría  | Ejemplo                   |
| ------- | ---------- | ------------------------- |
| int     | Primitivo  | `int edad = 25;`          |
| boolean | Primitivo  | `boolean activo = true;`  |
| double  | Primitivo  | `double peso = 70.5;`     |
| char    | Primitivo  | `char letra = 'A';`       |
| String  | Referencia | `String nombre = "Juan";` |

---

# Compilación y ejecución desde la terminal

## Compilar el programa

```bash
javac equipo/PruebaAtributos.java
```

## Ejecutar el programa

```bash
java equipo.PruebaAtributos
```

Si la clase no utiliza paquete:

```bash
javac PruebaAtributos.java
java PruebaAtributos
```

---

# Resultado esperado del programa

Al ejecutar el programa se observa algo similar a lo siguiente:

```
Valores por defecto de los atributos:
age: 0
subscribed: false
weight: 0.0
grade:
name: null

Variables locales inicializadas:
localInt: 10
localDouble: 2.5
```

El valor del `char` aparece vacío porque su valor por defecto es el carácter nulo (`'\u0000'`).
