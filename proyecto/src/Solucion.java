import java.util.*;

public class Solucion {
    private HashMap<Camion,List<Paquete>> asignaciones;
    private int pesoNoAsignado;
    private long metrica;

    public Solucion( HashMap<Camion,List<Paquete>>asignaciones,int pesoNoAsignado,long metrica) {
        this.asignaciones = asignaciones;
        this.pesoNoAsignado = pesoNoAsignado;
        this.metrica = metrica;
    }

    public int getPesoNoAsignado() {
        return pesoNoAsignado;
    }
    public long getMetrica() {
        return metrica;
    }


}
