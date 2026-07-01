import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        CSVReader reader = new CSVReader();

        Backtracking backtracking = new Backtracking();
        List<Camion> camionesB = reader.leerCamiones("Camiones.csv");
        List<Paquete> paquetesB = reader.leerPaquetes("Paquetes.csv");
        List<Camion> camionesG = reader.leerCamiones("Camiones.csv");
        List<Paquete> paquetesG = reader.leerPaquetes("Paquetes.csv");
        Servicios servicios = new Servicios("Camiones.csv", "Paquetes.csv");


        // Servicio 1
        System.out.println("\n=== SERVICIO 1 - Búsqueda por código ===");
        System.out.println("P001: " + servicios.servicio1("P001"));
        System.out.println("P999: " + servicios.servicio1("P999"));

        // Servicio 2
        System.out.println("\n=== SERVICIO 2 - Paquetes con alimentos ===");
        List<Paquete> conAlimentos = servicios.servicio2(true);
        for (Paquete p : conAlimentos) {
            System.out.println("  " + p);
        }

        System.out.println("\n=== SERVICIO 2 - Paquetes sin alimentos ===");
        List<Paquete> sinAlimentos = servicios.servicio2(false);
        for (Paquete p : sinAlimentos) {
            System.out.println("  " + p);
        }

        // Servicio 3
        System.out.println("\n=== SERVICIO 3 - Urgencia entre 5 y 90 ===");
        List<Paquete> rangoUrgencia = servicios.servicio3(5, 90);
        for (Paquete p : rangoUrgencia) {
            System.out.println("  " + p);
        }



        System.out.println("# BACKTRACKING");
        Solucion solucion = backtracking.asignar(camionesB, paquetesB, true);
        System.out.println("estados: " + solucion.getMetrica());



        System.out.println("# GREEDY");
        Greedy greedy = new Greedy();
        Solucion solucionG = greedy.asignar(camionesG, paquetesG);
        System.out.println("estados: " + solucionG.getMetrica());


    }
}
