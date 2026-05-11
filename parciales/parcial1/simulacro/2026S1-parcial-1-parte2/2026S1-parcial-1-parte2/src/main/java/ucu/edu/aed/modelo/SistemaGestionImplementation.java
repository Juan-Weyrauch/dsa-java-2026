package ucu.edu.aed.modelo;

import ucu.edu.aed.sistema.SistemaGestion;
import ucu.edu.aed.modelo.Tarea;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import ucu.edu.aed.tda.AVLArbol;

public class SistemaGestionImplementation implements SistemaGestion {
    // Tareas pendientes: una cola por nivel de criticidad
    // Justificación: FIFO por nivel + prioridad estricta entre niveles -> 4 colas
    // O(1) en procesarTarea

    private final Queue<Tarea> pendientesCrit1 = new LinkedList<>();
    private final Queue<Tarea> pendientesCrit2 = new LinkedList<>();
    private final Queue<Tarea> pendientesCrit3 = new LinkedList<>();
    private final Queue<Tarea> pendientesCrit4 = new LinkedList<>();

    // lsita con todas las colas
    private final List<Queue<Tarea>> todasLasColas = List.of(
            pendientesCrit1, pendientesCrit2, pendientesCrit3, pendientesCrit4);

    // Cola de espera
    private final Queue<Tarea> espera = new LinkedList<>();

    // Historial (R3): AVL para búsqueda logarítmica garantizada
    private final AVLArbol<Tarea> historial = new AVLArbol<>();

    // Max(s)
    private static final int MAX_PENDIENTES = 25;
    private static final int MAX_CRITICAS = 10; // criticidad 1 y 2

    // Helpers
    private int totalPendientes() {
        return pendientesCrit1.size() + pendientesCrit2.size() +
                pendientesCrit3.size() + pendientesCrit4.size();
    }

    private int totalCriticas() {
        return pendientesCrit1.size() + pendientesCrit2.size();
    }

    private boolean esCritica(Tarea t) {
        if (t.getCriticality() == 1 || t.getCriticality() == 2) {
            return true;
        }
        return false;
    }

    private boolean puedeEntrar(Tarea tarea) {
        boolean puedeEntrar = false;

        if (totalPendientes() < MAX_PENDIENTES &&
                (!esCritica(tarea) || totalCriticas() < MAX_CRITICAS)) {
            puedeEntrar = true;
        }

        return puedeEntrar;

    }

    private Queue<Tarea> colaParaNivel(int nivel) {
        switch (nivel) {
            case 1:
                return pendientesCrit1;
            case 2:
                return pendientesCrit2;
            case 3:
                return pendientesCrit3;
            case 4:
                return pendientesCrit4;
            default:
                throw new IllegalArgumentException("Nivel de criticidad inválido: " + nivel);
        }
    }

    // R1: Recibir tareas de forma continua.
    public boolean recibirTarea(Tarea tarea) {
        if (tarea == null)
            return false;

        if (puedeEntrar(tarea)) {
            colaParaNivel(tarea.getCriticality()).offer(tarea); // offer = add pero no tira exception (ret: bool)
            return true;
        } else {
            espera.offer(tarea);
            return false;
        }

    }

    // Se llama esto después de cada procesarTarea o cancelarTarea para mover tareas
    // de espera
    private void intentarAdmitirEspera() {
        while (!espera.isEmpty()) {
            Tarea t = espera.peek();
            if (puedeEntrar(t)) {
                espera.poll();
                colaParaNivel(t.getCriticality()).offer(t);
            } else {
                break;
            }
        }
    }

    @Override
    public Tarea procesarTarea() {
        Tarea procesada = null;

        if (!pendientesCrit1.isEmpty()) {
            procesada = pendientesCrit1.poll();
        } else if (!pendientesCrit2.isEmpty()) {
            procesada = pendientesCrit2.poll();
        } else if (!pendientesCrit3.isEmpty()) {
            procesada = pendientesCrit3.poll();
        } else if (!pendientesCrit4.isEmpty()) {
            procesada = pendientesCrit4.poll();
        }

        if (procesada != null) {
            historial.insertar(procesada); // R3
            intentarAdmitirEspera();
        }

        return procesada;
    }

    public Tarea buscarTareaProcesada(int id) {
        return historial.buscar(new CriterioPorId(id));
    }

    @Override
    public Tarea cancelarTarea(int id) {
        for (Queue<Tarea> cola : todasLasColas) {
            for (Tarea t : cola) {
                if (t.getId() == id) {
                    cola.remove(t);
                    intentarAdmitirEspera();
                    return t;
                }
            }
        }
        return null;
    }
}
