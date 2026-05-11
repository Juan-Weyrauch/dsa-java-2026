package ucu.edu.aed.modelo;

/**
 * Criterio de búsqueda de {@link Tarea} por identificador.
 *
 * <p>El TDA Árbol Binario expone sus operaciones recibiendo un {@link Comparable} contra
 * el tipo almacenado. Como las búsquedas en el historial se realizan por id (R3) y no se
 * dispone de la tarea completa al consultar, esta clase permite construir un comparable
 * <em>sintético</em> que solo conoce el id buscado y compara contra el id de la tarea.</p>
 */
public class CriterioPorId implements Comparable<Tarea> {

    private final int id;

    /**
     * Construye un criterio que matchea contra tareas con el id indicado.
     *
     * @param id identificador a buscar
     */
    public CriterioPorId(int id) {
        this.id = id;
    }

    /**
     * Compara el id buscado contra el id de la tarea recibida. La semántica es la misma que
     * {@link Tarea#compareTo(Tarea)}, lo que asegura coherencia con el orden BST del árbol.
     *
     * @param otra tarea almacenada en el árbol
     * @return valor negativo, cero o positivo según la relación de los ids
     */
    @Override
    public int compareTo(Tarea otra) {
        return Integer.compare(this.id, otra.getId());
    }
}
