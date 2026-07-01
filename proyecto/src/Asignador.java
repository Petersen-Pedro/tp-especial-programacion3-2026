import java.util.*;

public class Asignador {

    private List<Camion> camiones;
    private List<Paquete> paquetes;
    private int estadosBack;
    private int candidatosGreedy;
    private Solucion mejorSolucionGlobal;
    private int mejorPesoNoAsignado;
    private boolean podaActivada;

    public Asignador(List<Camion> camiones, Collection<Paquete> paquetes) {
        this.camiones = camiones;
        this.paquetes = new ArrayList<>(paquetes);
        this.mejorSolucionGlobal = null;
        this.mejorPesoNoAsignado = Integer.MAX_VALUE;
        this.podaActivada = true;
    }


    public void resolver() {
        // Estrategia Backtracking
        resetearCamiones();
        estadosBack = 0;
        mejorSolucionGlobal = null;
        mejorPesoNoAsignado = Integer.MAX_VALUE;


        resetearCamiones();

        candidatosGreedy = 0;
        Solucion solGreedy = greedy();
        if (solGreedy != null) {
            solGreedy.imprimir();
        }


    }

    /*
    1. Mantiene mejorPesoNoAsignado para poda eficiente
    2. Poda: si pesoNoAsignadoActual >= mejorPesoNoAsignado, descarta la rama
    3. Ordena paquetes por peso para mejorar la poda
    4. Estados generados cuenta cada nodo explorado
    */





    /*
     * * Al asignar primero los paquetes más pesados, se maximiza el uso
     * de la capacidad de los camiones. Esto reduce el espacio desperdiciado
     * y deja los paquetes livianos para espacios remanentes, minimizando
     * el peso total que queda sin asignar.
     *
     * CRITERIO DE ASIGNACIÓN (Best-Fit):
     * Para cada paquete, se selecciona el camión que, tras la asignación,
     * quede con el MENOR espacio remanente posible. Esto minimiza el
     * desperdicio de capacidad en cada camión.
     *
     * Complejidad: O(P log P + P * C), donde P = paquetes y C = camiones.
     */
    private Solucion greedy() {
        List<Paquete> ordenados = new ArrayList<>(paquetes);
        ordenados.sort((a, b) -> b.getPesoKg() - a.getPesoKg());

        Solucion solucion = new Solucion(camiones);

        for (Paquete p : ordenados) {
            Camion mejor = null;
            int menorEspacioRestante = Integer.MAX_VALUE;
            for (Camion c : camiones) {
                candidatosGreedy++;
                if (c.puedeTransportar(p)) {
                    int espacioRestante = ((c.getCapacidadKg() -
                            c.getPesoActual()) -
                            p.getPesoKg()
                    );
                    if (espacioRestante < menorEspacioRestante) {
                        menorEspacioRestante = espacioRestante;
                        mejor = c;
                    }
                }
            }

            if (mejor != null) {
                mejor.asignarPaquete(p);
                solucion.getAsignaciones().get(mejor).add(p);
            } else {
                solucion.agregarNoAsignado(p);
            }

        }
        return solucion;
    }


    private void resetearCamiones() {
        for (Camion c : camiones) {
            c.resetear();
        }
    }
}
