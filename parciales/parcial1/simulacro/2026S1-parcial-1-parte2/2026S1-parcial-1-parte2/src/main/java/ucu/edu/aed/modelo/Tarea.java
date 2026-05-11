package ucu.edu.aed.modelo;
import java.io.FileNotFoundException;
import java.util.List;

import ucu.edu.aed.utils.FileUtils;

/**
 * Representa una tarea operativa que recibe la nave para ser procesada.
 *
 * <p><strong>TODO (alumno):</strong> completar esta clase según el enunciado del parcial.
 *
 * <p>Recordá que el enunciado define los atributos de una tarea:
 * <ul>
 *   <li>ID único.</li>
 *   <li>Descripción.</li>
 *   <li>Nivel de criticidad (1 a 4, siendo 1 el más crítico).</li>
 * </ul>
 *
 * <p>Sugerencias:
 * <ul>
 *   <li>Para poder usar la tarea como dato del árbol AVL provisto, la clase debería
 *       implementar {@link Comparable Comparable&lt;Tarea&gt;}.</li>
 *   <li>Considerá si la clase debe ser inmutable (campos {@code final}, sin setters).</li>
 *   <li>Considerá qué validaciones aplican al construir una tarea.</li>
 * </ul>
 */
public class Tarea implements Comparable<Tarea> {
    // atributos.
    private final int id;
    private final String description;
    private final int criticality;

    // TODO: definir constructor(es) y validaciones.
    public Tarea(int inputId, String inputDescription, int inputCriticality) {
        if (inputCriticality < 1 || inputCriticality > 4){
            throw new IllegalArgumentException("Criticdiad fuera de rango (1-4)");
        }

        this.id = inputId;
        this.description = inputDescription;
        this.criticality = inputCriticality;
    }

    // TODO: definir getters relevantes.
    public int getId(){ return id; }
    public String getDescription() { return description; }
    public int getCriticality() { return criticality; }

    // TODO: implementar Comparable<Tarea> si corresponde.
    @Override
    public int compareTo(Tarea other){
        return Integer.compare(this.id, other.id);
    }

    // TODO: opcionalmente, sobrescribir equals / hashCode / toString.
    @Override
    public String toString(){
        return 
        "Tarea{" +
        "id=" + id + 
        ", criticidad=" + criticality + 
        ", desc='" + description + 
        "'}";
    }
}
