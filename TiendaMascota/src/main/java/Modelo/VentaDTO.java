package Modelo;

import java.util.List;
import java.util.Date;

public class VentaDTO {
    private String idVenta;
    private String idCliente;
    private String idVendedor;
    private String idMascota; // null o "" si no se compra mascota
    private List<DetalleDTO> detallesAccesorio; // Accesorios comprados (puede estar vacío)
    private Date fecha;
    private double total;

    public VentaDTO(String idVenta, String idCliente, String idVendedor, String idMascota, List<DetalleDTO> detallesAccesorio, Date fecha, double total) {
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.idVendedor = idVendedor;
        this.idMascota = idMascota;
        this.detallesAccesorio = detallesAccesorio;
        this.fecha = fecha;
        this.total = total;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public List<DetalleDTO> getDetallesAccesorio() {
        return detallesAccesorio;
    }

    public void setDetallesAccesorio(List<DetalleDTO> detallesAccesorio) {
        this.detallesAccesorio = detallesAccesorio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "VentaDTO{" + "idVenta=" + idVenta + ", idCliente=" + idCliente + ", idVendedor=" + idVendedor + ", idMascota=" + idMascota + ", detallesAccesorio=" + detallesAccesorio + ", fecha=" + fecha + ", total=" + total + '}';
    }
}