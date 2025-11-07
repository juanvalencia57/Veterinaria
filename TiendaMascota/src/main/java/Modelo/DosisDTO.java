/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.util.Date;
/**
 *
 * @author juan.valencia57
 */
public class DosisDTO {
    private Date fecha;
    private double cantidad;

    public DosisDTO() {
    }

    public DosisDTO(Date fecha, double cantidad) {
        this.fecha = fecha;
        this.cantidad = cantidad;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Dosis{" + "fecha=" + fecha + ", cantidad=" + cantidad + '}';
    }
}
