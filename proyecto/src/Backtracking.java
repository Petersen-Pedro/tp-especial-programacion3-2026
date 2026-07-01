import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Backtracking {

    private Solucion solucion;
    private int mejorPesoNoAsignado;
    private boolean poda;
    private long estados;



    /*
    La estrategia consiste en un arbol donde en cada nivel se
    representa un paquete, y en cada rama una asignacion de
    camiones disponibles.

    Se recorren los paquetes en busqueda de un camiones a los
    que se les pueda asignar, contruyendo posibles soluciones
    que son comparadas entre si hasta encontrar la mejor solucion
    posible.

    La poda consiste en descartasr las ramas que acumulen un
    peso no asignado mayor o igual al de la mejor solucion
    encontrada.


     */

    public Solucion asignar(
                            List<Camion> camiones,
                            List<Paquete> paquetes,
                            boolean ordenar
                            ) {

        this.mejorPesoNoAsignado = Integer.MAX_VALUE;
        this.estados = 0;
        this.solucion = null;

        if (ordenar) {
            paquetes.sort((p1, p2) -> Integer.compare(p2.getPesoKg(), p1.getPesoKg()));
        }

        HashMap<Camion, List<Paquete>> asignacion = new HashMap<>();

        for (Camion c :  camiones) {
            asignacion.put(c, new ArrayList<>());
        }

        backtracking(0, paquetes, camiones, asignacion, 0);

        return this.solucion;
    }

    private void backtracking(int index, List<Paquete> paquetes, List<Camion> camiones, HashMap<Camion, List<Paquete>> asignacionActual, int pesoNoAsignado) {

        estados++;

        if (index == paquetes.size()) {
            if (pesoNoAsignado < mejorPesoNoAsignado) {
                mejorPesoNoAsignado = pesoNoAsignado;
                solucion = new Solucion(copiarAsignacion(asignacionActual), pesoNoAsignado, estados);
            }
        } else {
            if ((pesoNoAsignado < mejorPesoNoAsignado)) {
                Paquete p = paquetes.get(index);

                for (Camion c : camiones) {
                    if (c.puedeTransportar(p)) {
                        c.asignarPaquete(p);
                        asignacionActual.get(c).add(p);

                        backtracking(index + 1, paquetes, camiones, asignacionActual, pesoNoAsignado);

                        List<Paquete> paquetesAsignados = asignacionActual.get(c);
                        paquetesAsignados.remove(paquetesAsignados.size() - 1);
                        c.quitarPaquete(p);
                    }
                }
                backtracking(index + 1, paquetes, camiones, asignacionActual, pesoNoAsignado + p.getPesoKg());
            }
        }
    }

    private HashMap<Camion, List<Paquete>> copiarAsignacion(HashMap<Camion, List<Paquete>> asignacionActual) {
        HashMap<Camion, List<Paquete>> copiar = new HashMap<>();
        for (Camion c : asignacionActual.keySet()) {
            copiar.put(c, asignacionActual.get(c));
        }
        return copiar;
    }
}

