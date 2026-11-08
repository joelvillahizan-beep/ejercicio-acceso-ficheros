import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GestionRecetas {
    private static final String RUTA_FICHERO = "src/main/resources/";
    private static final String FICHERO_BINARIO = "recetas.dat";
    private static final String FICHERO_XML = "recetas.xml";
    private static final String FICHERO_JSON = "configuracion.json";
    private static final int TAM_REGISTRO = 228;
    private static final int POS_TIEMPO = 224;

    public static void main(String[] args) {
        System.out.println("Usuario: " + System.getProperty("user.name"));
        System.out.println("Fecha y hora: " + java.time.ZonedDateTime.now());
        try {
            comprobarCatalogo();
        } catch (IOException e) {
            System.out.println("No se puede abrir el catálogo: " + e.getMessage());
            return;
        }

        Configuracion configuracion = leerConfiguracion();
        System.out.println("Última opción utilizada: " + configuracion.getUltimaOpcion());
        System.out.println("Recetas de la última sesión: " + configuracion.getTotalRecetas());

        Scanner sc = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n*** GESTOR DE RECETAS ***");
            System.out.println("1. Mostrar recetas");
            System.out.println("2. Buscar receta");
            System.out.println("3. Modificar tiempo de preparación");
            System.out.println("4. Añadir receta");
            System.out.println("5. Exportar recetas a XML");
            System.out.println("0. Salir");
            System.out.print("Seleccionar una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introduce una opción numérica.");
                opcion = -1;
            }

            try {
                comprobarCatalogo();
                switch (opcion) {
                    case 1:
                        mostrarRecetas();
                        break;
                    case 2:
                        buscarReceta(sc);
                        break;
                    case 3:
                        modificarTiempo(sc);
                        break;
                    case 4:
                        anadirReceta(sc);
                        break;
                    case 5:
                        exportarXML();
                        break;
                    case 0:
                        System.out.println("Fin del programa.");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
                if (opcion >= 1 && opcion <= 5) {
                    configuracion.setUltimaOpcion(opcion);
                }
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
            } catch (IOException e) {
                System.out.println("Error de acceso al catálogo: " + e.getMessage());
            }
        } while (opcion != 0);

        try {
            comprobarCatalogo();
            configuracion.setTotalRecetas(contarRecetas());
            guardarConfiguracion(configuracion);
        } catch (IOException e) {
            System.out.println("No se ha guardado la configuración: " + e.getMessage());
        }
        sc.close();
    }

    private static void comprobarCatalogo() throws IOException {
        File directorio = new File(RUTA_FICHERO);
        if (!directorio.exists() && !directorio.mkdirs()) {
            throw new IOException("No se puede crear el directorio de recursos.");
        }
        File fichero = new File(RUTA_FICHERO + FICHERO_BINARIO);
        if (!fichero.exists()) {
            crearFichero();
        }
        if (!fichero.isFile() || fichero.length() % TAM_REGISTRO != 0) {
            throw new IOException("El fichero binario no contiene registros completos.");
        }
    }

    private static void crearFichero() throws IOException {
        Receta[] recetas = {
                new Receta(1, "Tortilla de patatas", "Patatas, huevos, aceite y sal", 35),
                new Receta(2, "Ensalada", "Lechuga, tomate, cebolla y aceite", 15),
                new Receta(3, "Macarrones", "Macarrones, tomate y queso", 25)
        };

        try (RandomAccessFile fichero = new RandomAccessFile(RUTA_FICHERO + FICHERO_BINARIO, "rw")) {
            fichero.setLength(0);
            for (Receta receta : recetas) {
                escribirReceta(fichero, receta);
            }
            System.out.println("Catálogo inicial creado correctamente.");
        }
    }

    private static void escribirReceta(RandomAccessFile fichero, Receta receta) throws IOException {
        fichero.writeInt(receta.getIdentificador());

        StringBuffer nombre = new StringBuffer(receta.getNombre());
        nombre.setLength(30);
        fichero.writeChars(nombre.toString());

        StringBuffer ingredientes = new StringBuffer(receta.getIngredientes());
        ingredientes.setLength(80);
        fichero.writeChars(ingredientes.toString());

        fichero.writeInt(receta.getTiempoPreparacion());
    }

    private static Receta leerReceta(RandomAccessFile fichero) throws IOException {
        int identificador = fichero.readInt();

        char[] nombre = new char[30];
        for (int i = 0; i < nombre.length; i++) {
            nombre[i] = fichero.readChar();
        }

        char[] ingredientes = new char[80];
        for (int i = 0; i < ingredientes.length; i++) {
            ingredientes[i] = fichero.readChar();
        }

        int tiempo = fichero.readInt();
        return new Receta(identificador, new String(nombre).trim(), new String(ingredientes).trim(), tiempo);
    }

    private static void mostrarRecetas() {
        try (RandomAccessFile fichero = new RandomAccessFile(RUTA_FICHERO + FICHERO_BINARIO, "r")) {
            while (fichero.getFilePointer() < fichero.length()) {
                System.out.println("\n" + leerReceta(fichero));
            }
        } catch (IOException e) {
            System.out.println("Error al leer las recetas.");
        }
    }

    private static void buscarReceta(Scanner sc) {
        System.out.print("Identificador de la receta: ");
        int identificador = Integer.parseInt(sc.nextLine());

        try (RandomAccessFile fichero = new RandomAccessFile(RUTA_FICHERO + FICHERO_BINARIO, "r")) {
            while (fichero.getFilePointer() < fichero.length()) {
                Receta receta = leerReceta(fichero);
                if (receta.getIdentificador() == identificador) {
                    System.out.println("\n" + receta);
                    return;
                }
            }
            System.out.println("La receta no existe.");
        } catch (IOException e) {
            System.out.println("Error al buscar la receta.");
        }
    }

    private static void modificarTiempo(Scanner sc) {
        System.out.print("Identificador de la receta: ");
        int identificador = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo tiempo en minutos: ");
        int tiempo = Integer.parseInt(sc.nextLine());

        if (tiempo <= 0) {
            System.out.println("El tiempo debe ser mayor que cero.");
            return;
        }

        try (RandomAccessFile fichero = new RandomAccessFile(RUTA_FICHERO + FICHERO_BINARIO, "rw")) {
            while (fichero.getFilePointer() < fichero.length()) {
                long posicion = fichero.getFilePointer();
                Receta receta = leerReceta(fichero);
                if (receta.getIdentificador() == identificador) {
                    fichero.seek(posicion + POS_TIEMPO);
                    fichero.writeInt(tiempo);
                    System.out.println("Tiempo actualizado correctamente.");
                    return;
                }
            }
            System.out.println("La receta no existe.");
        } catch (IOException e) {
            System.out.println("Error al modificar la receta.");
        }
    }

    private static void anadirReceta(Scanner sc) {
        System.out.print("Identificador: ");
        int identificador = Integer.parseInt(sc.nextLine());
        System.out.print("Nombre de la receta: ");
        String nombre = sc.nextLine();
        System.out.print("Ingredientes: ");
        String ingredientes = sc.nextLine();
        System.out.print("Tiempo de preparación en minutos: ");
        int tiempo = Integer.parseInt(sc.nextLine());

        if (identificador <= 0 || tiempo <= 0 || nombre.trim().length() == 0 ||
                ingredientes.trim().length() == 0 || nombre.length() > 30 || ingredientes.length() > 80) {
            System.out.println("Datos no válidos. Máximo: 30 caracteres de nombre y 80 de ingredientes.");
            return;
        }

        try (RandomAccessFile fichero = new RandomAccessFile(RUTA_FICHERO + FICHERO_BINARIO, "rw")) {
            while (fichero.getFilePointer() < fichero.length()) {
                Receta receta = leerReceta(fichero);
                if (receta.getIdentificador() == identificador) {
                    System.out.println("Ya existe una receta con ese identificador.");
                    return;
                }
            }

            fichero.seek(fichero.length());
            escribirReceta(fichero, new Receta(identificador, nombre, ingredientes, tiempo));
            System.out.println("Receta añadida correctamente.");
        } catch (IOException e) {
            System.out.println("Error al añadir la receta.");
        }
    }

    private static void exportarXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document documento = builder.newDocument();
            Element recetas = documento.createElement("recetas");
            documento.appendChild(recetas);

            try (RandomAccessFile fichero = new RandomAccessFile(RUTA_FICHERO + FICHERO_BINARIO, "r")) {
                while (fichero.getFilePointer() < fichero.length()) {
                    Receta receta = leerReceta(fichero);
                    Element elementoReceta = documento.createElement("receta");
                    elementoReceta.setAttribute("id", "" + receta.getIdentificador());
                    recetas.appendChild(elementoReceta);

                    Element nombre = documento.createElement("nombre");
                    nombre.setTextContent(receta.getNombre());
                    elementoReceta.appendChild(nombre);

                    Element ingredientes = documento.createElement("ingredientes");
                    ingredientes.setTextContent(receta.getIngredientes());
                    elementoReceta.appendChild(ingredientes);

                    Element tiempo = documento.createElement("tiempoPreparacion");
                    tiempo.setTextContent("" + receta.getTiempoPreparacion());
                    elementoReceta.appendChild(tiempo);
                }
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(documento),
                    new StreamResult(new File(RUTA_FICHERO + FICHERO_XML)));
            System.out.println("Documento recetas.xml creado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al exportar las recetas a XML.");
        }
    }

    private static Configuracion leerConfiguracion() {
        File fichero = new File(RUTA_FICHERO + FICHERO_JSON);
        if (!fichero.exists()) {
            return new Configuracion(0, 0);
        }

        Gson gson = new Gson();
        try (FileReader lector = new FileReader(RUTA_FICHERO + FICHERO_JSON)) {
            Configuracion configuracion = gson.fromJson(lector, Configuracion.class);
            if (configuracion != null) {
                return configuracion;
            }
        } catch (IOException | com.google.gson.JsonParseException e) {
            System.out.println("Error al leer la configuración JSON: " + e.getMessage());
        }
        return new Configuracion(0, 0);
    }

    private static void guardarConfiguracion(Configuracion configuracion) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter escritor = new FileWriter(RUTA_FICHERO + FICHERO_JSON)) {
            gson.toJson(configuracion, escritor);
            System.out.println("Configuración guardada correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar la configuración JSON.");
        }
    }

    private static int contarRecetas() throws IOException {
        try (RandomAccessFile fichero = new RandomAccessFile(RUTA_FICHERO + FICHERO_BINARIO, "r")) {
            return (int) (fichero.length() / TAM_REGISTRO);
        }
    }
}
