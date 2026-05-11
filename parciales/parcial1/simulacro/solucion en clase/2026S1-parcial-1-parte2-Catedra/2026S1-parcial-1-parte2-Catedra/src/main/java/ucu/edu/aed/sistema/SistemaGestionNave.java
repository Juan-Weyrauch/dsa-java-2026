package ucu.edu.aed.sistema;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import ucu.edu.aed.modelo.CriterioPorId;
import ucu.edu.aed.modelo.Tarea;
import ucu.edu.aed.tda.AVLArbol;
import ucu.edu.aed.tda.TDAArbolBinario;

/**
 * Sistema de gestión de tareas operativas de una nave autónoma.
 *
 * <p>Implementa los cuatro requerimientos del enunciado:
 * <ul>
 *   <li><b>R1</b> Recepción con límites de capacidad y de tareas críticas, más cola de espera.</li>
 *   <li><b>R2</b> Procesamiento por mayor criticidad disponible y FIFO en empates.</li>
 *   <li><b>R3</b> Historial con búsqueda por id en O(log n) garantizado (árbol AVL).</li>
 *   <li><b>R4</b> Cancelación de tareas pendientes por id.</li>
 * </ul>
 *
 * <p>Estructuras internas:
 * <ul>
 *   <li><b>colaCriticas</b>: tareas pendientes de criticidad 1 y 2, ordenadas por
 *       criticidad ascendente y, dentro de cada nivel, por orden de inserción (FIFO).
 *       El primer elemento es siempre el de mayor prioridad.</li>
 *   <li><b>colaNormales</b>: tareas pendientes de criticidad 3 y 4 en FIFO estricto.</li>
 *   <li><b>colaEspera</b>: tareas que llegaron pero no pudieron ingresar por restricciones de
 *       capacidad o cuota de críticas; se reintentan al liberar cupo.</li>
 *   <li><b>bufferProcesadas</b> + <b>historial</b> (AVL): mientras no se alcanza el umbral de
 *       75 procesadas se acumulan en una lista; al cruzar el umbral se vuelcan al árbol AVL,
 *       que es el que se utiliza desde entonces para garantizar búsquedas O(log n).</li>
 * </ul>
 */
public class SistemaGestionNave implements SistemaGestion {

    /** Cantidad máxima total de tareas pendientes (R1). */
    public static final int MAX_PENDIENTES = 25;

    /** Cantidad máxima de tareas pendientes con criticidad 1 o 2 (R1). */
    public static final int MAX_CRITICAS = 10;

    /** Cantidad mínima de tareas procesadas a partir de la cual se exige el historial AVL (R3). */
    public static final int UMBRAL_HISTORIAL = 75;

    private final LinkedList<Tarea> colaCriticas = new LinkedList<>();
    private final Deque<Tarea> colaNormales = new ArrayDeque<>();
    private final Deque<Tarea> colaEspera = new ArrayDeque<>();

    private final LinkedList<Tarea> bufferProcesadas = new LinkedList<>();
    private TDAArbolBinario<Tarea> historial;
    private int totalProcesadas = 0;

    /**
     * @return cantidad total de tareas pendientes (críticas + normales).
     */
    public int cantidadPendientes() {
        return colaCriticas.size() + colaNormales.size();
    }

    /**
     * @return cantidad de tareas pendientes con criticidad 1 o 2.
     */
    public int cantidadCriticasPendientes() {
        return colaCriticas.size();
    }

    /**
     * @return cantidad de tareas en cola de espera (recibidas pero aún no admitidas).
     */
    public int cantidadEnEspera() {
        return colaEspera.size();
    }

    /**
     * @return cantidad acumulada de tareas que ya fueron procesadas.
     */
    public int cantidadProcesadas() {
        return totalProcesadas;
    }

