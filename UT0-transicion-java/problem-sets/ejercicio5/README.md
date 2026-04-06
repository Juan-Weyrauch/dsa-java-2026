# Ejercicio 5 – Marcapasos: tipos de datos, objetos y memoria

## Descripción

Este ejercicio consiste en modelar un dispositivo médico (**marcapasos**) mediante una clase en Java, aplicando conceptos de **tipos de datos eficientes, encapsulación, identidad de objetos y uso de memoria**.

La clase define atributos relevantes del dispositivo, implementa métodos comunes de los objetos en Java (`toString`, `equals`, `hashCode`) y mantiene un contador de instancias creadas.

---

# Atributos de la clase

La clase `Marcapasos` contiene los siguientes atributos:

| Atributo            | Tipo         | Justificación                       |
| ------------------- | ------------ | ----------------------------------- |
| `idDispositivo`     | `int`        | Identificador único del dispositivo |
| `codigoFabricante`  | `int`        | Código asignado por el fabricante   |
| `latidosPorMinuto`  | `short`      | Valores típicos entre 40 y 200 bpm  |
| `nivelBateria`      | `byte`       | Nivel entre 0 y 100                 |
| `totalDispositivos` | `static int` | Contador de instancias creadas      |

Los tipos fueron elegidos buscando **minimizar el uso de memoria sin perder significado semántico**.

---

# Encapsulación

La clase utiliza **encapsulación** mediante atributos `private` y métodos `getter` y `setter`.

No todos los atributos tienen setter:

| Atributo           | Setter | Justificación                                 |
| ------------------ | ------ | --------------------------------------------- |
| `idDispositivo`    | ❌      | Es un identificador único que no debe cambiar |
| `codigoFabricante` | ❌      | Define la identidad del dispositivo           |
| `latidosPorMinuto` | ✔      | Puede cambiar según la configuración médica   |
| `nivelBateria`     | ✔      | Cambia con el uso del dispositivo             |

Los atributos que definen la **identidad del objeto** se mantienen inmutables.

---

# Contador de instancias

Se implementa una variable `static` que registra cuántos objetos `Marcapasos` se han creado.

Este contador se incrementa dentro del constructor cada vez que se crea una nueva instancia.

Esto permite conocer el número total de dispositivos creados durante la ejecución del programa.

---

# Representación del objeto (`toString`)

El método `toString()` se implementa para devolver una representación legible del estado del objeto.

Esto permite imprimir directamente el objeto usando:

```text
System.out.println(marcapasos);
```

Ejemplo de salida:

```
Marcapasos{idDispositivo=101, codigoFabricante=2001, latidosPorMinuto=72, nivelBateria=90%}
```

---

# Identidad de objetos (`equals` y `hashCode`)

La identidad de un marcapasos se define mediante:

* `codigoFabricante`
* `idDispositivo`

Dos objetos se consideran iguales si ambos valores coinciden.

Por esta razón, el método `equals()` compara estos dos atributos.

El método `hashCode()` se implementa de forma consistente con `equals`, utilizando los mismos campos para generar el valor hash.

Esto permite que los objetos funcionen correctamente en estructuras de datos como:

* `HashSet`
* `HashMap`
* `HashTable`

---

# Cálculo de memoria

Se calcula la memoria considerando únicamente las variables declaradas en la clase.

| Variable           | Tipo  | Tamaño  |
| ------------------ | ----- | ------- |
| `idDispositivo`    | int   | 4 bytes |
| `codigoFabricante` | int   | 4 bytes |
| `latidosPorMinuto` | short | 2 bytes |
| `nivelBateria`     | byte  | 1 byte  |

Memoria total aproximada:

```
4 + 4 + 2 + 1 = 11 bytes
```

Sin embargo, en la práctica los objetos suelen alinearse en memoria a múltiplos de 4 u 8 bytes, por lo que el consumo real podría ser de **12 o 16 bytes dependiendo de la JVM**.

---

# Simplificaciones realizadas

Para este cálculo se asumieron las siguientes simplificaciones:

* No se considera el **overhead del objeto en la JVM**
* No se considera **alineación de memoria**
* No se consideran **referencias internas del sistema**
* No se incluye la variable `static`, ya que pertenece a la clase y no a cada instancia

Por lo tanto, el cálculo representa solo una **estimación teórica del tamaño de los datos del objeto**.

---

# Ejemplo de salida

Ejemplo de creación e impresión de un objeto:

```
Marcapasos m1 = new Marcapasos(101, 2001, 72, 90);
System.out.println(m1);
```

Salida esperada:

```
Marcapasos{idDispositivo=101, codigoFabricante=2001, latidosPorMinuto=72, nivelBateria=90%}
```

---

# Conclusión

La implementación de la clase `Marcapasos` permite aplicar conceptos fundamentales de programación orientada a objetos en Java, incluyendo:

* encapsulación
* identidad de objetos
* optimización de tipos de datos
* manejo de memoria
* métodos estándar (`toString`, `equals`, `hashCode`)

Estos elementos son esenciales para el diseño correcto de clases dentro de aplicaciones Java.
