package ucu.edu.aed.sistema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ucu.edu.aed.modelo.Tarea;

/**
 * Tests del requerimiento <b>R1 - Recepción de tareas</b>.
 *
 * <p>Verifican las cuatro reglas de admisión:
 * <ul>
 *   <li>Tope global de 25 tareas pendientes.</li>
 *   <li>Tope de 10 tareas críticas (criticidad 1 o 2) pendientes.</li>
 *   <li>Las tareas que no entran quedan en la cola de espera y se reintentan al liberar cupo.</li>
 *   <li>Una tarea normal nunca se descarta: si hay cupo total entra inmediatamente.</li>
 * </ul>
 */
public class RecibirTareaTest {

    private SistemaGestionNave sistema;

    /**
     * Crea un sistema limpio antes de cada test.
     */
    @Before
    public void setUp() {
        sistema = new SistemaGestionNave();
    }

    private Tarea nueva(int id, int crit) {
        return new Tarea(id, "t" + id, crit);
    }

    /**
     * <b>recibeTareaNormalCuandoSistemaVacio_quedaPendiente</b>
     *
     * <p>Caso feliz: con el sistema vacío, una tarea normal (criticidad 4) debe ser admitida
     * inmediatamente, incrementando el contador de pendientes y sin generar entradas en la
     * cola de espera. Verifica el camino más simple del flujo R1.</p>
     */
    @Test
    public void recibeTareaNormalCuandoSistemaVacio_quedaPendiente() {
        assertTrue(sistema.recibirTarea(nueva(1, 4)));
        assertEquals(1, sistema.cantidadPendientes());
        assertEquals(0, sistema.cantidadEnEspera());
    }

    /**
     * <b>recibirNullEsIgnorado</b>
     *
     * <p>Robustez: pasar {@code null} a {@code recibirTarea} debe retornar {@code false}
     * y no alterar el estado del sistema (no se crean pendientes ni entradas en espera).</p>
     */
    @Test
    public void recibirNullEsIgnorado() {
        assertFalse(sistema.recibirTarea(null));
        assertEquals(0, sistema.cantidadPendientes());
        assertEquals(0, sistema.cantidadEnEspera());
    }

    /**
     * <b>al26avaTareaSeDesbordaCupoTotal_vaACoolaEspera</b>
     *
     * <p>Verifica el tope global de pendientes (25). Tras llenar el sistema con 25 tareas
     * normales, la tarea número 26 no es admitida inmediatamente y queda registrada en
     * la cola de espera para ser reintentada al liberar cupo.</p>
     */
    @Test
    public void al26avaTareaSeDesbordaCupoTotal_vaACoolaEspera() {
        for (int i = 1; i <= 25; i++) {
            assertTrue("La tarea " + i + " debió admitirse", sistema.recibirTarea(nueva(i, 4)));
        }
        assertFalse(sistema.recibirTarea(nueva(26, 4)));
        assertEquals(25, sistema.cantidadPendientes());
        assertEquals(1, sistema.cantidadEnEspera());
    }

    /**
     * <b>onceavaTareaCriticaQuedaEnEspera_aunqueHayaCupoTotal</b>
     *
     * <p>Verifica la cuota independiente de tareas críticas (máx. 10). Tras admitir 10
     * tareas críticas (criticidad 1), una nueva tarea crítica debe quedar en espera aun
     * cuando todavía hay cupo total disponible (solo se han usado 10 de 25 lugares).</p>
     */
    @Test
    public void onceavaTareaCriticaQuedaEnEspera_aunqueHayaCupoTotal() {
        for (int i = 1; i <= 10; i++) {
            assertTrue(sistema.recibirTarea(nueva(i, 1)));
        }
        assertFalse(sistema.recibirTarea(nueva(11, 2)));
        assertEquals(10, sistema.cantidadCriticasPendientes());
        assertEquals(10, sistema.cantidadPendientes());
        assertEquals(1, sistema.cantidadEnEspera());
    }

    /**
     * <b>tareaNormalEntraAunqueColaCriticasEsteLlena</b>
     *
     * <p>La cuota de críticas (10) no debe afectar a las tareas normales (criticidad 3 y 4):
     * con 10 críticas en pendientes y aún cupo total disponible, una tarea normal nueva
     * debe ingresar inmediatamente sin pasar por la cola de espera.</p>
     */
    @Test
    public void tareaNormalEntraAunqueColaCriticasEsteLlena() {
        for (int i = 1; i <= 10; i++) {
            sistema.recibirTarea(nueva(i, 1));
        }
        assertTrue(sistema.recibirTarea(nueva(11, 3)));
        assertEquals(11, sistema.cantidadPendientes());
        assertEquals(0, sistema.cantidadEnEspera());
    }

    /**
     * <b>colaEsperaSeDrenaCuandoSeProcesa_yLasTareasReanudanIngreso</b>
     *
     * <p>Garantiza la regla de "queda en espera hasta que pueda ser ingresada". Tras saturar
     * la cuota de críticas y enviar una crítica adicional (que queda en espera), procesar
     * una tarea libera cupo y la tarea en espera debe ingresar automáticamente sin que el
     * cliente la vuelva a enviar.</p>
     */
    @Test
    public void colaEsperaSeDrenaCuandoSeProcesa_yLasTareasReanudanIngreso() {
        for (int i = 1; i <= 10; i++) sistema.recibirTarea(nueva(i, 1));
        sistema.recibirTarea(nueva(11, 1));
        assertEquals(1, sistema.cantidadEnEspera());

        sistema.procesarTarea();

        assertEquals(0, sistema.cantidadEnEspera());
        assertEquals(10, sistema.cantidadCriticasPendientes());
        assertNotNull(sistema.buscarTareaProcesada(1));
    }
}
