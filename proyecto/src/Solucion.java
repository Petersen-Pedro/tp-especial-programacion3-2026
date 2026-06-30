import java.util.*;

public class Solucion {
    private Map<Camion, List<Paquete>> asignaciones;
    private List<Paquete> noAsignados;
    private int pesoNoAsignado;

    public Solucion(List<Camion> camiones) {
        asignaciones = new LinkedHashMap<>();
        for (Camion c : camiones) {
            asignaciones.put(c, new ArrayList<>());
        }
        noAsignados = new ArrayList<>();
        pesoNoAsignado = 0;
    }

    public Solucion(Solucion otra) {
        asignaciones = new LinkedHashMap<>();
        for (Map.Entry<Camion, List<Paquete>> entry : otra.asignaciones.entrySet()) {
            asignaciones.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        noAsignados = new ArrayList<>(otra.noAsignados);
        pesoNoAsignado = otra.pesoNoAsignado;
    }

    public void agregarNoAsignado(Paquete paquete) {
        noAsignados.add(paquete);
        pesoNoAsignado += paquete.getPesoKg(); // Mantenemos actualizado
    }

    public void removerNoAsignado(Paquete paquete) {
        noAsignados.remove(paquete);
        pesoNoAsignado -= paquete.getPesoKg(); // Mantener actualizado
    }


    // O(1) en lugar de O(n)
    public int getPesoNoAsignado() {
        return pesoNoAsignado;
    }

    /*
     * Complejidad: O(n) donde n es la cantidad de paquetes no asignados.
    public int getPesoNoAsignado() {
        return noAsignados.stream().mapToInt(Paquete::getPesoKg).sum();
    }
     */

    public void imprimir() {
        for (Map.Entry<Camion, List<Paquete>> entry : asignaciones.entrySet()) {
            Camion c = entry.getKey();
            System.out.println("Camion " + c.getId() + " (" + c.getPatente() + "): " + entry.getValue());
        }
        System.out.println("Paquetes no asignados: " + noAsignados);
        System.out.println("Peso no asignado: " + getPesoNoAsignado() + " kg.");
    }
    public boolean esMejorQue(Solucion otra) {
        if (otra == null) return true;
        return this.getPesoNoAsignado() < otra.getPesoNoAsignado();
    }

    public List<Paquete> getNoAsignados() { return noAsignados; }

    public Map<Camion, List<Paquete>> getAsignaciones() { return asignaciones; }
}
