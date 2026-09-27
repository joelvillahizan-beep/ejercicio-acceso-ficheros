import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class A3 {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        System.out.print("Introduce el nombre del fichero: ");
        String nombre = teclado.nextLine();

        int contador = 0;

        try (BufferedReader lector =
                     new BufferedReader(new FileReader(nombre))) {

            while (lector.readLine() != null) {
                contador++;
            }

            System.out.println("Número de líneas: " + contador);

        } catch (IOException e) {
            System.out.println("Error al leer el fichero.");
        }

        teclado.close();
    }
}