    /**
     * Acceso de paquete al árbol AVL del historial. Pensado para tests que necesiten
     * inspeccionar la estructura interna (p.ej. validar que se haya materializado
     * tras alcanzar el umbral o medir la altura). El sistema sigue siendo el único
     * dueño de la inserción.
     *
     * @return el árbol AVL si el umbral R3 ya fue alcanzado, o {@code null} en caso contrario.
     */
    TDAArbolBinario<Tarea> getHistorial() {
        return historial;
    }

    /**
     * R1. Recibe una tarea para ser gestionada por el sistema.
     *
     * <p>Si la tarea cumple con las restricciones (cupo total y, en caso de ser crítica,
     * cuota de críticas) se ingresa inmediatamente al pool correspondiente. En caso
     * contrario queda almacenada en la cola de espera y se reintenta automáticamente
     * cada vez que se libera cupo (al procesar o al cancelar tareas).</p>
     *
     * @param tarea tarea a recibir; se ignora si es {@code null}
     * @return {@code true} si la tarea fue admitida inmediatamente,
     *         {@code false} si quedó en espera o si el parámetro es {@code null}
     */
    @Override
    public boolean recibirTarea(Tarea tarea) {
        if (tarea == null) return false;
        if (intentarIngresar(tarea)) {
            drenarColaEspera();
            return true;
        }
        colaEspera.addLast(tarea);
        return false;
    }

    /**
     * Intenta ingresar la tarea respetando las dos restricciones de R1:
     * cupo total ({@link #MAX_PENDIENTES}) y, si es crítica, cuota
     * ({@link #MAX_CRITICAS}). No reintenta — para eso existe {@link #drenarColaEspera()}.
     *
     * @param tarea tarea a ingresar
     * @return {@code true} si pudo ingresar
     */
    private boolean intentarIngresar(Tarea tarea) {
        if (cantidadPendientes() >= MAX_PENDIENTES) return false;
        if (tarea.esCritica()) {
            if (colaCriticas.size() >= MAX_CRITICAS) return false;
            insertarPorCriticidad(tarea);
            return true;
        }
        colaNormales.addLast(tarea);
        return true;
    }

    /**
     * Inserta la tarea crítica en la posición correcta para que la cabeza de la cola
     * sea siempre la próxima a ejecutar (R2): mayor criticidad primero y, dentro de
     * cada nivel, FIFO por orden de inserción.
     *
     * <p>Avanza con un {@link ListIterator} mientras la criticidad de los elementos
     * existentes sea menor o igual a la de la tarea entrante; al encontrar el primer
     * elemento con criticidad estrictamente mayor, retrocede una posición y usa
     * {@link ListIterator#add(Object)} para insertar antes de él. Esto resuelve el
     * problema en una sola pasada O(n) en lugar de las dos pasadas que requería
     * la combinación {@code iterator + add(idx, e)}.</p>
     *
     * @param tarea tarea crítica a insertar (criticidad 1 o 2)
     */
    private void insertarPorCriticidad(Tarea tarea) {
        ListIterator<Tarea> it = colaCriticas.listIterator();
        while (it.hasNext()) {
            Tarea actual = it.next();
            if (tarea.getCriticidad() < actual.getCriticidad()) {
                it.previous();
                break;
            }
        }
        it.add(tarea);
    }

    /**
     * Reintenta ingresar todas las tareas en espera respetando su orden original.
     * Si en una pasada se admitió al menos una se vuelve a intentar, ya que
     * las admisiones pueden destrabar otras tareas.
     */
    private void drenarColaEspera() {
        boolean cambio = true;
        while (cambio && !colaEspera.isEmpty()) {
            cambio = false;
            int n = colaEspera.size();
            for (int i = 0; i < n; i++) {
                Tarea t = colaEspera.pollFirst();
                if (intentarIngresar(t)) {
                    cambio = true;
                } else {
                    colaEspera.addLast(t);
                }
            }
        }
    }

