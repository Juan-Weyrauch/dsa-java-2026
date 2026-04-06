
import java.io.IOException;
import java.nio.file.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Ejercicio 11 — Entrada de datos por archivo y por teclado.
 *
 * Parte A: lee un archivo con un entero, un real y una cadena, muestra su suma
 * y división entera con resto.
 *
 * Parte B: lee el radio de una circunferencia desde stdin y devuelve su área y
 * perímetro.
 */
public class Principal {

    // PARTE A — leer desde archivo (NIO)
    public static void leerEntradaArchivo(String rutaArchivo) {
        System.out.println("PARTE A - Lectura desde archivo");

        Path path = Path.of(rutaArchivo);

        if (!Files.exists(path)) {
            System.out.println("Error: el archivo \"" + rutaArchivo + "\" no existe");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            if (lines.size() < 3) {
                System.out.println(" Error: el archivo debe tener al menos 3 líneas.");
                return;
            }
            int entero = Integer.parseInt(lines.get(0).trim());
            double real = Double.parseDouble(lines.get(1).trim());
            String cadena = lines.get(2).trim();

            // -------- Mostrar valores leídos --------
            System.out.println("  Valores leidos:");
            System.out.println("    Entero  -> " + entero);
            System.out.printf("    Real    -> %.2f%n", real);
            System.out.println("    Cadena  -> \"" + cadena + "\"");
            System.out.println();

            // -------- Operaciones --------
            double suma = entero + real;
            int divisor = (int) real;    // parte entera del real

            System.out.printf("  Suma (entero + real)       -> %.2f%n", suma);

            // División entera: controlar división por cero
            if (divisor == 0) {
                System.out.println("  División entera             -> Error: divisor es 0 (parte entera del real es 0)");
            } else {
                int cociente = entero / divisor;
                int resto = entero % divisor;
                System.out.println("  Division entera " + entero + " / " + divisor
                        + "    → cociente = " + cociente + ",  resto = " + resto);
            }

        } catch (NumberFormatException e) {
            System.out.println(" Error de formato: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(" Error al leer el archivo.");
        }

        System.out.println();
    }

    // PARTE B — leer desde stdin (teclado)
    public static void leerEntradaStdin() {
        System.out.println(" PARTE B - Lectura desde teclado ");

        Scanner sc = new Scanner(System.in);
        double radio = 0;
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.print("  Ingrese el radio de la circunferencia: ");
            try {
                radio = sc.nextDouble();
                if (radio <= 0) {
                    System.out.println(" Error: el radio debe ser un número positivo mayor que 0.");
                } else {
                    entradaValida = true;
                }
            } catch (InputMismatchException e) {
                System.out.println(" Error de formato: ingrese un número válido (ej: 5  o  3.14).");
                sc.nextLine(); // limpiar el buffer para no quedar en loop infinito
                sc.close();
            }
        }
        sc.close();

        // -------- Calcular y mostrar --------
        double area = Math.PI * radio * radio;
        double perimetro = 2 * Math.PI * radio;

        System.out.println();
        System.out.printf("  Radio      -> %.4f%n", radio);
        System.out.printf("  Area       -> pi x %.4f^2 = %.4f%n", radio, area);
        System.out.printf("  Perimetro  -> 2 x pi x %.4f = %.4f%n", radio, perimetro);
        System.out.println();
    }

    // MAIN
    public static void main(String[] args) {

        // Parte A — la ruta puede pasarse como argumento o usa el default
        String ruta = (args.length > 0) ? args[0] : "ejercicio11\\entrada.txt";
        leerEntradaArchivo(ruta);

        // Parte B
        leerEntradaStdin();
    }
}
