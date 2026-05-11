package uy.edu.ucu.aed;

import uy.edu.ucu.aed.modelo.Dron;
import uy.edu.ucu.aed.modelo.RedDeEstaciones;
import uy.edu.ucu.aed.modelo.RedDeEstaciones.EstadoExploracion;

/**
 * Programa principal que demuestra la simulación del dron
 * con los datos de ejemplo provistos por el enunciado:
 * IDs: [72, 9, 68, 120, 55, 63, 80]
 *
 * Condiciones:
 *   - OPTIMO:   señal entre 60 y 80  → recarga
 *   - CRITICO:  señal <= 10 o > 100  → FallaNavegacion
 *   - SUBOPTIMO: resto               → cuenta consecutivas
 *
 * Con estos datos esperamos FallaNavegacion porque el ID 9 es CRITICO (<=10).
 */
public class Main {

    public static void main(String[] args) {

        // --- Ejemplo del enunciado ---
        int[] ids = {72, 9, 68, 120, 55, 63, 80};

        RedDeEstaciones red = new RedDeEstaciones();
        red.cargar(ids);

        // Batería inicial 100, máximo 3 estaciones consecutivas sin recarga
        Dron dron = new Dron(100, 3);

        System.out.println("=== Simulación ejemplo del enunciado ===");
        System.out.println("IDs cargados: 72, 9, 68, 120, 55, 63, 80");
        System.out.println("Batería inicial: " + dron.getBateria());
        System.out.println("Max consecutivas sin recarga: " + dron.getMaxConsecutivas());
        System.out.println();

        EstadoExploracion estado = red.explorar(dron);

        System.out.println("Estado final: " + estado);
        System.out.println("Batería restante: " + dron.getBateria());
        System.out.println("Estaciones visitadas: " + dron.getVisitadas());

        System.out.println();

        // --- Ejemplo sin críticos para demostrar CompletaRuta ---
        int[] idsSeguros = {65, 40, 75, 70};

        RedDeEstaciones redSegura = new RedDeEstaciones();
        redSegura.cargar(idsSeguros);

        Dron dronSeguro = new Dron(100, 3);

        System.out.println("=== Simulación sin estaciones críticas ===");
        System.out.println("IDs cargados: 65, 40, 75, 70");

        EstadoExploracion estadoSeguro = redSegura.explorar(dronSeguro);

        System.out.println("Estado final: " + estadoSeguro);
        System.out.println("Batería restante: " + dronSeguro.getBateria());
        System.out.println("Estaciones visitadas: " + dronSeguro.getVisitadas());

        System.out.println();

        // --- Ejemplo que agota batería ---
        int[] idsSuboptimos = {50, 30, 55, 25, 45};

        RedDeEstaciones redSuboptima = new RedDeEstaciones();
        redSuboptima.cargar(idsSuboptimos);

        // Máximo 2 consecutivas sin recarga
        Dron dronLimitado = new Dron(100, 2);

        System.out.println("=== Simulación que agota batería ===");
        System.out.println("IDs cargados: 50, 30, 55, 25, 45 (todos SUBOPTIMO)");
        System.out.println("Max consecutivas: 2");

        EstadoExploracion estadoAgotado = redSuboptima.explorar(dronLimitado);

        System.out.println("Estado final: " + estadoAgotado);
        System.out.println("Batería restante: " + dronLimitado.getBateria());
        System.out.println("Estaciones visitadas: " + dronLimitado.getVisitadas());
    }
}
