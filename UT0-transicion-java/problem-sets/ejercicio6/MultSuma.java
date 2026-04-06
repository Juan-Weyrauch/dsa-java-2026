
import java.util.Arrays;
import java.util.Scanner;

public class MultSuma {

    // Overload: Same method name, java decides which method to use depending on the input data we sent. 
    // int version (overload)
    public static int multsuma(int a, int b, int c) {
        return a * b + c;
    }

    // double version (overload)
    public static double multsuma(double a, double b, double c) {
        return a * b + c;
    }

    // Vector version (overload)
    public static int[] multsuma(int[] a, int[] b, int[] c) {
        validate(a, b, c); // validate is responsable of flagging errors.

        int[] result = new int[a.length];

        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b[i] + c[i];
        }

        return result;
    }

    // ---------- Validation ----------
    private static void validate(int[] a, int[] b, int[] c) {
        if (a.length != b.length || b.length != c.length) {
            throw new IllegalArgumentException(
                    "Los vectores deben tener el mismo largo. "
                    + "Recibidos: " + a.length + ", " + b.length + ", " + c.length
            );
        }
    }

    // ---------- Input ----------
    public static int[] readVector(Scanner sc, String nombre, int largo) {
        int[] v = new int[largo];
        System.out.print("  Input " + largo + " integers to " + nombre + ": ");
        for (int i = 0; i < largo; i++) {
            v[i] = Integer.parseInt(sc.next()); // String -> int
        }
        return v;
    }

    // ---------- Output ----------
    public static void printArray(String etiqueta, int[] resultado) {
        System.out.println("  " + etiqueta + ": " + Arrays.toString(resultado));
    }

    // ---------- Main ----------
    public static void main(String[] args) {

        // --------- Escalares ---------
        System.out.println("=== Scalars ===");
        System.out.println("  multsuma(2, 3, 4)       = " + multsuma(2, 3, 4));
        System.out.println("  multsuma(2.5, 3.0, 1.2) = " + multsuma(2.5, 3.0, 1.2));

        // --------- Vectores fijos ---------
        System.out.println("\n=== Vectors (hardcoded) ===");

        int[] A = {1, 2, 3};
        int[] B = {4, 5, 6};
        int[] C = {7, 8, 9};
        int[] invalid = {1, 2};

        // Valid case
        printArray("A*B+C", multsuma(A, B, C));

        // Invalid case
        try {
            printArray("A*B+invalid", multsuma(A, B, invalid));
        } catch (IllegalArgumentException e) {
            System.out.println("  Expected error: " + e.getMessage());
        }

        // --------- Entrada por consola ---------
        System.out.println("\n=== Vectors (console input) ===");
        Scanner sc = new Scanner(System.in);

        System.out.print("How many elements do the vectors have? ");
        int size = Integer.parseInt(sc.nextLine());

        int[] X = readVector(sc, "X", size);
        int[] Y = readVector(sc, "Y", size);
        int[] Z = readVector(sc, "Z", size);

        sc.close();

        printArray("X*Y+Z", multsuma(X, Y, Z));
    }
}

/*
### Explicación de la sobrecarga

Java distingue qué versión de `multsuma` llamar según los tipos de los argumentos en tiempo de compilación. 
Cuando pasás tres `int`, usa la primera. Cuando pasás tres `double`, usa la segunda. Cuando pasás tres `int[]`, 
usa la tercera. El nombre es el mismo — la firma (tipos de parámetros) es lo que los diferencia.

### Estrategia de validación y cálculo vectorial

La validación chequea que los tres vectores tengan el mismo largo antes de operar. Si no lo tienen, no tiene sentido continuar,
se lanza una `IllegalArgumentException` inmediatamente. Si pasan la validación, el cálculo es elemento a elemento: 
`resultado[i] = a[i] * b[i] + c[i]` para cada posición `i`.

### Salida de ejemplo
=== Escalares ===
  multsuma(2, 3, 4)         = 10
  multsuma(2.5, 3.0, 1.2)   = 8.7

=== Vectores (hardcodeados) ===
  A*B+C: [11, 18, 27]
  Error esperado: Los vectores deben tener el mismo largo. Recibidos: 3, 3, 2

=== Vectores (entrada por consola) ===
¿Cuántos elementos tienen los vectores? 3
  Ingrese 3 enteros para X: 1 2 3
  Ingrese 3 enteros para Y: 4 5 6
  Ingrese 3 enteros para Z: 7 8 9
  X*Y+Z: [11, 18, 27]

 */
