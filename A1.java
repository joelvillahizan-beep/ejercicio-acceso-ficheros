import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class A1 {
    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        try (BufferedWriter escritor =
                     new BufferedWriter(new FileWriter("frases.txt"))) {

            System.out.println("Escribe FIN para terminar.");
            System.out.print("Introduce una frase: ");
            String frase = teclado.nextLine();

            while (!frase.equals("FIN")) {
                escritor.write(frase);
                escritor.newLine();

                System.out.print("Introduce otra frase: ");
                frase = teclado.nextLine();
            }

        } catch (IOException e) {
            System.out.println("Error al escribir el fichero.");
            teclado.close();
            return;
        }

        teclado.close();
        System.out.println("Frases guardadas en frases.txt.");
    }
}