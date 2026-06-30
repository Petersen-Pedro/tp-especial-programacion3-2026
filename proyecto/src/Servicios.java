import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/*
 * Clase que centraliza la carga de datos desde archivos CSV y expone
 * tres servicios de consulta sobre los paquetes, cada uno optimizado
 * con la estructura de datos adecuada para su caso de uso.
 *
 * Estructuras utilizadas:
 *   - HashMap  → búsqueda O(1) por código de paquete (Servicio 1)
 *   - ArrayList → retorno O(1) de listas precargadas por tipo (Servicio 2)
 *   - TreeMap  → búsqueda O(log P + k) por rango de urgencia (Servicio 3)
 */
public class Servicios {

    // Servicio 1: búsqueda O(1) por código
    private HashMap<String, Paquete> paquetesPorCodigo;

    // Servicio 2: listas precargadas para O(1) al retornar
    private List<Paquete> paquetesConAlimentos;
    private List<Paquete> paquetesSinAlimentos;

    // Servicio 3: permite buscar por rango de urgencia eficientemente
    private TreeMap<Integer, List<Paquete>> paquetesPorUrgencia;

    private List<Camion> camiones;

    /*
     * Constructor principal.
     * Inicializa todas las estructuras de datos y carga los archivos CSV.
     * Complejidad temporal: O(C + P log P)
     * donde C = cantidad de camiones y P = cantidad de paquetes.
     * La inserción en TreeMap es O(log P) por elemento → O(P log P) en total.
     * HashMap y listas son O(P). Total: O(C + P log P).
     */
    public Servicios(String pathCamiones, String pathPaquetes) {
        paquetesPorCodigo = new HashMap<>();
        paquetesConAlimentos = new ArrayList<>();
        paquetesSinAlimentos = new ArrayList<>();
        paquetesPorUrgencia = new TreeMap<>();
        camiones = new ArrayList<>();

        cargarCamiones(pathCamiones);
        cargarPaquetes(pathPaquetes);
    }

    private void cargarCamiones(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int total = Integer.parseInt(br.readLine().trim());
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split(";");
                int id = Integer.parseInt(partes[0].trim());
                String patente = partes[1].trim();
                boolean refrigerado = partes[2].trim().equals("1");
                int capacidad = Integer.parseInt(partes[3].trim());
                camiones.add(new Camion(id, patente, refrigerado, capacidad));
            }
        } catch (IOException e) {
            System.out.println("Error al leer camiones: " + e.getMessage());
        }
    }

    private void cargarPaquetes(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int total = Integer.parseInt(br.readLine().trim());
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split(";");
                int id = Integer.parseInt(partes[0].trim());
                String codigo = partes[1].trim();
                int peso = Integer.parseInt(partes[2].trim());
                boolean alimentos = partes[3].trim().equals("1");
                int urgencia = Integer.parseInt(partes[4].trim());

                Paquete p = new Paquete(id, codigo, peso, alimentos, urgencia);

                paquetesPorCodigo.put(codigo, p);

                if (alimentos) {
                    paquetesConAlimentos.add(p);
                } else {
                    paquetesSinAlimentos.add(p);
                }

                paquetesPorUrgencia.computeIfAbsent(urgencia, k -> new ArrayList<>()).add(p);
            }
        } catch (IOException e) {
            System.out.println("Error al leer paquetes: " + e.getMessage());
        }
    }

    /*
     * Servicio 1: busca y retorna un paquete por su código alfanumérico.
     * Retorna null si el código no existe o si el parámetro es nulo o vacío.
     * Complejidad temporal: O(1) — búsqueda directa en HashMap por clave.
     */
    public Paquete servicio1(String codigoPaquete) {
        if (codigoPaquete == null || codigoPaquete.trim().isEmpty()) return null;
        return paquetesPorCodigo.get(codigoPaquete);
    }

    /*
     * Servicio 2: retorna todos los paquetes según si contienen alimentos o no.
     * Las listas están precargadas en el constructor, por lo que este metodo
     * Complejidad temporal: O(k) donde k es la cantidad de paquetes en la lista retornada.
     */
    public List<Paquete> servicio2(boolean contieneAlimentos) {
        List<Paquete> lista = contieneAlimentos ? paquetesConAlimentos : paquetesSinAlimentos;
        return new ArrayList<>(lista);
    }

    /*
     * Servicio 3: retorna todos los paquetes cuyo nivel de urgencia está dentro del rango dado.
     * Usa TreeMap.subMap() para acceder eficienteme
     * Si urgenciaMinima > urgenciaMaxima, retorna unnte solo a las claves del rango,
     * sin recorrer todos los paquetes.a lista vacía.
     * Complejidad temporal: O(log P + k), donde P = total de paquetes y k = paquetes en el rango.
     */
    public List<Paquete> servicio3(int urgenciaMinima, int urgenciaMaxima) {
        if (urgenciaMinima > urgenciaMaxima) return new ArrayList<>();

        List<Paquete> resultado = new ArrayList<>();
        NavigableMap<Integer, List<Paquete>> rango = paquetesPorUrgencia.subMap(
                urgenciaMinima, true, urgenciaMaxima, true
        );
        for (List<Paquete> lista : rango.values()) {
            resultado.addAll(lista);
        }
        return resultado;
    }

    public List<Camion> getCamiones() { return camiones; }

    public Collection<Paquete> getTodosLosPaquetes() { return paquetesPorCodigo.values(); }
}
