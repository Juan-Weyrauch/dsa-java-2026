

public class PruebaAtributos {

    // ----- Atributos de instancia -----
    int age;                // default: 0
    boolean subscribed;     // default: false
    double weight;          // default: 0.0
    char grade;             // default: '\u0000'
    String name;            // default: null


    public static void main(String[] args) {

        PruebaAtributos obj = new PruebaAtributos();

        // default values:
        System.out.println("Valores por defecto de los atributos:");
        System.out.println("age: " + obj.age);
        System.out.println("subscribed: " + obj.subscribed);
        System.out.println("weight: " + obj.weight);
        System.out.println("grade: " + obj.grade);
        System.out.println("name: " + obj.name);

        System.out.println();

        // local var:
        int localInt;
        double localDouble;

        // Esto generaría error si se descomenta
        // System.out.println(localInt);

        // Inicialización obligatoria
        localInt = 10;
        localDouble = 2.5;

        System.out.println("Variables locales inicializadas:");
        System.out.println("localInt: " + localInt);
        System.out.println("localDouble: " + localDouble);
    }
}

