package Modelo;

public class AccesorioDTO {
    private String id;
    private String nombre;
    private String tipo;
    private double precio;
    private int cantidad; // Stock actual disponible

    public AccesorioDTO(String id, String nombre, String tipo, double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public AccesorioDTO() {
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "AccesorioDTO{" + "id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", precio=" + precio + ", cantidad=" + cantidad + '}';
    }
}