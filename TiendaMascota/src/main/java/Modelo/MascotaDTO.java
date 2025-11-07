/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;
import java.util.List;

public class MascotaDTO {
    private String nombre;
    private String raza;
    private int edad;
    private String tipo;       // Ejemplo: "Perro"
    private String id;
    private double peso;
    private Date fechaIngreso;
    private String lugarOrigen;
    private char genero;
    private double precio;
    private List<VacunaDTO> vacunas;
    private String idDueño;

    public MascotaDTO() {
    }

    public MascotaDTO(String nombre, String raza, int edad, String tipo, String id, double peso, Date fechaIngreso, String lugarOrigen, char genero, double precio, List<VacunaDTO> vacunas, String idDueño) {
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
        this.tipo = tipo;
        this.id = id;
        this.peso = peso;
        this.fechaIngreso = fechaIngreso;
        this.lugarOrigen = lugarOrigen;
        this.genero = genero;
        this.precio = precio;
        this.vacunas = vacunas;
        this.idDueño = idDueño;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getLugarOrigen() {
        return lugarOrigen;
    }

    public void setLugarOrigen(String lugarOrigen) {
        this.lugarOrigen = lugarOrigen;
    }

    public char getGenero() {
        return genero;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public List<VacunaDTO> getVacunas() {
        return vacunas;
    }

    public void setVacunas(List<VacunaDTO> vacunas) {
        this.vacunas = vacunas;
    }

    public String getIdDueño() {
        return idDueño;
    }

    public void setIdDueño(String idDueño) {
        this.idDueño = idDueño;
    }
 public boolean asignarDueñoSiDisponible(String nuevoDueño) {
        if (this.idDueño != null && !this.idDueño.trim().isEmpty()) {
            return false;
        }
        this.idDueño = nuevoDueño;
        return true;
    }
    @Override
    public String toString() {
        return "MascotaDTO{" + "nombre=" + nombre + ", raza=" + raza + ", edad=" + edad + ", tipo=" + tipo + ", id=" + id + ", peso=" + peso + ", fechaIngreso=" + fechaIngreso + ", lugarOrigen=" + lugarOrigen + ", genero=" + genero + ", precio=" + precio + ", vacunas=" + vacunas + ", idDue\u00f1o=" + idDueño + '}';
    }

    
}
