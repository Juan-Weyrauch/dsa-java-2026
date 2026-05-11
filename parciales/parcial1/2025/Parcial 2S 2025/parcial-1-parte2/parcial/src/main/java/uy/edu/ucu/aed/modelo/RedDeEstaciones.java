package uy.edu.ucu.aed.modelo;

import uy.edu.ucu.aed.tdas.ArbolAVL;
import uy.edu.ucu.aed.tdas.IElementoAB;

/**
 * Red de estaciones de aterrizaje organizada como un árbol AVL.
 * La etiqueta de cada nodo es el id de la estación (su señal),
 * lo que garantiza el ordenamiento numérico requerido por el enunciado.
 */
public class RedDeEstaciones {

    public enum EstadoExploracion {
        CompletaRuta,
        FallaNavegacion,
        BateriaAgotada
    }

    private final ArbolAVL<Estacion> red;

    public RedDeEstaciones() {
        this.red = new ArbolAVL<>();
    }

    /**
     * Carga una lista de IDs en la red, insertando cada uno como estación.
     *
     * @param ids arreglo de enteros con los IDs de señal a insertar
     */
    public void cargar(int[] ids) {
        for (int id : ids) {
            Estacion estacion = new Estacion(id);
            red.insertar(id, estacion);
        }
    }

    /**
     * Inicia la exploración de la red desde la raíz con el dron dado.
     *
     * @param dron el dron que realiza la exploración
     * @return el estado final de la exploración
     */
    public EstadoExploracion explorar(Dron dron) {
        return explorarRecursivo(dron, red.getRaiz());
    }

    /**
     * Recorre la red en pre-order aplicando las condiciones operativas en cada nodo.
     * Pre-order porque el enunciado establece que el dron procesa su estación actual
     * antes de desplazarse hacia las conexiones.
     *
     * PRECONDICIONES:
     *   - dron != null, dron.getBateria() > 0, dron.getMaxConsecutivas() > 0
     *   - nodoActual puede ser null (árbol vacío o subárbol agotado)
     *
     * POSTCONDICIONES:
     *   - Retorna exactamente uno de: CompletaRuta, FallaNavegacion, BateriaAgotada
     *   - dron.getVisitadas() contiene los IDs procesados hasta el punto de retorno
     *   - dron.getBateria() y dron.getConsecutivasSinRecarga() reflejan el estado al retorno
     *
     * @param dron       el dron que realiza la exploración
     * @param nodoActual el nodo AVL actual del recorrido
     * @return el estado de la exploración
     */
    private EstadoExploracion explorarRecursivo(Dron dron, IElementoAB<Estacion> nodoActual) {

        // Caso base: subárbol agotado
        if (nodoActual == null) {
            return EstadoExploracion.CompletaRuta;
        }

        Estacion estacion = nodoActual.getDatos();

        // Estación ya visitada: no consumir energía, no contar consecutivo, bajar directo
        if (dron.yaVisitada(estacion.getId())) {
            EstadoExploracion resultado = explorarRecursivo(dron, nodoActual.getHijoIzq());
            if (resultado != EstadoExploracion.CompletaRuta) {
                return resultado;
            }
            return explorarRecursivo(dron, nodoActual.getHijoDer());
        }

        // Estación no visitada: consumir energía por desplazamiento
        dron.consumirEnergia();

        // Evaluar condición operativa (PRE-ORDER: se procesa antes de bajar)
        Estacion.CondicionOperativa condicion = estacion.getCondicion();

        if (condicion == Estacion.CondicionOperativa.CRITICO) {
            return EstadoExploracion.FallaNavegacion;
        }

        if (condicion == Estacion.CondicionOperativa.OPTIMO) {
            dron.recargar();
            dron.resetConsecutivas();
        }

        if (condicion == Estacion.CondicionOperativa.SUBOPTIMO) {
            dron.incrementarConsecutivas();
            if (dron.getConsecutivasSinRecarga() > dron.getMaxConsecutivas()) {
                return EstadoExploracion.BateriaAgotada;
            }
        }

        dron.marcarVisitada(estacion.getId());

        // Descender a hijos
        EstadoExploracion resultado = explorarRecursivo(dron, nodoActual.getHijoIzq());
        if (resultado != EstadoExploracion.CompletaRuta) {
            return resultado;
        }

        return explorarRecursivo(dron, nodoActual.getHijoDer());
    }

    public ArbolAVL<Estacion> getRed() {
        return red;
    }
}
