import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Greedy {




    public Solucion asignar(
            List<Camion> camiones,
            List<Paquete> paquetes,
            boolean ordenar,
            boolean poda
    ) {

        paquetes.sort((ordenar) ? (p1, p2) -> Integer.compare(p2.getPesoKg(), p1.getPesoKg()) : (p1, p2) -> 0);

        HashMap<Camion, List<Paquete>> asignacionActual = new HashMap<>();
        for (Camion c : camiones) {
            asignacionActual.put(c, new ArrayList<>());
        }

        int pesoNoAsignado = 0;
        int estados = 0;

        for (Paquete p : paquetes) {

            Camion camionSeleccionado = null;
            int menorEspacioLibre = Integer.MAX_VALUE;

            for (Camion c : camiones) {

                estados++;

                if (c.puedeTransportar(p)) {

                    int espacioLibre = c.getCapacidadKg() - (c.getPesoActual() + p.getPesoKg());

                    if (espacioLibre < menorEspacioLibre) {
                        menorEspacioLibre = espacioLibre;
                        camionSeleccionado = c;
                    }
                }
            }

            if (camionSeleccionado != null) {
                camionSeleccionado.asignarPaquete(p);
                asignacionActual.get(camionSeleccionado).add(p);
            } else {
                pesoNoAsignado += p.getPesoKg();
            }
        }


        return new Solucion(asignacionActual, pesoNoAsignado, estados);
    }
}
