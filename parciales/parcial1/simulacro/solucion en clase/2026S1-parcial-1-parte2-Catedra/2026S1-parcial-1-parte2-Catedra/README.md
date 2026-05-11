# Sistema de Gestión de Tareas — Naves Autónomas (AEP)

Proyecto del Parcial 1 - Parte 2 de la asignatura **Algoritmos y Estructuras de Datos**
(Universidad Católica del Uruguay, semestre 2026-S1).

Implementa un sistema que administra las tareas operativas que recibe una nave autónoma
en tiempo real, respetando reglas de prioridad, capacidad e historial.

---

## 1. Requerimientos de ejecución

| Componente            | Versión mínima | Notas                                      |
|-----------------------|----------------|--------------------------------------------|
| **JDK**               | 11             | Definido en `pom.xml` (`maven.compiler.*`) |
| **Apache Maven**      | 3.6+           | Para compilar, testear y ejecutar          |
| **JUnit**             | 4.13.2         | Resuelto automáticamente por Maven         |

### Compilar

```bash
mvn -q -DskipTests compile
```

### Ejecutar la suite de tests

```bash
mvn -q test
```

Salida esperada: `Tests run: 25, Failures: 0, Errors: 0, Skipped: 0`.

### Ejecutar el programa principal

El `Main` carga las tareas del archivo `naves.txt` ubicado en la raíz del proyecto,
las procesa y demuestra la búsqueda en el historial:

```bash
mvn -q exec:java -Dexec.mainClass=ucu.edu.aed.Main
```

Para usar otro archivo:

```bash
mvn -q exec:java -Dexec.mainClass=ucu.edu.aed.Main -Dexec.args="ruta/a/mis-tareas.txt"
```

**Formato de `naves.txt`** (una tarea por línea, separadores `;`):

```
# Las líneas que comienzan con # se ignoran
id;descripcion;criticidad
1;Diagnóstico motor principal;1
2;Calibración antena;3
```

---

## 2. Arquitectura del repositorio

```
proyecto-base-ut02/
├── pom.xml                    Build, JDK 11, JUnit 4.13.2
├── naves.txt                  Set de tareas de ejemplo (120 entradas)
├── README.md
└── src/
    ├── main/java/ucu/edu/aed/
    │   ├── Main.java                              Entry point, lectura de naves.txt
    │   ├── modelo/
    │   │   ├── Tarea.java                         Entidad de dominio
    │   │   └── CriterioPorId.java                 Comparable sintético para búsqueda
    │   ├── sistema/
    │   │   └── SistemaGestionNave.java            Lógica R1-R4 (núcleo del sistema)
    │   └── tda/
    │       ├── TDAElemento.java        (provista) Interfaz nodo árbol binario
    │       ├── TDAArbolBinario.java    (provista) Interfaz árbol binario
    │       ├── AVLElemento.java                   Implementación AVL del nodo
    │       └── AVLArbol.java                      Implementación AVL del árbol
    └── test/java/ucu/edu/aed/sistema/
        ├── RecibirTareaTest.java                  6 tests de R1
        ├── ProcesarTareaTest.java                 7 tests de R2
        ├── BuscarTareaProcesadaTest.java          6 tests de R3
        └── CancelarTareaTest.java                 6 tests de R4
```

### Capas y responsabilidades

- **`tda`** — Estructuras de datos genéricas reutilizables. Expone los TDAs provistos
  por la cátedra y los completa con una implementación **AVL** (auto-balanceada) que
  garantiza altura O(log n) sin importar el orden de inserción.
- **`modelo`** — Entidades del dominio (`Tarea`) y comparables auxiliares (`CriterioPorId`)
  que permiten usar el árbol AVL como índice por id.
- **`sistema`** — Clase `SistemaGestionNave`, único punto de acceso público al sistema:
  agrupa las colas internas, el buffer/historial de procesadas y los cuatro métodos
  funcionales (`recibirTarea`, `procesarTarea`, `buscarTareaProcesada`, `cancelarTarea`).
- **`Main`** — Capa de aplicación: parsea el archivo de entrada e invoca al sistema.
- **`test/.../sistema`** — Pruebas unitarias dirigidas exclusivamente a los
  requerimientos funcionales (no se testean los TDAs base).

