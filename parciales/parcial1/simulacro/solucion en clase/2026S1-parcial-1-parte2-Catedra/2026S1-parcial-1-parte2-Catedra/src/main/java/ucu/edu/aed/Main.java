package ucu.edu.aed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import ucu.edu.aed.modelo.Tarea;
import ucu.edu.aed.sistema.SistemaGestionNave;

/**
 * Punto de entrada del proyecto. Carga las tareas definidas en {@code naves.txt}
 * en una instancia de {@link SistemaGestionNave}, las procesa secuencialmente
 * y demuestra la búsqueda de una tarea procesada por id (R3).
 */
public class Main {

    /**
     * Ejecuta el flujo completo de demostración:
     * <ol>
     *   <li>Carga las tareas desde el archivo (parámetro {@code args[0]} o {@code naves.txt}).</li>
     *   <li>Procesa todas las tareas hasta vaciar las colas (incluida la de espera).</li>
     *   <li>Muestra la cantidad final de procesadas y un ejemplo de búsqueda en el historial.</li>
     * </ol>
     *
     * @param args opcional: ruta al archivo de tareas
     */
    public static void main(String[] args) {
        SistemaGestionNave sistema = new SistemaGestionNave();
        String archivo = args.length > 0 ? args[0] : "naves.txt";

        cargarTareas(sistema, archivo);

        System.out.println("Pendientes tras carga: " + sistema.cantidadPendientes()
                + " (críticas: " + sistema.cantidadCriticasPendientes() + ")");
        System.out.println("En espera: " + sistema.cantidadEnEspera());

        while (sistema.cantidadPendientes() > 0 || sistema.cantidadEnEspera() > 0) {
            Tarea ejec = sistema.procesarTarea();
            if (ejec == null) break;
            System.out.println("Procesada -> " + ejec);
        }

        System.out.println("Total procesadas: " + sistema.cantidadProcesadas());

        Tarea encontrada = sistema.buscarTareaProcesada(1);
        System.out.println("Búsqueda historial id=1 -> " + encontrada);
    }

    /**
     * Lee el archivo de tareas e invoca {@link SistemaGestionNave#recibirTarea(Tarea)}
     * por cada línea válida.
     *
     * <p>Formato esperado por línea: {@code id;descripcion;criticidad}.
     * Las líneas vacías o que comienzan con {@code #} se ignoran.</p>
     *
     * @param sistema sistema de gestión sobre el que registrar las tareas
     * @param archivo ruta del archivo a leer
     */
    private static void cargarTareas(SistemaGestionNave sistema, String archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                String[] partes = linea.split(";");
                if (partes.length < 3) continue;
                try {
                    int id = Integer.parseInt(partes[0].trim());
                    String descripcion = partes[1].trim();
                    int criticidad = Integer.parseInt(partes[2].trim());
                    sistema.recibirTarea(new Tarea(id, descripcion, criticidad));
                } catch (IllegalArgumentException ex) {
                    System.err.println("Línea inválida: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer " + archivo + ": " + e.getMessage());
        }
    }
}
