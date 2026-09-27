import java.io.File;
import java.io.IOException;

public class EJ3 {
    public static void main(String[] args) {

        File directorio = new File("copias");


        if (directorio.exists()) {
            if (directorio.isDirectory()) {
                System.out.println("El directorio copias ya existe.");
            } else {
                System.out.println("copias existe, pero no es un directorio.");
                return;
            }
        } else {
            if (directorio.mkdir()) {
                System.out.println("Directorio copias creado.");
            } else {
                System.out.println("No se ha podido crear el directorio.");
                return;
            }
        }

        File fichero = new File(directorio, "config.txt");

        try {

            if (fichero.createNewFile()) {
                System.out.println("Fichero config.txt creado.");
            } else {
                System.out.println("config.txt ya existe.");

                if (!fichero.isFile()) {
                    System.out.println("No es un fichero.");
                    return;
                }
            }


            File[] elementos = directorio.listFiles();

            if (elementos != null) {
                for (File elemento : elementos) {
                    if (elemento.isDirectory()) {
                        System.out.println(
                                "Directorio: " + elemento.getName()
                        );
                    } else if (elemento.isFile()) {
                        System.out.println(
                                "Fichero: " + elemento.getName()
                        );
                    }
                }
            } else {
                System.out.println("No se ha podido listar el directorio.");
            }


            if (fichero.delete()) {
                System.out.println("config.txt eliminado.");
            } else {
                System.out.println("No se ha podido eliminar config.txt.");
            }


            if (directorio.delete()) {
                System.out.println("Directorio copias eliminado.");
            } else {
                System.out.println("No se ha podido eliminar copias.");
            }

        } catch (IOException e) {
            System.out.println("Error al crear el fichero.");
        }
    }
}