---

## 3. Resolución de los requerimientos funcionales

### R1 — Recepción de tareas (`recibirTarea`)

**Reglas:**

- Tope global de **25 tareas pendientes**.
- Tope de **10 tareas críticas** (criticidad 1 o 2) pendientes simultáneamente.
- Lo que no entra debe esperar **hasta poder ingresar** (no se descarta).

**Resolución:** se mantienen tres colas independientes.

| Cola             | Contenido                                                               |
|------------------|-------------------------------------------------------------------------|
| `colaCriticas`   | Tareas pendientes con criticidad 1 o 2, ordenadas por criticidad ascendente y, dentro de cada nivel, por orden de inserción (FIFO). El frente es siempre la próxima a ejecutar. |
| `colaNormales`   | Tareas pendientes con criticidad 3 o 4 en FIFO estricto.                |
| `colaEspera`     | Tareas que llegaron pero no pudieron admitirse aún.                      |

`recibirTarea` invoca `intentarIngresar`, que valida ambos topes antes de hacer el
push correspondiente. Si la admisión falla, la tarea se encola en `colaEspera`.
Cualquier evento que libere cupo (`procesarTarea` o `cancelarTarea`) dispara
`drenarColaEspera`, que reintenta admitir las tareas en su orden original tantas
pasadas como sea necesario hasta no poder admitir más.

### R2 — Procesamiento de tareas (`procesarTarea`)

**Reglas:**

- Siempre se ejecuta primero la tarea de **mayor criticidad disponible**.
- En **empate** de criticidad, gana el orden de llegada (FIFO).
- Para criticidades 3 y 4, la consigna agrega *"deben organizarse respetando
  estrictamente el orden en que fueron recibidas"*: dentro del grupo normal se
  aplica **FIFO puro** sin reordenar por criticidad (si una crit 4 llegó antes
  que una crit 3, se ejecuta primero la 4). Las críticas (1 y 2) sí se reordenan
  por nivel.

**Resolución:** la propia organización de las colas resuelve el problema en O(1)
amortizado:

1. Si `colaCriticas` no está vacía, se extrae su cabeza — por construcción es la
   tarea de menor número de criticidad y, dentro de ese nivel, la de menor orden
   de llegada.
2. En caso contrario se extrae la cabeza de `colaNormales` (FIFO directo).

Al finalizar se delega en `registrarProcesada` (R3) y se vuelve a drenar la cola
de espera, ya que la liberación de cupo puede admitir tareas que aguardaban.

### R3 — Registro de tareas procesadas (`buscarTareaProcesada`)

**Reglas:**

- Una vez procesadas **al menos 75 tareas**, deben almacenarse en una estructura
  con **búsqueda por id en O(log n) garantizado**, independientemente del orden
  de inserción.

**Resolución:** combinación de **buffer + AVL**:

- Mientras `totalProcesadas < 75` se guarda cada tarea en una `LinkedList`
  (`bufferProcesadas`). Las búsquedas en esta etapa son lineales, lo que el
  enunciado permite porque la garantía solo aplica a partir del umbral.
- Al procesar la 75ª tarea, el sistema:
  1. Crea un `AVLArbol<Tarea>` (campo `historial`).
  2. Vuelca todas las tareas del buffer al árbol.
  3. Descarta el buffer.
- A partir de allí, cada nueva procesada se inserta directamente en el AVL.
- `buscarTareaProcesada(id)` consulta el AVL si existe, usando `CriterioPorId`
  como comparable sintético.

**¿Por qué AVL y no un BST simple?** Un BST común degenera a O(n) cuando los
ids llegan ordenados (caso real al insertar tareas históricas en orden). El AVL
mantiene factor de balance en {-1, 0, 1} mediante rotaciones tras cada inserción,
preservando altura O(log n) en peor caso. Un test (`arbolHistorialMantieneAlturaLogaritmica`)
inserta 100 ids descendentes y verifica que la altura del árbol no supera 8.

### R4 — Cancelación de tareas (`cancelarTarea`)

**Reglas:**

