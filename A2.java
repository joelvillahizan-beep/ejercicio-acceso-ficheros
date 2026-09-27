import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class A2 {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        System.out.print("Introduce el nombre del fichero: ");
        String nombre = teclado.nextLine();

        try (BufferedReader lector =
                     new BufferedReader(new FileReader(nombre))) {

            String linea;
            int contador = 0;

            while ((linea = lector.readLine()) != null) {


                if (contador == 24) {
                    System.out.print("Pulsa Intro para continuar...");
                    teclado.nextLine();
                    contador = 0;
                }

                System.out.println(linea);
                contador++;
            }

        } catch (IOException e) {
            System.out.println("Error al leer el fichero.");
        }

        teclado.close();
    }
}