package ucu.edu.aed.sistema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ucu.edu.aed.modelo.Tarea;
import ucu.edu.aed.tda.TDAArbolBinario;

/**
 * Tests del requerimiento <b>R3 - Registro de tareas procesadas</b>.
 *
 * <p>Verifican que:
 * <ul>
 *   <li>Las tareas procesadas son recuperables por id antes y después del umbral.</li>
 *   <li>Una tarea no procesada (todavía pendiente) no se encuentra en el historial.</li>
 *   <li>Tras alcanzar 75 procesadas, el historial se materializa en una estructura
 *       con búsqueda logarítmica garantizada (árbol AVL).</li>
 *   <li>La búsqueda por id coincide con la inserción para distintos ids.</li>
 * </ul>
 */
public class BuscarTareaProcesadaTest {

    private SistemaGestionNave sistema;

    @Before
    public void setUp() {
        sistema = new SistemaGestionNave();
    }

    private Tarea nueva(int id, int crit) {
        return new Tarea(id, "t" + id, crit);
    }

    private void llenarYProcesar(int cantidad) {
        for (int i = 1; i <= cantidad; i++) {
            sistema.recibirTarea(nueva(i, 4));
        }
        for (int i = 0; i < cantidad; i++) {
            sistema.procesarTarea();
        }
    }

    /**
     * <b>buscarSinProcesarRetornaNull</b>
     *
     * <p>Caso borde: si una tarea fue recibida pero todavía no se procesó, no debe
     * aparecer en el historial. Garantiza que el historial registra solo tareas
     * efectivamente ejecutadas.</p>
     */
    @Test
    public void buscarSinProcesarRetornaNull() {
        sistema.recibirTarea(nueva(42, 3));
        assertNull(sistema.buscarTareaProcesada(42));
    }

    /**
     * <b>tareaProcesadaEsRecuperablePorId_antesDelUmbral</b>
     *
     * <p>Antes de las 75 tareas procesadas la implementación usa una lista temporal.
     * El test garantiza que aun en esa fase la búsqueda devuelve la tarea correcta,
     * preservando la interfaz pública del sistema.</p>
     */
    @Test
    public void tareaProcesadaEsRecuperablePorId_antesDelUmbral() {
        llenarYProcesar(5);

        Tarea encontrada = sistema.buscarTareaProcesada(3);
        assertNotNull(encontrada);
        assertEquals(3, encontrada.getId());
    }

    /**
     * <b>buscarIdInexistenteRetornaNull</b>
     *
     * <p>Solicitar un id que nunca fue procesado debe devolver {@code null}, sin
     * lanzar excepciones. Cubre el contrato negativo de {@code buscarTareaProcesada}.</p>
     */
    @Test
    public void buscarIdInexistenteRetornaNull() {
        llenarYProcesar(10);
        assertNull(sistema.buscarTareaProcesada(999));
    }

    /**
     * <b>al75avoProcesado_seActivaHistorialAVL</b>
     *
     * <p>Verifica el cambio de estructura interna al cruzar el umbral R3 (75 tareas).
     * Antes de las 75 procesadas el sistema no tiene aún el árbol activo; tras procesar
     * la 75ª, el campo {@code historial} debe haberse materializado y contener todas
     * las tareas previas. Se accede al árbol vía un getter de paquete agregado para
     * tests, lo que confirma además que las búsquedas pasarán a ser O(log n).</p>
     */
    @Test
    public void al75avoProcesado_seActivaHistorialAVL() {
        llenarYProcesar(74);
        assertNull("El árbol no debe existir antes del umbral", sistema.getHistorial());

        // Procesar la tarea número 75
        sistema.recibirTarea(nueva(75, 4));
        sistema.procesarTarea();

        TDAArbolBinario<Tarea> arbol = sistema.getHistorial();
        assertNotNull("El árbol AVL debe estar activo a partir de las 75 procesadas", arbol);
        assertEquals(75, arbol.cantidadNodos());
    }

    /**
     * <b>busquedaPorIdEsConsistentePostUmbral</b>
     *
     * <p>Una vez activo el árbol AVL, todas las tareas procesadas deben ser localizables
     * por id. El test procesa 100 tareas con ids no consecutivos (insertados en orden
     * descendente para forzar el balanceo del AVL) y consulta varias para confirmar que
     * la búsqueda devuelve siempre la tarea correcta.</p>
     */
    @Test
    public void busquedaPorIdEsConsistentePostUmbral() {
        for (int id = 100; id >= 1; id--) {
            sistema.recibirTarea(nueva(id, 4));
        }
        for (int i = 0; i < 100; i++) {
            sistema.procesarTarea();
        }

        assertNotNull(sistema.getHistorial());
        for (int id : new int[] {1, 25, 50, 75, 100}) {
            Tarea t = sistema.buscarTareaProcesada(id);
            assertNotNull("Debió encontrarse la tarea " + id, t);
            assertEquals(id, t.getId());
        }
        assertNull(sistema.buscarTareaProcesada(101));
    }

    /**
     * <b>arbolHistorialMantieneAlturaLogaritmica</b>
     *
     * <p>Refuerza la garantía AVL del enunciado: tras insertar 100 nodos en el peor caso
     * (ids descendentes), la altura del árbol debe permanecer acotada en aproximadamente
     * log2(n). Para n=100 se acepta hasta altura 8 (1.44·log2(100)+2 ≈ 11, con margen).
     * Si el árbol no se balanceara, la altura sería 100.</p>
     */
    @Test
    public void arbolHistorialMantieneAlturaLogaritmica() {
        for (int id = 100; id >= 1; id--) {
            sistema.recibirTarea(nueva(id, 4));
            sistema.procesarTarea();
        }
        TDAArbolBinario<Tarea> arbol = sistema.getHistorial();
        assertNotNull(arbol);
        int altura = arbol.obtenerRaiz().altura();
        assertTrue("Altura " + altura + " incompatible con AVL para 100 nodos", altura <= 8);
    }
}
