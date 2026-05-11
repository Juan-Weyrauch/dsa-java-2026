package uy.edu.ucu.aed.tdas;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación de una lista enlazada simple.
 * Permite almacenar elementos de tipo genérico y realizar operaciones básicas como insertar, buscar, eliminar e imprimir.
 *
 * @param <T> Tipo de dato almacenado en la lista.
 */
@SuppressWarnings("rawtypes")
public class Lista<T> implements ILista<T> {

    /**
     * Clase interna que representa un nodo de la lista enlazada.
     *
     * @param <X> Tipo de dato almacenado en el nodo.
     */
    protected class Nodo<X> {

        /**
         * Clave o etiqueta única que identifica al nodo.
         */
        public final Comparable etiqueta;

        /**
         * Dato almacenado en el nodo.
         */
        public X dato;

        /**
         * Referencia al siguiente nodo en la lista.
         */
        public Nodo<X> siguiente = null;

        /**
         * Constructor del nodo.
         *
         * @param etiqueta Clave o etiqueta del nodo.
         * @param dato Dato a almacenar en el nodo.
         */
        public Nodo(Comparable etiqueta, X dato) {
            this.etiqueta = etiqueta;
            this.dato = dato;
        }
    }

    /**
     * Referencia al primer nodo de la lista.
     */
    protected Nodo<T> primero;

    /**
     * Constructor de la lista. Inicializa la lista vacía.
     */
    public Lista() {
        primero = null;
    }

    @Override
    public boolean insertar(T dato, Comparable clave) {
        Nodo<T> nuevo = new Nodo<>(clave, dato);
        if (primero == null) {
            primero = nuevo;
        } else {
            Nodo<T> actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        return true;
    }

    @Override
    public T buscar(Comparable clave) {
        Nodo<T> actual = primero;
        while (actual != null) {
            if (actual.etiqueta.equals(clave)) {
                return actual.dato;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    @Override
    public boolean eliminar(Comparable clave) {
        if (primero == null) {
            return false;
        }
        if (primero.etiqueta.equals(clave)) {
            primero = primero.siguiente;
            return true;
        }
        Nodo<T> actual = primero;
        while (actual.siguiente != null) {
            if (actual.siguiente.etiqueta.equals(clave)) {
                actual.siguiente = actual.siguiente.siguiente;
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    @Override
    public String imprimir() {
        return imprimir(" ");
    }

    @Override
    public String imprimir(String separador) {
        StringBuilder sb = new StringBuilder();
        Nodo<T> actual = primero;
        while (actual != null) {
            sb.append(actual.etiqueta);
            if (actual.siguiente != null) {
                sb.append(separador);
            }
            actual = actual.siguiente;
        }
        return sb.toString();
    }

    @Override
    public int cantElementos() {
        int count = 0;
        Nodo<T> actual = primero;
        while (actual != null) {
            count++;
            actual = actual.siguiente;
        }
        return count;
    }

    @Override
    public boolean esVacia() {
        return primero == null;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListaIterator();
    }

    /**
     * Clase interna que implementa la interfaz Iterator para recorrer la lista.
     * 
     * Esto permtite recorrer la lista de forma sencilla utilizando un bucle for-each.
     */
    private class ListaIterator implements Iterator<T> {
        private Nodo<T> actual = primero;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (actual == null) {
                throw new NoSuchElementException();
            }
            T dato = actual.dato;
            actual = actual.siguiente;
            return dato;
        }
    }
}
