public class Main {

    public static void main(String[] args) {
        Servicios servicios = new Servicios("Camiones.csv", "Paquetes.csv");

        // --- Primera Parte: servicios de consulta ---

        System.out.println("=== Servicio 1 ===");
        System.out.println(servicios.servicio1("P001")); // paquete existente
        System.out.println(servicios.servicio1("P999")); // null esperado

        System.out.println("\n=== Servicio 2 - Con alimentos ===");
        servicios.servicio2(true).forEach(System.out::println);

        System.out.println("\n=== Servicio 2 - Sin alimentos ===");
        servicios.servicio2(false).forEach(System.out::println);

        System.out.println("\n=== Servicio 3 - Urgencia 5 a 90 ===");
        servicios.servicio3(5, 90).forEach(System.out::println);

        // --- Segunda Parte: asignación de paquetes a camiones ---

        System.out.println("\n");
        Asignador asignador = new Asignador(servicios.getCamiones(), servicios.getTodosLosPaquetes());
        asignador.resolver();
    }
}
