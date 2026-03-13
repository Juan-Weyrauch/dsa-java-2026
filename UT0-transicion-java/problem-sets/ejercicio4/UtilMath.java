// package dsa-java-2026.UT0-transicion-java.problem-sets.ejercicio4;


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
