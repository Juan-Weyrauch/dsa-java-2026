package uy.edu.ucu.aed.tdas;

import java.util.LinkedList;

/**
 * Clase que representa un nodo de un árbol binario de búsqueda (ABB).
 * Permite almacenar un dato genérico y referencias a los hijos izquierdo y derecho.
 *
 * @param <T> Tipo de dato almacenado en el nodo.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ElementoAB<T> implements IElementoAB<T> {
    /**
     * Etiqueta o clave única que identifica al nodo.
     */
    private Comparable etiqueta;

    /**
     * Dato almacenado en el nodo.
     */
    private T datos;

    /**
     * Referencia al hijo izquierdo.
     */
    private IElementoAB<T> hijoIzq;

    /**
     * Referencia al hijo derecho.
     */
    private IElementoAB<T> hijoDer;

    /**
     * Constructor del nodo del árbol binario.
     *
     * @param etiqueta Clave o etiqueta del nodo.
     * @param datos Dato a almacenar en el nodo.
     */
    public ElementoAB(Comparable etiqueta, T datos) {
        this.etiqueta = etiqueta;
        this.datos = datos;
    }

    @Override
    public Comparable getEtiqueta() {
        return etiqueta;
    }

    @Override
    public IElementoAB<T> getHijoIzq() {
        return hijoIzq;
    }

    @Override
    public IElementoAB<T> getHijoDer() {
        return hijoDer;
    }

    @Override
    public void setHijoIzq(IElementoAB<T> elemento) {
        hijoIzq = elemento;
    }

    @Override
    public void setHijoDer(IElementoAB<T> elemento) {
        hijoDer = elemento;

    }

    @Override
    public IElementoAB<T> buscar(Comparable unaEtiqueta) {
        if (etiqueta.compareTo(unaEtiqueta) < 0) {
            return hijoDer == null ? null : hijoDer.buscar(unaEtiqueta);
        }
        if (etiqueta.compareTo(unaEtiqueta) > 0) {
            return hijoIzq == null ? null : hijoIzq.buscar(unaEtiqueta);
        }
        return this;
    }

    @Override
    public boolean insertar(IElementoAB<T> elemento) {
        // a la izquierda
        if (elemento.getEtiqueta().compareTo(etiqueta) < 0) {
            if (hijoIzq == null) {
                hijoIzq = elemento;
                return true;
            } else {
                return hijoIzq.insertar(elemento);
            }
        }
        if (elemento.getEtiqueta().compareTo(etiqueta) > 0) {
            if (hijoDer == null) {
                hijoDer = elemento;
                return true;
            } else {
                return hijoDer.insertar(elemento);
            }
        }
        return false;
    }

    @Override
    public void preOrden(LinkedList<T> unaLista) {
        unaLista.add(getDatos());

        if (hijoIzq != null) {
            hijoIzq.preOrden(unaLista);
        }
        if (hijoDer != null) {
            hijoDer.preOrden(unaLista);
        }
    }

    @Override
    public void inOrden(LinkedList<T> unaLista) {

        if (hijoIzq != null) {
            hijoIzq.inOrden(unaLista);
        }
        unaLista.add(getDatos());
        if (hijoDer != null) {
            hijoDer.inOrden(unaLista);
        }


    }

    @Override
    public void postOrden(LinkedList<T> unaLista) {
        if (hijoIzq != null) {
            hijoIzq.postOrden(unaLista);
        }
        if (hijoDer != null) {
            hijoDer.postOrden(unaLista);
        }

        unaLista.add(getDatos());
    }

    @Override
    public T getDatos() {
        return datos;
    }

    @Override
    public IElementoAB<T> eliminar(Comparable unaEtiqueta) {
        // a la izquierda
        if (unaEtiqueta.compareTo(etiqueta) < 0) {
            if (hijoIzq != null) {
                hijoIzq = hijoIzq.eliminar(unaEtiqueta);
            }
            return this;
        }
        if (unaEtiqueta.compareTo(etiqueta) > 0) {
            if (hijoDer != null) {
                hijoDer = hijoDer.eliminar(unaEtiqueta);
            }
            return this;
        }
        return quitarElNodo();
    }

    /**
     * Elimina el nodo actual del árbol y reestructura los enlaces para mantener las propiedades del ABB.
     *
     * @return El nodo que debe ocupar el lugar del nodo eliminado.
     */
    private IElementoAB<T> quitarElNodo() {
        if (hijoIzq == null) {
            return hijoDer;
        }

        if (hijoDer == null) {
            return hijoIzq;
        }

        IElementoAB<T> elHijo = hijoIzq;
        IElementoAB<T> elPadre = this;

        while (elHijo.getHijoDer() != null) {
            elPadre = elHijo;
            elHijo = elHijo.getHijoDer();
        }
        if (elPadre.getEtiqueta().compareTo(etiqueta) != 0) {
            elPadre.setHijoDer(elHijo.getHijoIzq());
            elHijo.setHijoIzq(hijoIzq);
        }
        elHijo.setHijoDer(hijoDer);
        return elHijo;
    }

    @Override
    public int obtenerTamaño() {
        return 1 + (hijoIzq == null ? 0 : hijoIzq.obtenerTamaño()) + (hijoDer == null ? 0 : hijoDer.obtenerTamaño());
    }
}