    /**
     * R2. Selecciona y ejecuta la próxima tarea según las reglas del sistema.
     *
     * <p>Estrategia:
     * <ol>
     *   <li>Si hay tareas críticas, se ejecuta la cabeza de {@code colaCriticas},
     *       que por construcción tiene la mayor criticidad y, en empate, la
     *       de menor orden de llegada.</li>
     *   <li>En caso contrario se ejecuta la cabeza de {@code colaNormales} (FIFO).</li>
     * </ol>
     *
     * <p>La tarea ejecutada se registra en el historial (R3) y se intenta drenar
     * la cola de espera, ya que la liberación de cupo puede admitir tareas pendientes.</p>
     *
     * @return la tarea ejecutada o {@code null} si no había ninguna pendiente
     */
    @Override
    public Tarea procesarTarea() {
        Tarea siguiente = null;
        if (!colaCriticas.isEmpty()) {
            siguiente = colaCriticas.pollFirst();
        } else if (!colaNormales.isEmpty()) {
            siguiente = colaNormales.pollFirst();
        }
        if (siguiente == null) return null;

        registrarProcesada(siguiente);
        drenarColaEspera();
        return siguiente;
    }

    /**
     * Registra una tarea como procesada respetando la condición R3 sobre el historial.
     *
     * <p>Antes de alcanzar las {@link #UMBRAL_HISTORIAL} tareas procesadas se mantiene
     * una lista temporal. Al cruzar el umbral se construye el árbol AVL con todas las
     * procesadas hasta el momento y se descarta la lista. A partir de ahí cada nueva
     * tarea procesada se inserta directamente en el AVL.</p>
     *
     * @param t tarea a registrar
     */
    private void registrarProcesada(Tarea t) {
        totalProcesadas++;
        if (historial != null) {
            historial.insertar(t);
            return;
        }
        bufferProcesadas.add(t);
        if (totalProcesadas >= UMBRAL_HISTORIAL) {
            historial = new AVLArbol<>();
            for (Tarea procesada : bufferProcesadas) {
                historial.insertar(procesada);
            }
            bufferProcesadas.clear();
        }
    }

    /**
     * R3. Recupera una tarea ejecutada por su id.
     *
     * <p>Una vez activado el historial AVL la búsqueda se realiza en O(log n) garantizado.
     * Mientras no se haya alcanzado el umbral se realiza una búsqueda lineal en la
     * lista temporal — el enunciado solo exige la garantía logarítmica a partir
     * de las 75 tareas procesadas.</p>
     *
     * @param id identificador de la tarea procesada
     * @return la tarea encontrada o {@code null} si no existe
     */
    @Override
    public Tarea buscarTareaProcesada(int id) {
        if (historial != null) {
            return historial.buscar(new CriterioPorId(id));
        }
        for (Tarea t : bufferProcesadas) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    /**
     * R4. Cancela una tarea pendiente (no procesada) por su id.
     *
     * <p>Recorre las tres colas de tareas no procesadas (críticas, normales y en espera)
     * y elimina la primera coincidencia. El historial es inmutable desde la perspectiva
     * de cancelación: una tarea ya procesada no puede cancelarse. Tras una cancelación
     * exitosa se reintenta drenar la cola de espera, ya que pudo liberarse cupo.</p>
     *
     * @param id identificador de la tarea a cancelar
     * @return la tarea cancelada o {@code null} si no se encontró pendiente
     */
    @Override
    public Tarea cancelarTarea(int id) {
        Tarea cancelada = removerPorId(colaCriticas, id);
        if (cancelada == null) cancelada = removerPorId(colaNormales, id);
        if (cancelada == null) cancelada = removerPorId(colaEspera, id);
        if (cancelada != null) drenarColaEspera();
        return cancelada;
    }

    /**
     * Remueve la primera tarea de la cola dada cuyo id coincida con el indicado.
     *
     * @param cola cola sobre la que operar
     * @param id   identificador buscado
     * @return la tarea removida o {@code null} si no se encontró
     */
    private Tarea removerPorId(Deque<Tarea> cola, int id) {
        Iterator<Tarea> it = cola.iterator();
        while (it.hasNext()) {
            Tarea t = it.next();
            if (t.getId() == id) {
                it.remove();
                return t;
            }
        }
        return null;
    }
}
