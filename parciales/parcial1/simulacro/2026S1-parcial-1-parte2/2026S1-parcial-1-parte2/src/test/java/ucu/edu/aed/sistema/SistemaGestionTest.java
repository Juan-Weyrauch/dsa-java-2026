package ucu.edu.aed.sistema;

import org.junit.Before;
import org.junit.Test;
import ucu.edu.aed.modelo.*;

import static org.junit.Assert.*;

public class SistemaGestionTest {

    private SistemaGestion sistema;

    @Before
    public void setUp() {
        sistema = new SistemaGestionImplementation();
    }

    // ── R1: recibirTarea ─────────────────────────────────────────────────────

    @Test
    public void recibirTarea_null_retornaFalse() {
        assertFalse(sistema.recibirTarea(null));
    }

    @Test
    public void recibirTarea_tareaValida_retornaTrue() {
        assertTrue(sistema.recibirTarea(new Tarea(1, "desc", 3)));
    }

    @Test
    public void recibirTarea_alLlegarA25_siguienteVaAEspera() {
        for (int i = 1; i <= 25; i++) {
            sistema.recibirTarea(new Tarea(i, "desc", 3));
        }
        assertFalse(sistema.recibirTarea(new Tarea(26, "desc", 3)));
    }

    @Test
    public void recibirTarea_10Criticas_undecimaCriticaVaAEspera() {
        for (int i = 1; i <= 10; i++) {
            sistema.recibirTarea(new Tarea(i, "critica", 1));
        }
        assertFalse(sistema.recibirTarea(new Tarea(11, "critica", 2)));
    }

    @Test
    public void recibirTarea_10Criticas_tareaNoSeDescarta_sinoEspera() {
        for (int i = 1; i <= 10; i++) {
            sistema.recibirTarea(new Tarea(i, "critica", 1));
        }
        Tarea enEspera = new Tarea(11, "espera", 2);
        sistema.recibirTarea(enEspera);

        // vaciamos todas las de crit 1 (10 tareas)
        for (int i = 0; i < 10; i++) {
            sistema.procesarTarea();
        }

        // ahora la única pendiente es la 11 (entró cuando bajó a 9 críticas)
        Tarea procesada = sistema.procesarTarea();
        assertNotNull(procesada);
        assertEquals(11, procesada.getId());
    }

    @Test
    public void recibirTarea_10Criticas_tareaNosCriticaSiEntra() {
        for (int i = 1; i <= 10; i++) {
            sistema.recibirTarea(new Tarea(i, "critica", 1));
        }
        // criticidad 3 no es crítica, debe entrar igual aunque haya 10 críticas
        assertTrue(sistema.recibirTarea(new Tarea(11, "normal", 3)));
    }

    // ── R2: procesarTarea ────────────────────────────────────────────────────

    @Test
    public void procesarTarea_sinPendientes_retornaNull() {
        assertNull(sistema.procesarTarea());
    }

    @Test
    public void procesarTarea_siempreSaleElDeMayorCriticidad() {
        sistema.recibirTarea(new Tarea(1, "baja", 4));
        sistema.recibirTarea(new Tarea(2, "alta", 1));
        sistema.recibirTarea(new Tarea(3, "media", 3));

        Tarea procesada = sistema.procesarTarea();
        assertEquals(2, procesada.getId()); // criticidad 1 primero
    }

    @Test
    public void procesarTarea_empateEnCriticidad_salePrimeroElQueEntroAntes() {
        sistema.recibirTarea(new Tarea(1, "primera", 2));
        sistema.recibirTarea(new Tarea(2, "segunda", 2));
        sistema.recibirTarea(new Tarea(3, "tercera", 2));

        assertEquals(1, sistema.procesarTarea().getId());
        assertEquals(2, sistema.procesarTarea().getId());
        assertEquals(3, sistema.procesarTarea().getId());
    }

    @Test
    public void procesarTarea_criticas_antesQueNormales() {
        sistema.recibirTarea(new Tarea(1, "normal", 3));
        sistema.recibirTarea(new Tarea(2, "normal", 4));
        sistema.recibirTarea(new Tarea(3, "critica", 2));

        assertEquals(3, sistema.procesarTarea().getId());
    }

    @Test
    public void procesarTarea_tareaQuedaEnHistorial() {
        Tarea t = new Tarea(42, "desc", 1);
        sistema.recibirTarea(t);
        sistema.procesarTarea();

        assertNotNull(sistema.buscarTareaProcesada(42));
    }

    // ── R3: buscarTareaProcesada ─────────────────────────────────────────────

    @Test
    public void buscarTareaProcesada_noExiste_retornaNull() {
        assertNull(sistema.buscarTareaProcesada(999));
    }

    @Test
    public void buscarTareaProcesada_existeEnHistorial_retornaTarea() {
        sistema.recibirTarea(new Tarea(7, "desc", 3));
        sistema.procesarTarea();

        Tarea encontrada = sistema.buscarTareaProcesada(7);
        assertNotNull(encontrada);
        assertEquals(7, encontrada.getId());
    }

    @Test
    public void buscarTareaProcesada_pendienteNoEstaEnHistorial() {
        sistema.recibirTarea(new Tarea(5, "desc", 3));
        // no se procesó → no debe estar en el historial
        assertNull(sistema.buscarTareaProcesada(5));
    }

    // ── R4: cancelarTarea ────────────────────────────────────────────────────

    @Test
    public void cancelarTarea_pendiente_retornaTarea() {
        sistema.recibirTarea(new Tarea(10, "desc", 3));
        Tarea cancelada = sistema.cancelarTarea(10);
        assertNotNull(cancelada);
        assertEquals(10, cancelada.getId());
    }

    @Test
    public void cancelarTarea_noExiste_retornaNull() {
        assertNull(sistema.cancelarTarea(999));
    }

    @Test
    public void cancelarTarea_yaFueProcesada_retornaNull() {
        sistema.recibirTarea(new Tarea(8, "desc", 1));
        sistema.procesarTarea(); // pasa al historial
        assertNull(sistema.cancelarTarea(8)); // no debe poder cancelarse
    }

    @Test
    public void cancelarTarea_liberaCupo_tareaEnEsperaEntra() {
        // llenamos las 25 pendientes
        for (int i = 1; i <= 25; i++) {
            sistema.recibirTarea(new Tarea(i, "desc", 3));
        }
        // esta va a espera
        sistema.recibirTarea(new Tarea(26, "desc", 3));

        // cancelamos una pendiente → baja a 24 → la 26 debe entrar
        sistema.cancelarTarea(1);

        // la 26 ahora está pendiente, se puede procesar
        boolean encontrada = false;
        Tarea t;
        while ((t = sistema.procesarTarea()) != null) {
            if (t.getId() == 26) {
                encontrada = true;
                break;
            }
        }
        assertTrue(encontrada);
    }

    @Test
    public void cancelarTarea_noPuedeEliminarDelHistorial() {
        sistema.recibirTarea(new Tarea(3, "desc", 2));
        sistema.procesarTarea();

        // intentar cancelar una procesada no toca el historial
        sistema.cancelarTarea(3);
        assertNotNull(sistema.buscarTareaProcesada(3));
    }
}