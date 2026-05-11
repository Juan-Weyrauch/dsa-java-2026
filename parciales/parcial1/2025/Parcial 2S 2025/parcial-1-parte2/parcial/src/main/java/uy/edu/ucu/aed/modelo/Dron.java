package uy.edu.ucu.aed.modelo;

import java.util.LinkedList;
import java.util.List;

/**
 * Representa el dron de reparto.
 * Mantiene el estado de batería, historial de visitas
 * y contador de estaciones consecutivas sin recarga.
 */
public class Dron {

    private int bateria;
    private final int maxConsecutivas;
    private int consecutivasSinRecarga;
    private final List<Integer> visitadas;

    public static final int COSTO_ENERGIA = 10;
    public static final int CANTIDAD_RECARGA = 30;

    public Dron(int bateriaInicial, int maxConsecutivas) {
        this.bateria = bateriaInicial;
        this.maxConsecutivas = maxConsecutivas;
        this.consecutivasSinRecarga = 0;
        this.visitadas = new LinkedList<>();
    }

    public int getBateria() {
        return bateria;
    }

    public int getConsecutivasSinRecarga() {
        return consecutivasSinRecarga;
    }

    public int getMaxConsecutivas() {
        return maxConsecutivas;
    }

    public boolean yaVisitada(int id) {
        return visitadas.contains(id);
    }

    public void marcarVisitada(int id) {
        visitadas.add(id);
    }

    public void consumirEnergia() {
        bateria -= COSTO_ENERGIA;
    }

    public void recargar() {
        bateria += CANTIDAD_RECARGA;
    }

    public void resetConsecutivas() {
        consecutivasSinRecarga = 0;
    }

    public void incrementarConsecutivas() {
        consecutivasSinRecarga++;
    }

    public List<Integer> getVisitadas() {
        return visitadas;
    }

    @Override
    public String toString() {
        return "Dron{bateria=" + bateria
                + ", consecutivas=" + consecutivasSinRecarga
                + ", visitadas=" + visitadas + "}";
    }
}
