package uy.edu.ucu.aed.modelo;

/**
 * Representa una estación de aterrizaje en la red del dron.
 * La clave de ordenamiento en el AVL es el propio id (señal).
 */
public class Estacion implements Comparable<Estacion> {

    public enum CondicionOperativa {
        OPTIMO, SUBOPTIMO, CRITICO
    }

    private final int id;

    public Estacion(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public CondicionOperativa getCondicion() {
        if (id <= 10 || id > 100) {
            return CondicionOperativa.CRITICO;
        }
        if (id >= 60 && id <= 80) {
            return CondicionOperativa.OPTIMO;
        }
        return CondicionOperativa.SUBOPTIMO;
    }

    @Override
    public int compareTo(Estacion otra) {
        return Integer.compare(this.id, otra.id);
    }

    @Override
    public String toString() {
        return "Estacion{id=" + id + ", condicion=" + getCondicion() + "}";
    }
}
