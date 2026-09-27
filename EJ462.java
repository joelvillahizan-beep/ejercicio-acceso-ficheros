import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class EJ462 {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        System.out.print("Introduce una palabra: ");
        String palabra = teclado.nextLine();

        if (palabra.equals("")) {
            System.out.println("No has introducido ninguna palabra.");
            teclado.close();
            return;
        }

        palabra = palabra.toUpperCase();
        int contador = 0;

        try (BufferedReader lector =
                     new BufferedReader(new FileReader("datos.txt"))) {

            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.toUpperCase().contains(palabra)) {
                    contador++;
                }
            }

            System.out.println(
                    "Líneas que contienen el texto: " + contador
            );

        } catch (IOException e) {
            System.out.println("Error al leer el fichero.");
        }

        teclado.close();
    }
}