package ucu.edu.aed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ucu.edu.aed.modelo.SistemaGestionImplementation;
import ucu.edu.aed.sistema.SistemaGestion;
import ucu.edu.aed.modelo.Tarea;

/**
 * Punto de entrada del proyecto.
 *
 * <p>
 * <strong>TODO (alumno):</strong> completar el flujo de demostración:
 * <ol>
 * <li>Instanciar tu implementación de {@link SistemaGestion}.</li>
 * <li>Cargar las tareas desde {@code naves.txt} invocando {@code recibirTarea}
 * por
 * cada línea (la lectura/parseo del archivo ya está hecha en
 * {@link #cargarTareas}).</li>
 * <li>Procesar todas las tareas hasta vaciar las colas.</li>
 * <li>Demostrar la búsqueda de una tarea procesada por id (R3).</li>
 * <li>Demostrar la cancelación de una tarea pendiente por id (R4).</li>
 * </ol>
 */
public class Main {

    public static void main(String[] args) {
        String archivo = args.length > 0 ? args[0] : "naves.txt";

        // TODO: instanciar tu implementación de SistemaGestion (la clase que vos
        // crees).
        SistemaGestionImplementation sistema = new SistemaGestionImplementation();

        // 1. cargar
        cargarTareas(sistema, archivo);

        // 2. procesar TODO antes de buscar
        System.out.println("\n--- Procesando tareas ---");
        Tarea procesada;
        while ((procesada = sistema.procesarTarea()) != null) {
            System.out.println("[PROCESADA] " + procesada);
        }

        // 3. buscar en historial
        System.out.println("\n--- Búsqueda en historial (R3) ---");
        Tarea encontrada = sistema.buscarTareaProcesada(1);
        System.out.println(encontrada != null ? "[ENCONTRADA] " + encontrada : "[NO ENCONTRADA] id=1");

        Tarea encontrada2 = sistema.buscarTareaProcesada(2);
        System.out.println(encontrada2 != null ? "[ENCONTRADA] " + encontrada2 : "[NO ENCONTRADA] id=2");

        // 4. cancelar
        System.out.println("\n--- Cancelación (R4) ---");
        sistema.recibirTarea(new Tarea(999, "Tarea de prueba cancelacion", 4));
        Tarea cancelada = sistema.cancelarTarea(999);
        System.out.println(cancelada != null ? "[CANCELADA] " + cancelada : "[NO ENCONTRADA] id=999");
    }

    /**
     * Lee {@code naves.txt} e invoca {@code recibirTarea} en el sistema por cada
     * línea válida.
     *
     * <p>
     * Formato esperado por línea: {@code id;descripcion;criticidad}.
     * Las líneas vacías o que comienzan con {@code #} se ignoran.
     * </p>
     *
     * @param sistema sistema de gestión sobre el que registrar las tareas
     * @param path    ruta del archivo a leer
     */
    private static void cargarTareas(SistemaGestion sistema, String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#"))
                    continue;
                String[] partes = linea.split(";");
                if (partes.length < 3)
                    continue;
                try {
                    int id = Integer.parseInt(partes[0].trim());
                    String descripcion = partes[1].trim();
                    int criticidad = Integer.parseInt(partes[2].trim());
                    // TODO: construir una Tarea con (id, descripcion, criticidad)
                    // y llamar a sistema.recibirTarea(tarea).
                    Tarea t = new Tarea(id, descripcion, criticidad);
                    boolean admitida = sistema.recibirTarea(t);
                    System.out.println((admitida ? "[ADMITIDA] " : "[EN ESPERA] ") + t);

                } catch (IllegalArgumentException ex) {
                    System.err.println("Línea inválida: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer " + path + ": " + e.getMessage());
        }
    }
}
