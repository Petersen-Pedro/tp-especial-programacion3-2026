import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public List<Camion> leerCamiones(String path) throws IOException {

        List<Camion> camiones = new ArrayList<Camion>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            int cantidadEsperada = Integer.parseInt(br.readLine());

            String linea;

            while ((linea = br.readLine()) != null) {
                camiones.add(parsearCamion(linea));
            }

            if (camiones.size() != cantidadEsperada) {
                throw new RuntimeException(
                        "Error en cantidad de camiones");
            }
        }
        return camiones;
    }

    public List<Paquete> leerPaquetes(String path) throws IOException {

        List<Paquete> paquetes = new ArrayList<Paquete>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            int cantidadEsperada = Integer.parseInt(br.readLine());

            String linea;

            while ((linea = br.readLine()) != null) {
                paquetes.add(parsearPaquete(linea));
            }

            if (paquetes.size() != cantidadEsperada) {
                throw new RuntimeException(
                        "Error en cantidad de paquetes");
            }
        }

        return paquetes;
    }



    private Paquete parsearPaquete(String linea) {

        String[] datos = linea.split(";");

        return new Paquete(
                Integer.parseInt(datos[0]),
                datos[1],
                Integer.parseInt(datos[2]),
                datos[3].equals("1"),
                Integer.parseInt(datos[4])
        );
    }
    private Camion parsearCamion(String linea) {

        String[] datos = linea.split(";");

        return new Camion(
                Integer.parseInt(datos[0]),
                datos[1],
                datos[2].equals("1"),
                Integer.parseInt(datos[3]),
                new ArrayList<>()
        );
    }
}