- Se permite cancelar tareas pendientes a partir de su id.
- **Solo** pueden cancelarse tareas **aún no procesadas** — el historial es inmutable.

**Resolución:** `cancelarTarea` recorre las tres colas no procesadas
(`colaCriticas`, `colaNormales`, `colaEspera`) en ese orden y remueve la primera
coincidencia. Si encuentra una, dispara `drenarColaEspera` para aprovechar el
cupo recién liberado. El historial AVL nunca se consulta ni se modifica desde
este método, lo que garantiza que las tareas ya procesadas permanezcan intactas
y siempre recuperables vía `buscarTareaProcesada`.

---

## 4. Decisiones de modelado de entidades

El enunciado define dos conceptos centrales: la **tarea** (entidad de dominio) y
el **sistema de gestión** (servicio que la administra). Alrededor de ellos surgen
dos clases auxiliares (`CriterioPorId` y los nodos AVL) cuya razón de ser también
proviene del modelado.

### 4.1 `Tarea` — entidad de dominio

```java
public final class Tarea implements Comparable<Tarea> {
    private final int id;
    private final String descripcion;
    private final int criticidad;
}
```

Tres atributos, exactamente los del enunciado.

| Atributo      | Justificación del modelado                                                                                                                                                                                                       |
|---------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `id`          | El enunciado lo califica de "único", por lo que se usa como **clave primaria** en todas las búsquedas (R3, R4). Tipo `int` por simplicidad y eficiencia.                                                                          |
| `descripcion` | Texto libre. No participa en ninguna lógica de negocio: solo se transporta y se imprime.                                                                                                                                          |
| `criticidad`  | Nivel 1–4 modelado como `int`. Se valida en el constructor para garantizar el invariante. La criticidad domina dos reglas distintas (cuota R1 y orden R2) y por eso se materializa en un atributo propio en lugar de derivarla. |

**¿Y el orden de llegada para el desempate FIFO de R2?** No se modela como atributo
de `Tarea`. Es responsabilidad de las **colas internas del sistema**: cada cola
preserva el orden de inserción por construcción (FIFO en `ArrayDeque`, inserción
ordenada por criticidad sin alterar el suborden FIFO en `colaCriticas`). Agregar
un campo `ordenLlegada` a la tarea sería redundante y obligaría al cliente a
generar un contador global, contaminando el contrato.

**Decisiones clave:**

- **Inmutabilidad** (clase `final`, todos los campos `final`, sin setters): una
  tarea representa un evento ya ocurrido; su identidad y atributos no deben
  cambiar mientras navega por las colas o el historial. Esto evita bugs sutiles
  cuando la misma tarea se referencia desde varias estructuras.
- **`Comparable<Tarea>` por id**: el árbol AVL del historial necesita un orden
  total sobre las tareas. Como la búsqueda de R3 es por id, ordenar por id
  permite usar la propia tarea como dato del árbol, sin estructuras paralelas.
- **`equals` y `hashCode` también por id**: respeta la recomendación del contrato
  de `Comparable` (consistencia con `equals`) y evita comportamientos sorpresa
  si en el futuro la tarea se usa en colecciones que combinan orden e identidad.
- **`esCritica()` como método derivado**, no como atributo: la criticidad ya
  contiene la información, exponer un booleano redundante invitaría a
  inconsistencias. El método encapsula la regla "1 o 2 son críticas" en un solo
  lugar para que cualquier cambio futuro (ej. agregar criticidad 0) sea trivial.
- **Validación en constructor** (`IllegalArgumentException` si criticidad ∉ [1,4]):
  evita que una tarea inválida llegue a las colas, donde el error sería más
  difícil de diagnosticar.

### 4.2 `CriterioPorId` — comparable sintético

```java
public class CriterioPorId implements Comparable<Tarea> { ... }
```

Surge directamente del **contrato del TDA Árbol Binario**: las operaciones
`buscar` y `eliminar` reciben `Comparable<T>`, no `T`. Esto permite buscar sin
construir una `Tarea` completa (lo que requeriría inventar descripción y una
criticidad arbitraria solo para tirarlas).

**Alternativas descartadas:**

