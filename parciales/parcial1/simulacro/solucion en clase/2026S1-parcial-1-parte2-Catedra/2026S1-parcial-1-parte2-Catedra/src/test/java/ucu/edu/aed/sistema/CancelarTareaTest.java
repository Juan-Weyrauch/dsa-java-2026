package ucu.edu.aed.sistema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import ucu.edu.aed.modelo.Tarea;

/**
 * Tests del requerimiento <b>R4 - Cancelación de tareas</b>.
 *
 * <p>Verifican que:
 * <ul>
 *   <li>Se puede cancelar una tarea pendiente, ya sea crítica o normal.</li>
 *   <li>Se puede cancelar una tarea que está en cola de espera.</li>
 *   <li>No es posible cancelar tareas ya procesadas (siguen visibles en el historial).</li>
 *   <li>La cancelación libera cupo y permite que tareas en espera reingresen.</li>
 * </ul>
 */
public class CancelarTareaTest {

    private SistemaGestionNave sistema;

    @Before
    public void setUp() {
        sistema = new SistemaGestionNave();
    }

    private Tarea nueva(int id, int crit) {
        return new Tarea(id, "t" + id, crit);
    }

    /**
     * <b>cancelarTareaInexistenteRetornaNull</b>
     *
     * <p>Si se solicita cancelar un id que no existe en ninguna cola, la operación
     * debe retornar {@code null} sin modificar el estado del sistema.</p>
     */
    @Test
    public void cancelarTareaInexistenteRetornaNull() {
        sistema.recibirTarea(nueva(1, 4));
        assertNull(sistema.cancelarTarea(999));
        assertEquals(1, sistema.cantidadPendientes());
    }

    /**
     * <b>cancelarTareaCriticaPendiente_laRetiraDeLaCola</b>
     *
     * <p>Confirma que la cancelación recorre la cola de críticas: tras cancelar una de
     * dos tareas críticas pendientes, queda solo la otra y su id es el que recupera el
     * próximo {@code procesarTarea}.</p>
     */
    @Test
    public void cancelarTareaCriticaPendiente_laRetiraDeLaCola() {
        sistema.recibirTarea(nueva(1, 1));
        sistema.recibirTarea(nueva(2, 1));

        Tarea cancelada = sistema.cancelarTarea(1);
        assertNotNull(cancelada);
        assertEquals(1, cancelada.getId());

        assertEquals(1, sistema.cantidadPendientes());
        assertEquals(2, sistema.procesarTarea().getId());
    }

    /**
     * <b>cancelarTareaNormalPendiente_laRetiraDeLaCola</b>
     *
     * <p>Análogo al caso crítico para tareas de criticidad 3 o 4. Verifica que la
     * cancelación funciona también sobre la cola FIFO normal sin afectar el orden
     * de las restantes.</p>
     */
    @Test
    public void cancelarTareaNormalPendiente_laRetiraDeLaCola() {
        sistema.recibirTarea(nueva(10, 3));
        sistema.recibirTarea(nueva(20, 3));
        sistema.recibirTarea(nueva(30, 3));

        assertNotNull(sistema.cancelarTarea(20));
        assertEquals(10, sistema.procesarTarea().getId());
        assertEquals(30, sistema.procesarTarea().getId());
    }

    /**
     * <b>cancelarTareaEnEspera_laQuitaDeLaColaDeEspera</b>
     *
     * <p>R4 indica que pueden cancelarse tareas pendientes; el sistema interpreta como
     * pendientes también las que están en espera porque aún no fueron admitidas. El test
     * satura la cuota de críticas, deja una crítica adicional en espera y la cancela:
     * debe desaparecer de la cola de espera sin afectar a las admitidas.</p>
     */
    @Test
    public void cancelarTareaEnEspera_laQuitaDeLaColaDeEspera() {
        for (int i = 1; i <= 10; i++) sistema.recibirTarea(nueva(i, 1));
        sistema.recibirTarea(nueva(99, 1));
        assertEquals(1, sistema.cantidadEnEspera());

        Tarea cancelada = sistema.cancelarTarea(99);
        assertNotNull(cancelada);
        assertEquals(99, cancelada.getId());
        assertEquals(0, sistema.cantidadEnEspera());
        assertEquals(10, sistema.cantidadCriticasPendientes());
    }

    /**
     * <b>cancelarTareaYaProcesadaNoEsPosible_yPermaneceEnHistorial</b>
     *
     * <p>R4 prohíbe cancelar tareas ya procesadas. El test ejecuta una tarea, intenta
     * cancelarla y verifica que la operación retorna {@code null}; además confirma que
     * la tarea sigue siendo recuperable desde el historial vía {@code buscarTareaProcesada}.</p>
     */
    @Test
    public void cancelarTareaYaProcesadaNoEsPosible_yPermaneceEnHistorial() {
        sistema.recibirTarea(nueva(7, 2));
        sistema.procesarTarea();

        assertNull(sistema.cancelarTarea(7));
        assertNotNull(sistema.buscarTareaProcesada(7));
    }

    /**
     * <b>cancelarLiberaCupo_yLaTareaEnEsperaIngresa</b>
     *
     * <p>Verifica el efecto secundario clave de la cancelación: liberar cupo activa el
     * drenaje de la cola de espera. Tras saturar las 10 críticas, dejar una en espera
     * y cancelar una crítica admitida, la tarea en espera debe entrar automáticamente
     * sin nueva intervención del cliente.</p>
     */
    @Test
    public void cancelarLiberaCupo_yLaTareaEnEsperaIngresa() {
        for (int i = 1; i <= 10; i++) sistema.recibirTarea(nueva(i, 1));
        sistema.recibirTarea(nueva(99, 1));
        assertEquals(1, sistema.cantidadEnEspera());

        sistema.cancelarTarea(5);

        assertEquals(0, sistema.cantidadEnEspera());
        assertEquals(10, sistema.cantidadCriticasPendientes());
    }
}
