import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EJ461 {
    public static void main(String[] args) {

        int contador = 0;

        try (BufferedReader lector =
                     new BufferedReader(new FileReader("datos.txt"))) {

            while (lector.readLine() != null) {
                contador++;
            }

            System.out.println("Número de líneas: " + contador);

        } catch (IOException e) {
            System.out.println("Error al leer el fichero.");
        }
    }
}