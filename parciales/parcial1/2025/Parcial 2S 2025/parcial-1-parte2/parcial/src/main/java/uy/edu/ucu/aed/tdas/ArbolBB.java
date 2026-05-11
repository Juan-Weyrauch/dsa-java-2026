package uy.edu.ucu.aed.tdas;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementación de un Árbol Binario de Búsqueda (ABB).
 * Permite insertar, buscar, eliminar y recorrer elementos de tipo genérico.
 *
 * @param <T> Tipo de dato almacenado en el árbol.
 */
@SuppressWarnings({"rawtypes"})
public class ArbolBB<T> implements IArbolBB<T> {
    /**
     * Raíz del árbol binario de búsqueda.
     */
    protected IElementoAB<T> raiz;

    /**
     * Devuelve la raíz del árbol.
     * @return Nodo raíz del árbol.
     */
    public IElementoAB<T> getRaiz() {
        return raiz;
    }

    @Override
    public boolean insertar(Comparable etiqueta, T unDato) {
        ElementoAB<T> elemento = new ElementoAB<T>(etiqueta, unDato);
        if (raiz == null) {
            raiz = elemento;
            return true;
        }
        return raiz.insertar(elemento);
    }

    @Override
    public T buscar(Comparable unaEtiqueta) {
        if (raiz == null)
            return null;

        IElementoAB<T> result = raiz.buscar(unaEtiqueta);
        return result == null ? null : result.getDatos();
    }

    @Override
    public void eliminar(Comparable unaEtiqueta) {
        if (raiz == null) return;
        raiz = raiz.eliminar(unaEtiqueta);
    }

    @Override
    public List<T> preOrden() {
        LinkedList<T> dato = new LinkedList<>();
        if (raiz != null) {
            raiz.preOrden(dato);
        }
        return dato;
    }

    @Override
    public List<T> inOrden() {
        if (raiz == null) return null;

        LinkedList<T> dato = new LinkedList<>();
        raiz.inOrden(dato);

        return dato;
    }

    @Override
    public List<T> postOrden() {
        if (raiz == null) return null;

        LinkedList<T> dato = new LinkedList<>();
        raiz.postOrden(dato);
        return dato;
    }

    @Override
    public boolean esVacio() {
        return raiz == null;
    }

    @Override
    public boolean vaciar() {
        if (raiz == null) return false;
        raiz = null;
        return true;
    }
}
