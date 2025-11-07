package Modelo;

public class DetalleDTO {
    private String idAccesorio;
    private int cantidad;
    private double precioUnitario;

    public DetalleDTO(String idAccesorio, int cantidad, double precioUnitario) {
        this.idAccesorio = idAccesorio;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public String getIdAccesorio() {
        return idAccesorio;
    }

    public void setIdAccesorio(String idAccesorio) {
        this.idAccesorio = idAccesorio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Override
    public String toString() {
        return "DetalleDTO{" + "idAccesorio=" + idAccesorio + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + '}';
    }
}