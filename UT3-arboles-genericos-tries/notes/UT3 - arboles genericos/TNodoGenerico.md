``` Java
public class TNodoGenerico<T extends Comparable<T>> implements TINodoGenerico<T> { private T dato; 
private Map<T, TNodoGenerico<T>> hijos; 
// ❌ private boolean esFinDePalabra — esto no va acá 
// ❌ private TNodoGenerico<T> padre — explicación abajo 

public TNodoGenerico(T dato) { 
this.dato = dato; 
this.hijos = new HashMap<>(); } }
```

Sobre `padre`: guardarlo en el nodo crea **acoplamiento bidireccional** (padre apunta a hijo, hijo apunta a padre), lo que complica mucho el mantenimiento. `obtenerPadre(criterio)` se puede implementar recursivamente **sin** guardar la referencia — el nodo busca en sus hijos si alguno satisface el criterio, y si es así, se devuelve a sí mismo:

``` Java
@Override
public TINodoGenerico<T> obtenerPadre(Comparable<T> criterio) {
    // Si alguno de mis hijos satisface el criterio, yo soy el padre
    for (TNodoGenerico<T> hijo : hijos.values()) {
        if (criterio.compareTo(hijo.dato) == 0) {
            return this;
        }
    }
    // Si no, busco recursivamente en mis hijos
    for (TNodoGenerico<T> hijo : hijos.values()) {
        TINodoGenerico<T> resultado = hijo.obtenerPadre(criterio);
        if (resultado != null) return resultado;
    }
    return null;
}
```

