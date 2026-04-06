
import java.util.Objects;

public class Marcapasos {

    private final int idDispositivo;      // may be more than a short
    private final int codigoFabricante;   // may be more than a short
    private short latidosPorMinuto; // should be alarming passing 32.767 bpm (or worse, being below -32,768)
    private byte nivelBateria;      // wont excede 100 or go below 0

    private static int totalDispositivos = 0;

    // Class(es) Bob (constructor)
    public Marcapasos(int idDispositivo, int codigoFabricante,
            short latidosPorMinuto, byte nivelBateria) {

        validarBateria(nivelBateria);
        validarBpm(latidosPorMinuto);

        this.idDispositivo = idDispositivo;
        this.codigoFabricante = codigoFabricante;
        this.latidosPorMinuto = latidosPorMinuto;
        this.nivelBateria = nivelBateria;

        totalDispositivos++;   // static: pertenece a la clase, no a 'this'
    }

    // validaciones para no repetir
    private static void validarBateria(byte nivel) {
        if (nivel < 0 || nivel > 100) {
            throw new IllegalArgumentException(
                    "nivelBateria debe estar entre 0 y 100, recibido: " + nivel);
        }
    }

    private static void validarBpm(short bpm) {
        if (bpm <= 0) {
            throw new IllegalArgumentException(
                    "latidosPorMinuto debe ser positivo, recibido: " + bpm);
        }
    }

    // Getters
    public int getIdDispositivo() {
        return idDispositivo;
    }

    public int getCodigoFabricante() {
        return codigoFabricante;
    }

    public short getLatidosPorMinuto() {
        return latidosPorMinuto;
    }

    public byte getNivelBateria() {
        return nivelBateria;
    }

    // Getter del contador de clase
    public static int getTotalDispositivos() {
        return totalDispositivos;
    }

    /*
     * Sin setter para idDispositivo ni codigoFabricante porque son 'final':
     * representan la identidad física del dispositivo. Cambiarlos sería
     * equivalente a convertirlo en otro objeto — si se necesita otro id,
     * se crea una nueva instancia.
     */
    // Setters solo para atributos modificables
    public void setLatidosPorMinuto(short bpm) {
        validarBpm(bpm);
        this.latidosPorMinuto = bpm;
    }

    public void setNivelBateria(byte nivel) {
        validarBateria(nivel);
        this.nivelBateria = nivel;
    }

    // toString
    @Override
    public String toString() {
        return String.format(
                "Marcapasos { id=%d, fabricante=%d, bpm=%d, bateria=%d%% }",
                idDispositivo, codigoFabricante, latidosPorMinuto, nivelBateria);
    }

    // equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Marcapasos other = (Marcapasos) obj;
        return this.idDispositivo == other.idDispositivo
                && this.codigoFabricante == other.codigoFabricante;
    }

    // hashCode
    @Override
    public int hashCode() {
        return Objects.hash(idDispositivo, codigoFabricante);
    }

    /* --------------------------------------------
                    Ejemplo de main
       --------------------------------------------
     */

    public static void main(String[] args) {

        Marcapasos m1 = new Marcapasos(1001, 42, (short) 75, (byte) 85);
        Marcapasos m2 = new Marcapasos(1002, 42, (short) 60, (byte) 40);
        Marcapasos m3 = new Marcapasos(1001, 42, (short) 90, (byte) 10); // mismo id+fabricante que m1

        System.out.println(m1);
        System.out.println(m2);
        System.out.println(m3);

        System.out.println("\nm1.equals(m3): " + m1.equals(m3)); // true
        System.out.println("m1.equals(m2): " + m1.equals(m2)); // false

        System.out.println("\nTotal instancias: " + Marcapasos.getTotalDispositivos()); // 3

        // Probar validación
        try {
            Marcapasos malo = new Marcapasos(9, 9, (short) 70, (byte) 150); // typecast, bc java
        } catch (IllegalArgumentException e) {
            System.out.println("\nExcepcion capturada: " + e.getMessage());
        }
    }
}

/* 
Salida:

Marcapasos { id=1001, fabricante=42, bpm=75, bateria=85% }
Marcapasos { id=1002, fabricante=42, bpm=60, bateria=40% }
Marcapasos { id=1001, fabricante=42, bpm=90, bateria=10% }

m1.equals(m3): true
m1.equals(m2): false

Total instancias: 3

Excepcion capturada: nivelBateria debe estar entre 0 y 100, recibido: 150
*/
