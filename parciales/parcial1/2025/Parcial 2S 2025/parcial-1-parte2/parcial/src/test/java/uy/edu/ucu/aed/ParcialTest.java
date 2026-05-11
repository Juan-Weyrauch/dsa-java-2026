package uy.edu.ucu.aed;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uy.edu.ucu.aed.modelo.Dron;
import uy.edu.ucu.aed.modelo.Estacion;
import uy.edu.ucu.aed.modelo.RedDeEstaciones;
import uy.edu.ucu.aed.modelo.RedDeEstaciones.EstadoExploracion;

import static org.junit.jupiter.api.Assertions.*;

class ParcialTest {

    // ===================== TESTS DE Estacion =====================

    @Test
    void estacionConSenialOptimaDevuelveOptimo() {
        assertEquals(Estacion.CondicionOperativa.OPTIMO, new Estacion(60).getCondicion());
        assertEquals(Estacion.CondicionOperativa.OPTIMO, new Estacion(80).getCondicion());
        assertEquals(Estacion.CondicionOperativa.OPTIMO, new Estacion(70).getCondicion());
    }

    @Test
    void estacionConSenialCriticaBajaDevuelveCritico() {
        assertEquals(Estacion.CondicionOperativa.CRITICO, new Estacion(10).getCondicion());
        assertEquals(Estacion.CondicionOperativa.CRITICO, new Estacion(1).getCondicion());
    }

    @Test
    void estacionConSenialCriticaAltaDevuelveCritico() {
        assertEquals(Estacion.CondicionOperativa.CRITICO, new Estacion(101).getCondicion());
        assertEquals(Estacion.CondicionOperativa.CRITICO, new Estacion(120).getCondicion());
    }

    @Test
    void estacionConSenialSuboptimaDevuelveSuboptimo() {
        assertEquals(Estacion.CondicionOperativa.SUBOPTIMO, new Estacion(11).getCondicion());
        assertEquals(Estacion.CondicionOperativa.SUBOPTIMO, new Estacion(59).getCondicion());
        assertEquals(Estacion.CondicionOperativa.SUBOPTIMO, new Estacion(81).getCondicion());
        assertEquals(Estacion.CondicionOperativa.SUBOPTIMO, new Estacion(100).getCondicion());
    }

    // ===================== TESTS DE Dron =====================

    @Test
    void dronInicializaCorrectamente() {
        Dron dron = new Dron(100, 3);
        assertEquals(100, dron.getBateria());
        assertEquals(0, dron.getConsecutivasSinRecarga());
        assertTrue(dron.getVisitadas().isEmpty());
    }

    @Test
    void dronConsomeEnergia() {
        Dron dron = new Dron(100, 3);
        dron.consumirEnergia();
        assertEquals(100 - Dron.COSTO_ENERGIA, dron.getBateria());
    }

    @Test
    void dronRecargaEnergia() {
        Dron dron = new Dron(50, 3);
        dron.recargar();
        assertEquals(50 + Dron.CANTIDAD_RECARGA, dron.getBateria());
    }

    @Test
    void dronMarcaVisitadaCorrectamente() {
        Dron dron = new Dron(100, 3);
        assertFalse(dron.yaVisitada(42));
        dron.marcarVisitada(42);
        assertTrue(dron.yaVisitada(42));
    }

    @Test
    void dronIncrementaYResetaConsecutivas() {
        Dron dron = new Dron(100, 3);
        dron.incrementarConsecutivas();
        dron.incrementarConsecutivas();
        assertEquals(2, dron.getConsecutivasSinRecarga());
        dron.resetConsecutivas();
        assertEquals(0, dron.getConsecutivasSinRecarga());
    }

    // ===================== TESTS DE explorar =====================

    @Test
    void explorarArbolVacioDevuelveCompletaRuta() {
        RedDeEstaciones red = new RedDeEstaciones();
        Dron dron = new Dron(100, 3);
        assertEquals(EstadoExploracion.CompletaRuta, red.explorar(dron));
    }

    @Test
    void explorarConEstacionCriticaDevuelveFallaNavegacion() {
        // ID 9 es CRITICO (<=10)
        RedDeEstaciones red = new RedDeEstaciones();
        red.cargar(new int[]{9});
        Dron dron = new Dron(100, 3);
        assertEquals(EstadoExploracion.FallaNavegacion, red.explorar(dron));
    }

    @Test
    void explorarConEstacionCriticaAltaDevuelveFallaNavegacion() {
        // ID 120 es CRITICO (>100)
        RedDeEstaciones red = new RedDeEstaciones();
        red.cargar(new int[]{65, 120});
        Dron dron = new Dron(100, 3);
        assertEquals(EstadoExploracion.FallaNavegacion, red.explorar(dron));
    }

    @Test
    void explorarSoloOptimosDevuelveCompletaRuta() {
        // 65, 70, 75 todos OPTIMO
        RedDeEstaciones red = new RedDeEstaciones();
        red.cargar(new int[]{65, 70, 75});
        Dron dron = new Dron(100, 3);
        assertEquals(EstadoExploracion.CompletaRuta, red.explorar(dron));
    }

    @Test
    void explorarSuperaMaxConsecutivasDevuelveBateriaAgotada() {
        // Todos SUBOPTIMO, maxConsecutivas = 2
        // IDs 50, 30, 55 → preorder: 50, 30, 55 → 3 consecutivas > 2
        RedDeEstaciones red = new RedDeEstaciones();
        red.cargar(new int[]{50, 30, 55});
        Dron dron = new Dron(100, 2);
        assertEquals(EstadoExploracion.BateriaAgotada, red.explorar(dron));
    }

    @Test
    void explorarEjemploEnunciadoDevuelveFallaNavegacion() {
        // [72, 9, 68, 120, 55, 63, 80]
        // La raíz es 72 (OPTIMO), hijo izq es 9 (CRITICO) → FallaNavegacion
        RedDeEstaciones red = new RedDeEstaciones();
        red.cargar(new int[]{72, 9, 68, 120, 55, 63, 80});
        Dron dron = new Dron(100, 3);
        assertEquals(EstadoExploracion.FallaNavegacion, red.explorar(dron));
    }

    @Test
    void estacionYaVisitadaNoConsomeEnergia() {
        RedDeEstaciones red = new RedDeEstaciones();
        red.cargar(new int[]{65});
        Dron dron = new Dron(100, 3);

        // Primera exploración
        red.explorar(dron);
        int bateriaPost = dron.getBateria();

        // Segunda exploración: el nodo ya está visitado, no debe consumir
        red.explorar(dron);
        assertEquals(bateriaPost, dron.getBateria());
    }
}
