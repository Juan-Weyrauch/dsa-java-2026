// class ArithmeticDemo {
//     public static void main(String[] args) {
//         int result = 1 + 2;
//         result = result - 1;
//         result = result * 2;
//         result = result / 2;
//         result = result + 8;
//         result = result % 7;
//     }
// }

import java.util.Scanner;

class ArithmeticDemo {

    public static void main(String[] args) {

        int result = 1 + 2;     // result no existe todavia, no se puede escribir como asignacion comp.

        result -= 1;            // equivalente a: result = result - 1
        result *= 2;            // equivalente a: result = result * 2
        result /= 2;            // equivalente a: result = result / 2
        result += 8;            // equivalente a: result = result + 8
        result %= 7;            // equivalente a: result = result % 7

        System.out.println("------ Parte 1: ------");
        System.out.println("Resultado final: " + result); // 3
        System.out.println();

        // ---- Ejemplo de orden de evaluación ----
        System.out.println("------ Parte 2: ------");
        int a = 5;
        int i = 3;

        // Orden:
        // 1.       ++i  → i se incrementa de 3 a 4 (pre-incremento)
        // 2.       a += 4 → a = 5 + 4 = 9
        a += ++i;

        System.out.println("i = " + i); // 4
        System.out.println("a = " + a); // 9
        System.out.println();

        System.out.println("------ Parte 3 ------");

        Scanner input = new Scanner(System.in);

        System.out.print("Ingrese el primer numero: ");
        double x = Double.parseDouble(input.nextLine()); // String -> double

        System.out.print("Ingrese el segundo numero: ");
        double y = Double.parseDouble(input.nextLine()); // String -> double

        input.close();

        System.out.printf("%nOperandos: x = %.2f, y = %.2f%n", x, y);
        System.out.printf("  x + y = %.2f%n", x + y);
        System.out.printf("  x - y = %.2f%n", x - y);
        System.out.printf("  x * y = %.2f%n", x * y);

        if (y == 0) {
            System.out.println("  x / y = Error: división por cero");
            System.out.println("  x % y = Error: división por cero");
        } else {
            System.out.printf("  x / y = %.2f%n", x / y);
            System.out.printf("  x %% y = %.2fF%n", x % y);
        }

        System.exit(0); // para cerrar al bro (no se porque no lo hace cuando llega al final del programa(? )

    }
}

/*
    Orden de evaluación:
    int result = 1 + 2
    Java evalúa el lado derecho primero. 1 + 2 son dos literales int, la suma da 3, y ese valor se asigna a result.
    a += ++i con a=5, i=3

    Se evalúa el lado derecho: ++i es pre-incremento, entonces i pasa a 4 antes de usarse
    Recién ahí se ejecuta a += 4 → a = 5 + 4 = 9

    Si fuera a += i++, el orden sería al revés: se usa i=3 primero, a queda en 8, y después i pasa a 4.
    result %= 7 con result=10
    % es el operador módulo — devuelve el resto de la división entera. 10 / 7 = 1 con resto 3, entonces result queda en 3. 

    Tabla de conversiones
    String  -> Double:  Double.parseDouble()            -> leer input del usuario
    int     -> Double:  'widening' implicito            -> result (int) en operaciones con doubles.
    Double  -> String:  implícito en printf con %.2f    -> mostrar resultados formateados
 */ 
