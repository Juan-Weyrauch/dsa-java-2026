public class UtilMath {

    // ----- Factorial Method -----
    public static long calculateFactorial(int num) {
        if (num < 0) {
            throw new Error("Invalid num, must be positive and not zero boi.");
        }

        long result = 1;
        for (int i = 1; i <= num; i++) {
            result *= i;
        }

        return result;
    }

    // ----- Prime Method -----
    public static boolean isPrime(long n) {
        if (n <= 1) {
            return false;
        }

        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static int conditionalSum(int n) {
        int sum = 0;
        int i = 0;

        if (isPrime(n)) {
            while (i <= n) {
                if (i % 2 == 0){
                    sum += i;
                }
                i++;
            }
        } else {
            while (i <= n) {
                if (i % 2 != 0){
                    sum += i;
                }
                i ++;
            }
        }

        return sum;

    }

    public static void main(String[] args) {

        int number = 7;

        System.out.println("Factorial: " + calculateFactorial(number));
        System.out.println("Is prime: " + isPrime(number));
        System.out.println("Conditional sum: " + conditionalSum(number));
    }
}

/*
Explicación de al menos dos decisiones de diseño:
    - Se utilizó una variable de tipo long en el método "calculateFactorial" debido a que, a pesar de ocupar más espacio en memoria, 
    el mayor valor de un dato tipo integer es de 2.147.483.647, lo cual es 12! = 479,001,600, ya que 13! = 6,227,020,800 , y lo excede. 
    Long llega hasta 20!, lo cuál es (en nuestra opinión) más que suficiente para casos normales. En otro caso, se utlizaría una variable 
    BigInteger, ya que no tiene un límite teórico de espacio. 
    - Se utlizó el ciclo while y no el do-while ya que resulta en una comprensión más simple (a nuestro parecer), así como también nos da
    una lógica más simple, al no tener que trabajar con un primer loop que corre siempre. 
*/
