package ucu.edu.aed.sistema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import ucu.edu.aed.modelo.Tarea;

/**
 * Tests del requerimiento <b>R2 - Procesamiento de tareas</b>.
 *
 * <p>Verifican el orden de ejecución:
 * <ul>
 *   <li>Mayor criticidad disponible primero (1 antes que 2, 2 antes que 3, etc.).</li>
 *   <li>En empate de criticidad: FIFO (orden de llegada).</li>
 *   <li>Las tareas normales solo se ejecutan cuando no quedan críticas pendientes.</li>
 *   <li>Cuando no hay tareas pendientes, {@code procesarTarea} retorna {@code null}.</li>
 * </ul>
 */
public class ProcesarTareaTest {

    private SistemaGestionNave sistema;

    @Before
    public void setUp() {
        sistema = new SistemaGestionNave();
    }

    private Tarea nueva(int id, int crit) {
        return new Tarea(id, "t" + id, crit);
    }

    /**
     * <b>procesarSobreSistemaVacioRetornaNull</b>
     *
     * <p>Caso borde: invocar {@code procesarTarea} sin tareas pendientes no debe lanzar
     * excepción y debe retornar {@code null}, indicando al llamador que no hay nada por hacer.</p>
     */
    @Test
    public void procesarSobreSistemaVacioRetornaNull() {
        assertNull(sistema.procesarTarea());
    }

    /**
     * <b>criticidadMayorSeEjecutaAntes_aunqueLlegueDespues</b>
     *
     * <p>Demuestra la regla central de R2: al recibir primero una tarea de criticidad 4 y
     * luego una de criticidad 1, debe ejecutarse antes la crítica. Verifica que la prioridad
     * domina al orden temporal cuando los niveles son distintos.</p>
     */
    @Test
    public void criticidadMayorSeEjecutaAntes_aunqueLlegueDespues() {
        sistema.recibirTarea(nueva(10, 4));
        sistema.recibirTarea(nueva(20, 1));

        assertEquals(20, sistema.procesarTarea().getId());
        assertEquals(10, sistema.procesarTarea().getId());
    }

    /**
     * <b>entreCriticidad1y2_seEjecutaPrimeroLa1</b>
     *
     * <p>Aclara la jerarquía dentro de las tareas críticas: con una criticidad 2 ya en cola
     * y luego una criticidad 1, la 1 se ejecuta antes — es decir, {@code esCritica()} no
     * borra la diferencia interna entre niveles.</p>
     */
    @Test
    public void entreCriticidad1y2_seEjecutaPrimeroLa1() {
        sistema.recibirTarea(nueva(1, 2));
        sistema.recibirTarea(nueva(2, 1));

        assertEquals(2, sistema.procesarTarea().getId());
        assertEquals(1, sistema.procesarTarea().getId());
    }

    /**
     * <b>empateCriticidad_resolvidoPorOrdenDeLlegadaFIFO</b>
     *
     * <p>Verifica el desempate por orden de llegada cuando dos tareas tienen exactamente
     * la misma criticidad. Ejecuta tres criticidades 3 ingresadas en orden A, B, C y
     * confirma que se procesan en ese mismo orden.</p>
     */
    @Test
    public void empateCriticidad_resolvidoPorOrdenDeLlegadaFIFO() {
        sistema.recibirTarea(nueva(100, 3));
        sistema.recibirTarea(nueva(200, 3));
        sistema.recibirTarea(nueva(300, 3));

        assertEquals(100, sistema.procesarTarea().getId());
        assertEquals(200, sistema.procesarTarea().getId());
        assertEquals(300, sistema.procesarTarea().getId());
    }

    /**
     * <b>entreNormales_primaFIFOSobreCriticidad</b>
     *
     * <p>Aclara la regla específica del enunciado para criticidades 3 y 4:
     * <em>"Las tareas de criticidad 3 y 4 deben organizarse respetando estrictamente
     * el orden en que fueron recibidas"</em>. Si llega primero una criticidad 4 y luego
     * una criticidad 3, se ejecuta antes la 4 (la que llegó primero), aunque la 3 tenga
     * "mayor criticidad" en sentido estricto. Es decir, dentro del grupo de tareas
     * normales (3 y 4) se aplica FIFO puro sin reordenar por criticidad.
     * En contraposición, las críticas (1 y 2) sí se reordenan por nivel.</p>
     */
    @Test
    public void entreNormales_primaFIFOSobreCriticidad() {
        sistema.recibirTarea(nueva(1, 4));
        sistema.recibirTarea(nueva(2, 3));

        assertEquals(1, sistema.procesarTarea().getId());
        assertEquals(2, sistema.procesarTarea().getId());
    }

    /**
     * <b>criticasSeVacianAntesDeQueSeProceseUnaNormal</b>
     *
     * <p>Combina ambas reglas: con 2 críticas y 2 normales pendientes, las dos críticas
     * deben ejecutarse antes que cualquier normal. Comprueba que la cola de normales
     * solo se atiende cuando la de críticas está vacía.</p>
     */
    @Test
    public void criticasSeVacianAntesDeQueSeProceseUnaNormal() {
        sistema.recibirTarea(nueva(1, 3));
        sistema.recibirTarea(nueva(2, 4));
        sistema.recibirTarea(nueva(3, 1));
        sistema.recibirTarea(nueva(4, 2));

        assertEquals(3, sistema.procesarTarea().getId());
        assertEquals(4, sistema.procesarTarea().getId());
        assertEquals(1, sistema.procesarTarea().getId());
        assertEquals(2, sistema.procesarTarea().getId());
    }

    /**
     * <b>procesarIncrementaContador_yReduceColaPendientes</b>
     *
     * <p>Verifica los efectos colaterales de procesar: la tarea se quita de pendientes y
     * el contador de procesadas se incrementa. Es la garantía mínima sobre el estado del
     * sistema entre invocaciones consecutivas a {@code procesarTarea}.</p>
     */
    @Test
    public void procesarIncrementaContador_yReduceColaPendientes() {
        sistema.recibirTarea(nueva(7, 3));
        sistema.recibirTarea(nueva(8, 4));

        sistema.procesarTarea();
        assertEquals(1, sistema.cantidadPendientes());
        assertEquals(1, sistema.cantidadProcesadas());

        sistema.procesarTarea();
        assertEquals(0, sistema.cantidadPendientes());
        assertEquals(2, sistema.cantidadProcesadas());
    }
}
