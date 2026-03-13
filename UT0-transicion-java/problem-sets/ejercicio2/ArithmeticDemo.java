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

class ArithmeticDemo {

    public static void main(String[] args) {
        int result = 1 + 2;

        result -= 1;          // equivalente a: result = result - 1
        result *= 2;          // equivalente a: result = result * 2
        result /= 2;          // equivalente a: result = result / 2
        result += 8;          // equivalente a: result = result + 8
        result %= 7;          // equivalente a: result = result % 7

        System.out.println("Resultado final: " + result);

        // ---- Ejemplo de orden de evaluación ----
        int a = 5;
        int i = 3;

        a += ++i;

        System.out.println("Valor de a: " + a);
        System.out.println("Valor de i: " + i);
    }
}