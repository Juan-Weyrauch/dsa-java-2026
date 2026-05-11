package ucu.edu.aed.modelo;

import java.util.Objects;

/**
 * Representa una tarea operativa que recibe la nave para ser procesada.
 *
 * <p>Cada tarea posee exactamente los tres atributos que define el enunciado:
 * <ul>
 *   <li><b>id</b>: identificador único, clave de búsqueda en el historial AVL.</li>
 *   <li><b>descripcion</b>: texto libre descriptivo de la tarea.</li>
 *   <li><b>criticidad</b>: nivel de 1 (más crítico) a 4 (menos crítico). Las criticidades
 *       1 y 2 son consideradas <em>tareas críticas</em> y tienen prioridad de ejecución.</li>
 * </ul>
 *
 * <p>El orden de llegada (FIFO) lo preservan las propias colas internas del sistema por
 * orden de inserción; no se materializa como atributo de la tarea.</p>
 *
 * <p>La clase es inmutable e implementa {@link Comparable} ordenando por {@code id}, lo
 * que permite usarla directamente como dato del árbol AVL del historial (R3). Para
 * mantener el contrato sugerido por {@link Comparable}, {@code equals} y {@code hashCode}
 * también se basan en {@code id}.</p>
 */
public final class Tarea implements Comparable<Tarea> {

    private final int id;
    private final String descripcion;
    private final int criticidad;

    /**
     * Crea una tarea inmutable con los datos indicados.
     *
     * @param id            identificador único de la tarea
     * @param descripcion   texto descriptivo (no se valida)
     * @param criticidad    nivel entre 1 y 4 inclusive
     * @throws IllegalArgumentException si la criticidad está fuera del rango [1, 4]
     */
    public Tarea(int id, String descripcion, int criticidad) {
        if (criticidad < 1 || criticidad > 4) {
            throw new IllegalArgumentException("Criticidad debe estar entre 1 y 4");
        }
        this.id = id;
        this.descripcion = descripcion;
        this.criticidad = criticidad;
    }

    /**
     * @return el identificador único de la tarea.
     */
    public int getId() {
        return id;
    }

    /**
     * @return la descripción textual de la tarea.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @return el nivel de criticidad (1 a 4, siendo 1 el más crítico).
     */
    public int getCriticidad() {
        return criticidad;
    }

    /**
     * Indica si la tarea es de criticidad alta (1 o 2). Estas tareas computan
     * contra el límite de 10 críticas pendientes (R1) y se ejecutan antes que
     * las de criticidad 3 o 4 (R2).
     *
     * @return {@code true} si la criticidad es 1 o 2
     */
    public boolean esCritica() {
        return criticidad == 1 || criticidad == 2;
    }

    /**
     * Compara tareas por id para permitir su uso como clave del árbol AVL del historial.
     *
     * @param otra tarea contra la que comparar
     * @return valor negativo, cero o positivo según la relación de los ids
     */
    @Override
    public int compareTo(Tarea otra) {
        return Integer.compare(this.id, otra.id);
    }

    /**
     * Igualdad por id, consistente con {@link #compareTo(Tarea)}. Mantener este invariante
     * evita comportamientos inconsistentes si la tarea se usa en colecciones que combinan
     * orden e identidad (p.ej. {@code TreeMap}, {@code TreeSet}).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tarea)) return false;
        return this.id == ((Tarea) o).id;
    }

    /**
     * @return hashCode derivado del id, consistente con {@link #equals(Object)}.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * @return representación textual compacta para depuración.
     */
    @Override
    public String toString() {
        return "Tarea{id=" + id + ", crit=" + criticidad + ", desc='" + descripcion + "'}";
    }
}
