
public class Marcapasos {

    private final int idDispositivo;      // may be more than a short
    private final int codigoFabricante;   // may be more than a short
    private short latidosPorMinuto; // should be alarming passing 32.767 bpm (or worse, being below -32,768)
    private byte nivelBateria;      // wont excede 100 or go below 0

    private static int totalDispositivos = 0;

    // Class(es) Bob (constructor)
    public Marcapasos(int id, int code, short bpm, byte battery) {
        if (battery < 0 || battery > 100) {
            throw new Error("Incorrect battery level ");
        }
        this.idDispositivo = id;
        this.codigoFabricante = code;
        this.latidosPorMinuto = bpm;
        this.nivelBateria = battery;

        totalDispositivos++; // check why this.totalDispositivos doesn't work (static stuff).
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

    // Setters solo para atributos modificables
    public void setLatidosPorMinuto(short bpm) {
        this.latidosPorMinuto = bpm;
    }

    public void setNivelBateria(byte battery) {

        if (battery < 0 || battery > 100) {
            throw new Error("Incorrect battery level ");
        }

        this.nivelBateria = battery;
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
        int result = Integer.hashCode(idDispositivo);
        result = 31 * result + Integer.hashCode(codigoFabricante);
        return result;
    }

}
