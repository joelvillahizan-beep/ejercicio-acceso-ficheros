public class Receta {
    private int identificador;
    private String nombre;
    private String ingredientes;
    private int tiempoPreparacion;

    public Receta(int identificador, String nombre, String ingredientes, int tiempoPreparacion) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.ingredientes = ingredientes;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    @Override
    public String toString() {
        return "ID: " + identificador +
                "\nNombre: " + nombre +
                "\nIngredientes: " + ingredientes +
                "\nTiempo de preparación: " + tiempoPreparacion + " minutos";
    }
}
