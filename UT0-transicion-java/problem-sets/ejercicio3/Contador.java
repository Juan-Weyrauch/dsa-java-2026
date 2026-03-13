
import java.util.Scanner;

public class Contador {

    private static final int MAX_COUNT = 100;
    int Incremento = 0;
    int Contador = 0;

    public Contador(int incremento, int contador) {
        this.Incremento = incremento;
        this.Contador = contador;
    }

    public void increase() {
        Scanner typeOfOperation = new Scanner(System.in);
        int userInput = 0;

        while (userInput < 1 || userInput > 3) {
            System.out.println("Which loop do you want the system to use? (choose only one):");
            System.out.println("1) While");
            System.out.println("2) Do-While");
            System.out.println("3) For");
            userInput = typeOfOperation.nextInt();
        }

        switch (userInput) {
            case 1:
                while (this.Contador != this.MAX_COUNT) {
                    this.Contador++;
                    this.Incremento++;
                }

                break;
            case 2:
                do {
                    this.Contador++;
                    this.Incremento++;
                } while (this.Contador != this.MAX_COUNT);
                break;

            case 3:
                for (int i = 0; i != this.MAX_COUNT; i++) {
                    this.Contador++;
                }
                break;

            default:
                throw new Error("sad things happen");
        }

    }

    public static void main(String[] args) {
        Contador counter = new Contador(1, 0);
        counter.increase();
        System.out.println("Valor final del contador: " + counter.Contador);
    }

}


/*
# Diferencia entre un atributo static y uno de instancia:
    Los miembros estáticos pertenecen a la clase (MAX_COUNT en este caso).
    Los miembros de instancia pertenecen a cada objeto (Objeto: counter --> miembros de instancia: incremento & contador)
# Explicar en no más de diez líneas en qué casos usarías while, do-while y for.
    El loop while lo debes usar siempre que necesites repetir un proceso hasta cierto punto (que se cumpla una condición).
    El loop do-while lo debes usar usar siempre que necesites repetir un proceso hasta cierto punto, pero con la diferencia de que siempre 
    se va a ejecutar una vez antes de revisar la condición de finalización que le pongas.
    El loop for se utiliza cuando tengas un rango definido o para recorrer ciertas estructuras, también es posible con un ciclo while. 
 */
