/* Ejercicio 7

Implementa en Java el algoritmo del ejercicio anterior, usando un contador para contar
cuántas veces se va a invocar la sentencia Si. El programa debe:
Leer de un archivo numeros.txt cuya primera línea será la cantidad de números a leer
(sea N) y las siguientes N líneas contendrán los elementos de entrada del algoritmo, uno
por cada línea.

El programa debe mostrar:
• El valor de N
• El contenido del contador
• La cantidad de intercambios realizados
• El primer y el último elemento del arreglo resultante
*/

package uy.edu.ucu.aed.parcial1;

import uy.edu.ucu.aed.utils.ManejadorArchivosGenerico;

public class bubble {

    public static void main(String[] args) {
        String[] lineas = ManejadorArchivosGenerico.leerArchivo("numeros.txt");

        int N = Integer.parseInt(lineas[0]);
        int[] arreglo = new int[N];

        for (int i = 0; i < N; i++) {
            arreglo[i] = Integer.parseInt(lineas[i + 1]);
        }

        int contadorIf = 0;
        int intercambios = 0;

        for (int i = 0; i < N - 1; i++) {
            for (int j = N; j == (i + 1); j--) {

                contadorIf++;

                if (arreglo[j] < arreglo[j - 1]) {
                    int aux = arreglo[j];
                    arreglo[j] = arreglo[j - 1];
                    arreglo[j - 1] = aux;

                    intercambios++;
                }
            }
        }

        // resultados:
        System.out.println("N: " + N);
        System.out.println("Cantidad de veces que se evalúa el if: " + contadorIf);
        System.out.println("Cantidad de intercambios: " + intercambios);
        System.out.println("Primer elemento: " + arreglo[0]);
        System.out.println("Último elemento: " + arreglo[N - 1]);
    }
}
