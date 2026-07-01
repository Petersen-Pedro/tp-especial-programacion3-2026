import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Servicios {

    /// Servicio 1: búsqueda O(1) por código
    private HashMap<String, Paquete> paquetesPorCodigo;

    /// Servicio 2: listas precargadas para O(1) al retornar
    private List<Paquete> paquetesConAlimentos;
    private List<Paquete> paquetesSinAlimentos;

    /// Servicio 3: permite buscar por rango de urgencia eficientemente
    private ArrayList<Paquete>[] paquetesPorUrgencia;

    private List<Camion> camiones;

    ///  Complejidad O(p+c) donde p = cantidad de paquetes y c = cantidad de camiones
    public Servicios(String pathCamiones, String pathPaquetes) {

        try {

            CSVReader reader = new CSVReader();

            ///O(p)
            List<Paquete> paquetes = reader.leerPaquetes(pathPaquetes);

            ///O(c)
            this.camiones =  reader.leerCamiones(pathCamiones);

            ///O(1)
            inicializarEstructuras();

            ///O(p)
            indexarPaquetes(paquetes);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

    private void inicializarEstructuras() {
        paquetesPorCodigo = new HashMap<>();
        paquetesConAlimentos = new ArrayList<>();
        paquetesSinAlimentos = new ArrayList<>();
        paquetesPorUrgencia = new ArrayList[101];

        for (int i = 0; i <= 100; i++) {
            paquetesPorUrgencia[i] = new ArrayList<>();
        }
    }

    private void indexarPaquetes(List<Paquete> paquetes) {

        for (Paquete p : paquetes) {

            paquetesPorCodigo.put(p.getCodigo(),p);

            if (p.contieneAlimentos()) {
                paquetesConAlimentos.add(p);
            } else {
                paquetesSinAlimentos.add(p);
            }

            paquetesPorUrgencia[p.getNivelUrgencia()].add(p);
        }
    }

    /// Complejidad O(1)
    /// Se accede en O(1) a través del HashMap
    public Paquete servicio1(String codigoPaquete) {
        if (codigoPaquete == null || codigoPaquete.trim().isEmpty()) {
            return null;
        }
        return paquetesPorCodigo.get(codigoPaquete);
    }

    /// Complejidad O(1)
    /// Se lleva el control de dos listas donde son almacenados los paquetes con y sin alimentos
    public List<Paquete> servicio2(boolean contieneAlimentos) {
        if (contieneAlimentos){
            return this.paquetesConAlimentos;
        }
        return this.paquetesSinAlimentos;
    }

    /// Complejidad O(p) donde p es el numero de paquetes dentro del rango
    public List<Paquete> servicio3(int urgenciaMinima, int urgenciaMaxima) {
        List<Paquete> resultado = new ArrayList<>();
        for(int i = urgenciaMinima; i <= urgenciaMaxima; i++){
            resultado.addAll(paquetesPorUrgencia[i]);
        }
        return resultado;
    }
}
