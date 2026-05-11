package uy.edu.ucu.aed.tdas;

/**
 * Implementación de un Árbol AVL.
 * <p>
 * Extiende la funcionalidad de un Árbol Binario de Búsqueda (ABB) asegurando el balanceo automático
 * después de cada operación de inserción o eliminación, manteniendo la diferencia de altura entre subárboles en no más de 1.
 * </p>
 *
 * <p><b>Advertencia sobre la nomenclatura de rotaciones:</b></p>
 * <p>
 * En esta implementación, los métodos de rotación (LL, RR, LR, RL) están nombrados según el tipo de desbalance
 * que corrigen, no según el sentido en que se realiza la rotación. Por ejemplo, una rotación LL corrige un desbalance
 * producido por un exceso de nodos en el subárbol izquierdo del hijo izquierdo, y así sucesivamente.
 * </p>
 *
 * <p>
 * Es importante notar que en otros textos o implementaciones, las rotaciones pueden nombrarse según la dirección
 * en la que se mueve el nodo desbalanceado (por ejemplo, "rotación a la derecha" o "rotación a la izquierda").
 * Por lo tanto, se recomienda al lector prestar especial atención a la convención utilizada en cada contexto para evitar confusiones.
 * </p>
 *
 * @param <T> Tipo de dato almacenado en el árbol.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ArbolAVL<T> extends ArbolBB<T> {
    @Override
    public boolean insertar(Comparable etiqueta, T unDato) {
        IElementoAB<T> nuevoNodo = crearNodo(etiqueta, unDato);
        raiz = insertar(raiz, nuevoNodo);
        return true;
    }

    protected IElementoAB<T> crearNodo(Comparable etiqueta, T unDato) {
        return new ElementoAB<>(etiqueta, unDato);
    }

    @Override
    public void eliminar(Comparable unaEtiqueta) {
        raiz = eliminar(raiz, unaEtiqueta);
    }

    /**
     * Elimina un nodo con la etiqueta dada del árbol AVL y realiza las rotaciones necesarias para mantener el balanceo.
     * 
     * El método busca recursivamente el nodo a eliminar. Una vez encontrado, lo elimina utilizando la lógica del ABB.
     * Luego, al retroceder en la recursión, verifica el factor de balanceo de cada subárbol afectado y realiza las rotaciones
     * necesarias (LL, LR, RR, RL) para restaurar la propiedad de balanceo del AVL.
     *
     * @param nodo Nodo actual del árbol.
     * @param unaEtiqueta Etiqueta del nodo a eliminar.
     * @return La nueva raíz del subárbol tras la eliminación y posible rotación.
     */
    private IElementoAB<T> eliminar(IElementoAB<T> nodo, Comparable unaEtiqueta) {
        // Caso base: el árbol esta vacío (se invocó el método con una raíz nula) o se llegó a un nodo hoja sin encontrar un nodo con la etiqueta indicada para eliminar.
        if (nodo == null) {
            return null;
        }

        // Se busca recursivamente el nodo a eliminar
        IElementoAB<T> base = nodo;
        if (unaEtiqueta.compareTo(nodo.getEtiqueta()) < 0) {
            nodo.setHijoIzq(eliminar(nodo.getHijoIzq(), unaEtiqueta));
        } else if (unaEtiqueta.compareTo(nodo.getEtiqueta()) > 0) {
            nodo.setHijoDer(eliminar(nodo.getHijoDer(), unaEtiqueta));
        } else {
            // Nodo encontrado: se elimina y se reestructura el subárbol
            base = nodo.eliminar(unaEtiqueta);
        }

        // Si el subárbol resultante es nulo, se retorna
        if (base == null) return base;

        // Se calcula el factor de balanceo para determinar si es necesario rotar
        int balance = obtenerBalanceo(base);

        // Caso LL: desbalanceo hacia la izquierda
        if (balance > 1 && obtenerBalanceo(base.getHijoIzq()) >= 0) {
            return rotacionLL(base);
        }
        // Caso LR: desbalanceo hacia la izquierda con subárbol derecho más alto
        if (balance > 1 && obtenerBalanceo(base.getHijoIzq()) < 0) {
            base.setHijoIzq(rotacionLL(base.getHijoIzq()));
            return rotacionRR(base);
        }

        // Caso RR: desbalanceo hacia la derecha
        if (balance < -1 && obtenerBalanceo(base.getHijoDer()) <= 0) {
            return rotacionRR(base);
        }

        // Caso RL: desbalanceo hacia la derecha con subárbol izquierdo más alto
        if (balance < -1 && obtenerBalanceo(base.getHijoDer()) > 0) {
            base.setHijoDer(rotacionRR(base.getHijoDer()));
            return rotacionLR(base);
        }
        // Si no se requiere rotación, se retorna el subárbol tal como quedó
        return base;
    }

    /**
     * Inserta un nuevo nodo en el árbol AVL y realiza las rotaciones necesarias para mantener el balanceo.
     *
     * @param nodo Nodo actual del árbol.
     * @param elemento Nodo a insertar.
     * @return La nueva raíz del subárbol tras la inserción y posible rotación.
     */
    private IElementoAB<T> insertar(IElementoAB<T> nodo, IElementoAB<T> elemento) {
        if (nodo == null) {
            return elemento;
        }

        if (elemento.getEtiqueta().compareTo(nodo.getEtiqueta()) < 0) {
            nodo.setHijoIzq(insertar(nodo.getHijoIzq(), elemento));
        } else if (elemento.getEtiqueta().compareTo(nodo.getEtiqueta()) > 0) {
            nodo.setHijoDer(insertar(nodo.getHijoDer(), elemento));
        } else {
            return nodo;
        }

        int balance = obtenerBalanceo(nodo);
        if (balance > 1 && elemento.getEtiqueta().compareTo(nodo.getHijoIzq().getEtiqueta()) < 0) {
            // rotacion LL
            return rotacionLL(nodo);
        }
        if (balance > 1 && elemento.getEtiqueta().compareTo(nodo.getHijoIzq().getEtiqueta()) > 0) {
            // rotacion LR
            return rotacionLR(nodo);
        }

        if (balance < -1 && elemento.getEtiqueta().compareTo(nodo.getHijoDer().getEtiqueta()) > 0) {
            // rotacion RR
            return rotacionRR(nodo);
        }

        if (balance < -1 && elemento.getEtiqueta().compareTo(nodo.getHijoDer().getEtiqueta()) < 0) {
            // rotacion RL
            return rotacionRL(nodo);
        }

        return nodo;

    }


    /**
     * Realiza una rotación simple a la izquierda (LL) para balancear el árbol.
     *
     * @param k2 Nodo desbalanceado.
     * @return Nueva raíz del subárbol tras la rotación.
     */
    private IElementoAB<T> rotacionLL(IElementoAB<T> k2) {
        IElementoAB<T> k1 = k2.getHijoIzq();
        k2.setHijoIzq(k1.getHijoDer());
        k1.setHijoDer(k2);
        return k1;
    }

    /**
     * Realiza una rotación simple a la derecha (RR) para balancear el árbol.
     *
     * @param k1 Nodo desbalanceado.
     * @return Nueva raíz del subárbol tras la rotación.
     */
    private IElementoAB<T> rotacionRR(IElementoAB<T> k1) {
        IElementoAB<T> k2 = k1.getHijoDer();
        k1.setHijoDer(k2.getHijoIzq());
        k2.setHijoIzq(k1);
        return k2;
    }

    /**
     * Realiza una rotación doble izquierda-derecha (LR) para balancear el árbol.
     *
     * @param k3 Nodo desbalanceado.
     * @return Nueva raíz del subárbol tras la rotación.
     */
    private IElementoAB<T> rotacionLR(IElementoAB<T> k3) {
        k3.setHijoIzq(rotacionRR(k3.getHijoIzq()));
        return rotacionLL(k3);
    }

    /**
     * Realiza una rotación doble derecha-izquierda (RL) para balancear el árbol.
     *
     * @param k1 Nodo desbalanceado.
     * @return Nueva raíz del subárbol tras la rotación.
     */
    private IElementoAB<T> rotacionRL(IElementoAB<T> k1) {
        k1.setHijoDer(rotacionLL(k1.getHijoDer()));
        return rotacionRR(k1);
    }

    /**
     * Calcula la altura de un nodo en el árbol.
     *
     * @param elemento Nodo del cual se desea conocer la altura.
     * @return Altura del nodo, -1 si es nulo.
     */
    private int obtenerAltura(IElementoAB<T> elemento) {
        if (elemento == null) return -1;
        return Math.max(obtenerAltura(elemento.getHijoIzq()), obtenerAltura(elemento.getHijoDer())) + 1;
    }

    /**
     * Calcula el factor de balanceo de un nodo.
     *
     * @param elemento Nodo del cual se desea conocer el balanceo.
     * @return Diferencia de alturas entre el subárbol izquierdo y derecho.
     */
    private int obtenerBalanceo(IElementoAB<T> elemento) {
        if (elemento == null) return 0;
        // si da un valor positivo, fue por la izq, en otro caso va por la der
        return obtenerAltura(elemento.getHijoIzq()) - obtenerAltura(elemento.getHijoDer());
    }
}
