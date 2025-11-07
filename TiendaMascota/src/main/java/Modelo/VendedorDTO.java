/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author juan.valencia57
 */
public class VendedorDTO {
    private String identificacion;
    private String nombres;
    private char genero;

    public VendedorDTO() {
    }

   public VendedorDTO(String identificacion, String nombres, char genero) {
    this.identificacion = identificacion;
    this.nombres = nombres;
    this.genero = genero;
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

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    

    @Override
    public String toString() {
        return "VendedorDTO{" + "identificacion=" + identificacion + ", nombres=" + nombres + ", genero=" + genero + '}';
    }

    
    
}