- *Buscar pasando una `Tarea` con id real y campos dummy*: rompe la inmutabilidad
  conceptual y obliga a aceptar criticidades inválidas en el constructor.
- *Cambiar la firma del TDA a `buscar(T)`*: el TDA es provisto por la cátedra y
  está diseñado precisamente para soportar criterios sintéticos.

`CriterioPorId` resuelve esto con una clase de una sola responsabilidad: comparar
un id contra el id de una tarea, devolviendo el mismo resultado que
`Tarea.compareTo`. La consistencia de orden con `Tarea` es **invariante crítico**:
si `Tarea` cambiara su ordenamiento, `CriterioPorId` debe cambiar a la par o el
árbol fallaría silenciosamente.

### 4.3 `SistemaGestionNave` — agregado de servicio

El sistema **no es una entidad de dominio**, sino el agregado que coordina la
vida de las tareas. Modela cuatro estados visibles desde afuera:

```
              recibirTarea()                procesarTarea()
[mundo] ─────────────────▶ EN ESPERA ─┐          │
                                      ▼          ▼
                                 PENDIENTE ──▶ PROCESADA
                                      │
                                      └─ cancelarTarea() ─▶ [removida]
```

**Decisiones de modelado:**

- **Tres colas en lugar de una `PriorityQueue` única**: aunque conceptualmente
  parece más simple, una sola estructura no captura las dos restricciones
  *independientes* de R1 (tope total **y** cuota crítica), ni la regla de R2 que
  exige FIFO estricto entre normales sin que las críticas las influyan. Tres
  colas hacen explícita cada subpoblación y simplifican los chequeos.
- **Cola de espera separada de las pendientes**: las tareas en espera **no
  cuentan** contra los topes, por lo que viven en su propia estructura. Mezclarlas
  obligaría a duplicar contadores.
- **Buffer + AVL en vez de AVL desde el principio**: el enunciado pide la garantía
  logarítmica solo a partir de las 75 procesadas. Un buffer permite mantener la
  inserción en O(1) durante el período inicial y diferir el costo del árbol hasta
  que sea necesario, manteniendo a la vez una API uniforme (`buscarTareaProcesada`
  funciona desde la primera tarea).
- **`totalProcesadas` como contador independiente** (no `bufferProcesadas.size()
  + historial.cantidadNodos()`): es la fuente de verdad para detectar el cruce
  del umbral y se mantiene válido aunque luego se vacíe el buffer.

### 4.4 `AVLElemento` / `AVLArbol` — implementación del TDA

No son entidades de dominio sino la materialización de los TDAs provistos. La
decisión de modelado es elegir **AVL** sobre alternativas como BST simple,
*Red-Black tree* o *Skip list*:

| Alternativa     | Por qué se descartó                                                            |
|-----------------|--------------------------------------------------------------------------------|
| BST simple      | No cumple R3: peor caso O(n) si los ids llegan ordenados.                      |
| Red-Black tree  | Cumple, pero sus invariantes son más complejos de explicar y testear.          |
| Skip list       | Cumple en promedio, pero el enunciado pide *en todos los casos*: no aplica.    |
| AVL             | **Elegido**: balance estricto, peor caso O(log n) demostrable, rotaciones simples y bien conocidas. |

El cast `Comparable<T>` → `T`, inevitable por la firma del TDA, se centraliza en
`AVLElemento.comoT` con `@SuppressWarnings("unchecked")` documentado. Es el
único punto del proyecto donde se rompe la seguridad de tipos.

---

## 5. Decisiones de diseño relevantes

- **`AVLElemento.comoT`** centraliza el único cast inseguro (`Comparable<T>` → `T`,
  forzado por la firma del TDA) en un helper documentado con `@SuppressWarnings`,
  manteniendo el resto del código libre de avisos.
- **`SistemaGestionNave#getHistorial`** es package-private, exclusivamente para
  que los tests puedan inspeccionar la estructura interna sin abrir la API pública.
- **Tests por requerimiento, no por clase**: los archivos de test
  (`RecibirTareaTest`, `ProcesarTareaTest`, etc.) están organizados por R1–R4 y
  no por clase implementada. Esto refleja que el contrato a verificar es el del
  enunciado, no la estructura interna.
