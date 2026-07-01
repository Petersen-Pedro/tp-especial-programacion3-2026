import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Greedy {




    public Solucion asignar(
                            List<Camion> camiones,
                            List<Paquete> paquetes
                            ) {
        paquetes.sort((p1, p2) -> Integer.compare(p2.getPesoKg(), p1.getPesoKg()));

        HashMap<Camion, List<Paquete>> asignacion = new HashMap<>();
        for (Camion c : camiones) {
            asignacion.put(c, new ArrayList<>());
        }

        int pesoNoAsignado = 0;
        int estados = 0;

        for (Paquete p : paquetes) {

            Camion camionMejor = null;
            int menorEspacioDisponible = Integer.MAX_VALUE;

            for (Camion c : camiones) {
                estados++;


                if (c.puedeTransportar(p)){
                    int espacioDisponible = (c.getCapacidadKg() - c.getPesoActual()) - p.getPesoKg();
                    if (espacioDisponible < menorEspacioDisponible) {
                        menorEspacioDisponible = espacioDisponible;
                        camionMejor = c;
                    }
                }
            }

            if (camionMejor != null) {
                camionMejor.asignarPaquete(p);
                asignacion.get(camionMejor).add(p);
            } else {
                pesoNoAsignado += p.getPesoKg();
            }
        }

        return new Solucion(asignacion, pesoNoAsignado, estados);
    }
}
