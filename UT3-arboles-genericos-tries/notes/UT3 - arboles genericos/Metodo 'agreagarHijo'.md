### ¿Qué significa `agregarHijo(T padre, T hijo)` en el nodo?

El nodo **no sabe desde afuera** si él es el padre buscado. Entonces la lógica es recursiva:

```
¿Mi dato == padre?
  → Sí: agrego hijo a mi mapa de hijos. Listo.
  → No: le pregunto a cada uno de mis hijos que busquen ellos.
```

``` Java
@Override
public boolean agregarHijo(T padre, T hijo) {
    // ¿Soy yo el padre?
    if (this.dato.compareTo(padre) == 0) {
        if (hijos.containsKey(hijo)) return false; // ya existe
        hijos.put(hijo, new TNodoGenerico<>(hijo));
        return true;
    }

    // No soy yo — le pregunto a mis hijos recursivamente
    for (TNodoGenerico<T> nodoHijo : hijos.values()) {
        if (nodoHijo.agregarHijo(padre, hijo)) {
            return true; // alguien lo encontró y agregó
        }
    }

    return false; // no se encontró el padre en este subárbol
}
```

### ¿Por qué `this.put(padre, hijo)` no tiene sentido?

Porque `padre` es un **dato** (`T`), no el nodo. Si yo soy el nodo con dato `"A"` y me llaman con `agregarHijo("B", "C")`, yo no tengo nada que ver — tengo que delegarlo.

El `hijos.put()` solo se hace cuando confirmás que `this.dato == padre`.

## Flujo visual

```
agregarHijo("B", "D") llamado desde la raíz "A"

       A  ← ¿soy "B"? No → pregunto a mis hijos
      / \
     B   C  ← ¿soy "B"? Sí → hijos.put("D", new TNodo("D"))
            → return true
```

