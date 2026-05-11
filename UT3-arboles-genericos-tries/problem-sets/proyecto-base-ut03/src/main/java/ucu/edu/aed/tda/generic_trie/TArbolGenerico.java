package ucu.edu.aed.tda.generic_trie;

import java.util.function.Consumer;
import java.lang.Comparable;

import ucu.edu.aed.tda.generic_trie.TIArbolGenerico;

public class TArbolGenerico<T extends Comparable<T>> implements TIArbolGenerico<T> {
    // Atributos
    private TNodoGenerico<T> raiz;

    public TArbolGenerico() {
        this.raiz = null;
    }

    @Override
    public boolean agregarHijo(Comparable<T> padre, T hijo) {
        if (isRootEmpty())
            return false;

        // busco el nodo que satisface el criterio
        TINodoGenerico<T> nodoPadre = raiz.buscar(padre);
        if (nodoPadre == null)
            return false;

        // le pido al nodo que se agregue a sí mismo como padre
        // o sea, agregarHijo donde padre == su propio dato
        return raiz.agregarHijo(nodoPadre.getDato(), hijo);
    }

    @Override
    public void eliminar(Comparable<T> criterio) {
        if (isRootEmpty())
            return;

        // caso: hay que eliminar la raiz
        if (criterio.compareTo(raiz.getDato()) == 0) {
            raiz = null; // el arbol queda vacío
            return;
        }

        // si no, delego al nodo
        raiz.eliminar(criterio);
    }

    @Override
    public T obtenerPadre(Comparable<T> criterio) {
        if (isRootEmpty())
            return null;

        // (si es la raiz...) la raiz no tiene padre:
        if (criterio.compareTo(raiz.getDato()) == 0) {
            return null;
        }

        // si no, busco recursivamente:
        TINodoGenerico<T> nodoPadre = raiz.obtenerPadre(criterio);
        if (nodoPadre == null)
            return null;

        // devuelvo el DATO del padre, no el nodo.
        // porque TIArbolGenerico trabaja con T, no con nodos
        return nodoPadre.getDato();

    }

    @Override
    public T buscar(Comparable<T> criterio) {
        if (isRootEmpty())
            return null;

        TINodoGenerico<T> resultado = raiz.buscar(criterio);
        if (resultado == null)
            return null;

        // igual que obtenerPadre: el árbol expone datos, no nodos
        return resultado.getDato();
    }

    // Los recorridos del árbol reciben Consumer<T> (solo datos)
    // pero los del nodo reciben Consumer<TINodoGenerico<T>> (nodos completos)
    // entonces adaptamos: recibimos el consumidor de T y lo envolvemos
    // para que el nodo nos pase su dato al consumidor original

    @Override
    public void preOrden(Consumer<T> consumidor) {
        if (isRootEmpty())
            return;

        // nodo -> consumidor.accept(nodo.getDato()) es el Consumer<TINodoGenerico<T>>
        raiz.preOrden(nodo -> consumidor.accept(nodo.getDato()));

    }

    @Override
    public void inOrden(Consumer<T> consumidor) {
        if (isRootEmpty())
            return;

        raiz.inOrden(nodo -> consumidor.accept(nodo.getDato()));
    }

    @Override
    public void postOrden(Consumer<T> consumidor) {
        if (isRootEmpty())
            return;
        raiz.postOrden(nodo -> consumidor.accept(nodo.getDato()));

    }

    @Override
    public void vaciar() {
        raiz = null; // el generic trie se encarga del resto
    }

    @Override
    public int grado(Comparable<T> criterio) {
        if (isRootEmpty())
            return -1;

        TINodoGenerico<T> nodo = raiz.buscar(criterio);
        if (nodo == null)
            return 0;

        return nodo.grado();
    }

    @Override
    public int altura(Comparable<T> criterio) {
        if (isRootEmpty())
            return 0;

        TINodoGenerico<T> nodo = raiz.buscar(criterio);
        if (nodo == null)
            return 0;

        return nodo.altura();
    }

    /*
     * Sé que es innecesario pero quería hacerlo.
     * No afecta en nada, solo a la escritura. Siento que facilita la lectura del
     * código.
     */
    private boolean isRootEmpty() {
        return (raiz == null) ? true : false;
    }
}