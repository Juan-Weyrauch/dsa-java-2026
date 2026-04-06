public class StringsDemo {

    public static void main(String[] args) {

        // Base dada
        String hannah = "Did Hannah see bees? Hannah did.";
        String frase = "Anita lava la tina";

        System.out.println("=== PRUEBAS DE PALINDROMOS ===");

        probarPalindromo(frase);
        probarPalindromo("Anita, lava la tina!");
        probarPalindromo("Acaso hubo buhos aca?");
        probarPalindromo("Hola mundo");

        System.out.println("\n=== METODOS DE STRING ===");

        String texto = "  Hola Mundo Java  ";

        System.out.println("Original: '" + texto + "'");

        System.out.println("trim(): '" + texto.trim() + "'");
        System.out.println("toLowerCase(): " + texto.toLowerCase());
        System.out.println("toUpperCase(): " + texto.toUpperCase());

        System.out.println("substring(2, 6): " + texto.substring(2, 6));
        System.out.println("subSequence(2, 6): " + texto.subSequence(2, 6));

        String ejemploSplit = "uno,dos,tres";
        String[] partes = ejemploSplit.split(",");
        System.out.println("split(): ");
        for (String p : partes) {
            System.out.println(" - " + p);
        }

        System.out.println("indexOf('M'): " + texto.indexOf("M"));
        System.out.println("lastIndexOf('a'): " + texto.lastIndexOf("a"));

        System.out.println("contains('Mundo'): " + texto.contains("Mundo"));

        System.out.println("replace('o','0'): " + texto.replace('o', '0'));
        System.out.println("replaceAll('a','@'): " + texto.replaceAll("a", "@"));
        System.out.println("replaceFirst('a','@'): " + texto.replaceFirst("a", "@"));

        System.out.println("\n=== MICROCONSULTAS CON HANNAH ===");

        System.out.println("Texto: " + hannah);
        System.out.println("Longitud: " + hannah.length());
        System.out.println("Caracter en indice 5: " + hannah.charAt(5));
        System.out.println("Substring(4, 10): " + hannah.substring(4, 10));
    }

    // Método simple para verificar palíndromos
    public static void probarPalindromo(String texto) {

        String limpio = texto.toLowerCase()
                .replaceAll("[^a-záéíóúüñ]", ""); // [^a-záéíóúüñ] es "cualquier cosa que NO sea una letra" (de a a z, vocales con tilde)

        String invertido = new StringBuilder(limpio).reverse().toString();

        boolean esPalindromo = limpio.equals(invertido);

        System.out.println("Frase: " + texto);
        System.out.println("Limpio: " + limpio);
        System.out.println("Invertido: " + invertido);
        System.out.println("Es palindromo?: " + esPalindromo);
        System.out.println("--------------------------------");
    }
}