/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;

public class ConsultaDTO {
    private String idMascota;
    private String nombreMascota;
    private Date fecha;
    private String sintomas;
    private String tratamiento;

    public ConsultaDTO() {
        
    }
    public ConsultaDTO(String idMascota, String nombreMascota, Date fecha, String sintomas, String tratamiento) {
        this.idMascota = idMascota;
        this.nombreMascota = nombreMascota;
        this.fecha = fecha;
        this.sintomas = sintomas;
        this.tratamiento = tratamiento;
    }

    public String getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(String idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    @Override
    public String toString() {
        return "ConsultaDTO{" + "idMascota=" + idMascota + ", nombreMascota=" + nombreMascota + ", fecha=" + fecha + ", sintomas=" + sintomas + ", tratamiento=" + tratamiento + '}';
    }
    
    
}
