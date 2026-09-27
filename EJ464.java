import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EJ464 {
    public static void main(String[] args) {

        try (
                BufferedReader lector =
                        new BufferedReader(new FileReader("datos.txt"));
                BufferedWriter escritor =
                        new BufferedWriter(new FileWriter("copia.txt"))
        ) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (!linea.trim().equals("")) {
                    escritor.write(linea);
                    escritor.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Error al copiar el fichero.");
            return;
        }

        System.out.println("Se han copiado las líneas con texto.");
    }
}