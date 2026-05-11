package ucu.edu.aed.tda.generic_trie;

import java.util.List;
import java.util.function.Consumer;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import ucu.edu.aed.tda.generic_trie.TINodoGenerico;

public class TNodoGenerico<T extends Comparable<T>> implements TINodoGenerico<T> {
    private Map<T, TNodoGenerico<T>> hijos = new HashMap<>();
    private T dato;

    public TNodoGenerico(T dato) {
        this.dato = dato;
        this.hijos = new HashMap<>();
    }

    @Override
    public T getDato() {
        return this.dato;
    }

    /**
     * Agrega un nodo con dato T al padre
     */
    @Override
    public boolean agregarHijo(T padre, T hijo) {
        // si soy el padre
        if (this.dato.compareTo(padre) == 0) {
            if (hijos.containsKey(hijo)) {
                return false; // ya existe
            } else {
                hijos.put(hijo, new TNodoGenerico<>(hijo));
                return true;
            }
        }

        // si no soy el padre (recursivo)
        for (TNodoGenerico<T> nodoHijo : this.hijos.values()) {
            if (nodoHijo.agregarHijo(padre, hijo)) {
                return true; // alguien lo encontró y lo agregó
            }
        }

        return false;
    }

    /**
     * elimina el nodo y sus hijos directos utilizando el criterio pasado
     */
    @Override
    public TINodoGenerico<T> eliminar(Comparable<T> criterio) {
        // reviso si alguno de mis hijos directos es el nodo a eliminar
        for (T keyHijo : hijos.keySet()) {
            if (criterio.compareTo(keyHijo) == 0) {
                // lo encontre, lo saco del mapa
                TNodoGenerico<T> eliminado = hijos.remove(keyHijo);
                return eliminado;
            }
        }

        // no es hijo directo, pregunto recursivamente
        for (TNodoGenerico<T> hijo : hijos.values()) {
            TINodoGenerico<T> resultado = hijo.eliminar(criterio);
            if (resultado != null) {
                return resultado; // se encontró
            }
        }

        return null; // no se encontró
    }

    /**
     * busca un nodo utilizando el criterio pasado
     */
    @Override
    public TINodoGenerico<T> buscar(Comparable<T> criterio) {
        // soy el nodo buscado?
        if (criterio.compareTo(this.dato) == 0) {
            return this;
        }

        // si no, reviso a mis hijos
        for (TINodoGenerico<T> hijo : this.hijos.values()) {
            TINodoGenerico<T> resultado = hijo.buscar(criterio);
            if (resultado != null) {
                return resultado;
            }
        }

        return null;
    }

    /**
     * obtiene al nodo padre del hijo que satisface el criterio pasado
     */
    @Override
    public TINodoGenerico<T> obtenerPadre(Comparable<T> criterio) {
        // reviso si alguno de mis hijos satisface
        for (TNodoGenerico<T> hijo : this.hijos.values()) {
            if (criterio.compareTo(hijo.dato) == 0) {
                return this;
            }
        }

        // si no, paso a mis hijos (recursivo)
        for (TNodoGenerico<T> hijo : this.hijos.values()) {
            TINodoGenerico<T> resultado = hijo.obtenerPadre(criterio);
            if (resultado != null) {
                return resultado;
            }
        }

        return null;

    }

    @Override
    public void preOrden(Consumer<TINodoGenerico<T>> consumidor) {
        consumidor.accept(this); // primero yo
        for (TNodoGenerico<T> hijo :hijos.values()) {
            hijo.preOrden(consumidor); // luego mis hijos
        }

    }

    @Override
    public void inOrden(Consumer<TINodoGenerico<T>> consumidor) {
        List<TNodoGenerico<T>> listaHijos = new ArrayList<>(hijos.values());

        // mientras no sea hoja, recorro el primer hijo (izq por oreden (lista))
        if (!listaHijos.isEmpty()) {
            listaHijos.get(0).inOrden(consumidor); // primer hijo
        }

        consumidor.accept(this); // yo

        for (int i = 1; i < listaHijos.size(); i++) {
            listaHijos.get(i).inOrden(consumidor); // resto de los hijos
        }
    }

    @Override
    public void postOrden(Consumer<TINodoGenerico<T>> consumidor) {
        // primero mis hijos
        for (TNodoGenerico<T> hijo : hijos.values()) {
            hijo.postOrden(consumidor);
        }
        // luego yo
        consumidor.accept(this); // luego yo
    }

    @Override
    /*
    * retorna la altura del nodo
    * Altura de un nodo: La cantidad de niveles desde este nodo hasta la hoja más lejana.
     */
    public int altura() {
        // si soy hoja
        if (this.hijos.size() == 0) {
            return 1;
        }

        int altura = 0;
        for (TNodoGenerico<T> hijo : this.hijos.values()) {
            int alturaHijo = hijo.altura();
            if (alturaHijo > altura) {
                altura = alturaHijo;
            }
        }

        return altura + 1;
    }

    /**
     * retorna la cantidad de hijos que tiene el nodo actual
     */
    @Override
    public int grado() {
        return this.hijos.size();
    }

    @Override
    public void vaciar() {
        hijos.clear();
    }

    /**
     * retorna los hijos directos de este nodo
     * solo expone los datos (las keys), no los nodos internos
     */
    @Override
    public List<T> obtenerHijos() {
        return new ArrayList<>(hijos.keySet());
    }
}
