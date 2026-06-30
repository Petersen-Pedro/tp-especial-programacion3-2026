import java.util.*;

public class Asignador {

    private List<Camion> camiones;
    private List<Paquete> paquetes;
    private int estadosBack;
    private int candidatosGreedy;
    private Solucion mejorSolucionGlobal;

    public Asignador(List<Camion> camiones, Collection<Paquete> paquetes) {
        this.camiones = camiones;
        this.paquetes = new ArrayList<>(paquetes);
        this.mejorSolucionGlobal = null;
    }

    /*
     * Ejecuta ambas estrategias de resolución en secuencia e imprime los resultados.
     * Primero corre el Backtracking y muestra la solución óptima junto con la cantidad
     * de estados explorados. Luego resetea los camiones y corre el Greedy,
     * mostrando su solución y la cantidad de candidatos evaluados.
     */
    public void resolver() {
        System.out.println("=== Estrategia Backtracking ===");
        resetearCamiones();
        estadosBack = 0;
        mejorSolucionGlobal = null;

        // Llamar al backtracking con los parámetros adicionales
        Solucion mejorBT = backtracking(0, new ArrayList<>(), 0);
        //Solucion mejorBT = backtracking(0, new ArrayList<>());
        mejorBT.imprimir();
        System.out.println("Cantidad de estados generados: " + estadosBack);



        resetearCamiones();

        System.out.println("\n=== Estrategia Greedy ===");
        candidatosGreedy = 0;
        Solucion solGreedy = greedy();
        solGreedy.imprimir();
        System.out.println("Candidatos considerados: " + candidatosGreedy);

    }

    /*
     * Recorre los paquetes uno por uno (por índice). Para cada paquete prueba
     * todas las opciones posibles: asignarlo a cada camión válido, o dejarlo sin asignar.
     * El estado se mantiene directamente en los objetos Camion (asignar/desasignar),
     * y al llegar a una hoja del árbol se toma una instancia del estado actual
     * como Solucion. Siempre se conserva la solución con menor peso no asignado.
     *
     * Poda: si se encuentra una solución con peso no asignado igual a 0,
     * se retorna de inmediato ya que no puede existir un resultado mejor.
     *
     * Complejidad: O((C+1)^P) en el peor caso, donde C = camiones y P = paquetes.
     */
    private Solucion backtracking(int indicePaquete,
                                  List<Paquete> noAsignadosActual,
                                  int pesoNoAsignadoActual) {
        estadosBack++;

        // Poda: si el peso actual ya supera la mejor solución, descartar esta rama
        if (mejorSolucionGlobal != null &&
                pesoNoAsignadoActual >= mejorSolucionGlobal.getPesoNoAsignado()) {
            return null;
        }


        if (indicePaquete == paquetes.size()) {
            // Construir solución hoja
            Solucion sol = new Solucion(camiones);
            for (Camion c : camiones) {
                sol.getAsignaciones().put(c, new ArrayList<>(c.getPaquetesAsignados()));
            }
            for (Paquete p : noAsignadosActual) {
                sol.agregarNoAsignado(p);
            }

            // Actualizar mejor solución global
            if (mejorSolucionGlobal == null || sol.esMejorQue(mejorSolucionGlobal)) {
                mejorSolucionGlobal = sol;
            }

            return sol;
        }

        Paquete actual = paquetes.get(indicePaquete);
        Solucion mejorLocal = null;

        // Ordenar camiones por capacidad restante (mayor primero)
        // Esto ayuda a encontrar buenas soluciones temprano
        List<Camion> camionesOrdenados = new ArrayList<>(camiones);
        camionesOrdenados.sort((a, b) -> {
            int restoA = a.getCapacidadKg() - a.getPesoActual();
            int restoB = b.getCapacidadKg() - b.getPesoActual();
            return Integer.compare(restoB, restoA);
        });

        // Opción 1: intentar asignar a cada camión válido
        for (Camion c : camionesOrdenados) {
            if (c.puedeAsignar(actual)) {
                c.asignar(actual);
                Solucion resultado = backtracking(indicePaquete + 1,
                        noAsignadosActual,
                        pesoNoAsignadoActual);
                c.desasignar(actual); // backtrack

                if (resultado != null && (mejorLocal == null || resultado.esMejorQue(mejorLocal))) {
                    mejorLocal = resultado;
                }

                // Poda: si encontramos solución óptima, retornar
                if (mejorLocal != null && mejorLocal.getPesoNoAsignado() == 0) {
                    return mejorLocal;
                }
            }
        }

        // Opción 2: dejar el paquete sin asignar
        noAsignadosActual.add(actual);
        Solucion sinAsignar = backtracking(indicePaquete + 1,
                noAsignadosActual,
                pesoNoAsignadoActual + actual.getPesoKg());
        noAsignadosActual.remove(noAsignadosActual.size() - 1);

        if (sinAsignar != null && (mejorLocal == null || sinAsignar.esMejorQue(mejorLocal))) {
            mejorLocal = sinAsignar;
        }

        return mejorLocal;
    }

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
                if (c.puedeAsignar(p)) {
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
                mejor.asignar(p);
                solucion.getAsignaciones().get(mejor).add(p);
            } else {
                solucion.agregarNoAsignado(p);
            }

        }
        return solucion;
    }

        /*
        List<Paquete> ordenados = new ArrayList<>(paquetes);
        ordenados.sort((a, b) -> b.getNivelUrgencia() - a.getNivelUrgencia());

        Solucion sol = new Solucion(camiones);

        for (Paquete p : ordenados) {
            Camion mejor = null;
            int menorEspacioRestante = Integer.MAX_VALUE;

            // Buscar el camión válido con menor espacio restante tras asignar el paquete
            for (Camion c : camiones) {
                candidatosGreedy++;
                if (c.puedeAsignar(p)) {
                    int espacioRestante = c.getCapacidadKg() - c.getPesoActual() - p.getPesoKg();
                    if (espacioRestante < menorEspacioRestante) {
                        menorEspacioRestante = espacioRestante;
                        mejor = c;
                    }
                }
            }

            if (mejor != null) {
                mejor.asignar(p);
                sol.getAsignaciones().get(mejor).add(p);
            } else {
                sol.getNoAsignados().add(p);
            }
        }

        return sol;


         */


    private void resetearCamiones() {
        for (Camion c : camiones) {
            c.resetear();
        }
    }
}
