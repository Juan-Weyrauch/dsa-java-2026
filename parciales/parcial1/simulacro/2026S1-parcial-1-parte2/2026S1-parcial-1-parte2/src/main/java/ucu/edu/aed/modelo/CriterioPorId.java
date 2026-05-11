package ucu.edu.aed.modelo;

public class CriterioPorId implements Comparable<Tarea> {
    private final int id;

    public CriterioPorId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Tarea tarea) {
        if (tarea == null){
            return 1; // si el nodo es null, el id buscado es "mayor"
        }

        return Integer.compare(this.id, tarea.getId());
    }
}
