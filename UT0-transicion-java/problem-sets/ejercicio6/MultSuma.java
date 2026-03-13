import java.util.Arrays;

public class MultSuma {

    // ---------- Overload for int ----------
    public static int multsuma(int a, int b, int c) {
        return a * b + c;
    }

    // ---------- Overload for double ----------
    public static double multsuma(double a, double b, double c) {
        return a * b + c;
    }

    // ---------- Vector version ----------
    public static int[] multsuma(int[] a, int[] b, int[] c) {

        if (!validate(a, b, c)) {
            System.out.println("Error: vectors must have the same length.");
            return null;
        }

        int[] result = new int[a.length];

        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b[i] + c[i];
        }

        return result;
    }

    // ---------- Validation ----------
    public static boolean validate(int[] a, int[] b, int[] c) {
        return a.length == b.length && b.length == c.length;
    }

    // ---------- Output ----------
    public static void printArray(int[] arr) {
        if (arr == null) {
            System.out.println("Invalid operation.");
            return;
        }

        System.out.println(Arrays.toString(arr));
    }

    // ---------- Main ----------
    public static void main(String[] args) {

        int[] A = {1,2,3};
        int[] B = {4,5,6};
        int[] C = {7,8,9};

        int[] invalid = {1,2};

        // examples
        System.out.println("Scalar int: " + multsuma(2,3,4));
        System.out.println("Scalar double: " + multsuma(2.5,3.0,1.2));

        // valid vector 
        int[] result1 = multsuma(A, B, C);
        printArray(result1);

        // invalid 
        int[] result2 = multsuma(A, B, invalid);
        printArray(result2);
    }
}