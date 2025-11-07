/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author juan.valencia57
 */
public class ClienteDTO {
    private String identificacion;
    private String nombres;
    private String direccionContacto;
    private String numeroContacto;
    private List<String> mascotas;
    public ClienteDTO() {
        this.mascotas = new ArrayList<>();
    }

    public ClienteDTO(String identificacion, String nombres, String direccionContacto, String numeroContacto, List<String> mascotas) {
        this.identificacion = identificacion;
        this.nombres = nombres;
        this.direccionContacto = direccionContacto;
        this.numeroContacto = numeroContacto;
        this.mascotas = mascotas;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDireccionContacto() {
        return direccionContacto;
    }

    public void setDireccionContacto(String direccionContacto) {
        this.direccionContacto = direccionContacto;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public List<String> getMascotas() {
        return mascotas;
    }

    public void setMascotas(List<String> mascotas) {
        this.mascotas = mascotas;
    }

    @Override
    public String toString() {
        return "ClienteDTO{" + "identificacion=" + identificacion + ", nombres=" + nombres + ", direccionContacto=" + direccionContacto + ", numeroContacto=" + numeroContacto + ", mascotas=" + mascotas + '}';
    }
}
