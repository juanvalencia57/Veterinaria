/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.List;

public class VacunaDTO {
    private String tipo;             
    private List<DosisDTO> dosis; 

    public VacunaDTO(String tipo, List<DosisDTO> dosis) {
        this.tipo = tipo;
        this.dosis = dosis;
    }

    public VacunaDTO() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<DosisDTO> getDosis() {
        return dosis;
    }

    public void setDosis(List<DosisDTO> dosis) {
        this.dosis = dosis;
    }

    @Override
    public String toString() {
        return "VacunaDTO{" + "tipo=" + tipo + ", dosis=" + dosis + '}';
    }
    
    
}
