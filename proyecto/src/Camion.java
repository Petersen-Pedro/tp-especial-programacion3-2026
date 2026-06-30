import java.util.ArrayList;
import java.util.List;

public class Camion {
    private int id;
    private String patente;
    private boolean estaRefrigerado;
    private int capacidadKg;
    private List<Paquete> paquetesAsignados;
    private int pesoActual;

    public Camion(int id, String patente, boolean estaRefrigerado, int capacidadKg) {
        this.id = id;
        this.patente = patente;
        this.estaRefrigerado = estaRefrigerado;
        this.capacidadKg = capacidadKg;
        this.paquetesAsignados = new ArrayList<>();
        this.pesoActual = 0;
    }

    public int getId() { return id; }

    public String getPatente() { return patente; }

    public boolean estaRefrigerado() { return estaRefrigerado; }

    public int getCapacidadKg() { return capacidadKg; }

    public int getPesoActual() { return pesoActual; }

    public List<Paquete> getPaquetesAsignados() { return paquetesAsignados; }

    /*
     * Verifica si el paquete puede ser asignado a este camión.
     * Se aplican dos restricciones:
     *   1. Si el paquete contiene alimentos, el camión debe estar refrigerado.
     *   2. El peso del paquete más el peso actual no puede superar la capacidad máxima.
     */
    public boolean puedeAsignar(Paquete p) {
        if (p.contieneAlimentos() && !this.estaRefrigerado) return false;
        return pesoActual + p.getPesoKg() <= capacidadKg;
    }

    public void asignar(Paquete p) {
        paquetesAsignados.add(p);
        pesoActual += p.getPesoKg();
    }

    public void desasignar(Paquete p) {
        paquetesAsignados.remove(p);
        pesoActual -= p.getPesoKg();
    }

    public void resetear() {
        paquetesAsignados.clear();
        pesoActual = 0;
    }

    @Override
    public String toString() {
        return "Camion{id=" + id + ", patente='" + patente + "', refrigerado=" + estaRefrigerado +
               ", capacidad=" + capacidadKg + "kg, pesoActual=" + pesoActual + "kg}";
    }
}
