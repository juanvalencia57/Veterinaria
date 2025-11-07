/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;
import Modelo.VendedorDTO;
import java.util.List;
/**
 *
 * @author USUARIO
 */
public interface VendedorDAO {
        public boolean almacenarVendedor(VendedorDTO vendedor);
        public VendedorDTO consultarVendedor(String identificacion);
	public List<VendedorDTO> listarVendedor();
	public boolean eliminarVendedor(String identificacion);
        public boolean actualizarVendedor(VendedorDTO vendedor);
}
