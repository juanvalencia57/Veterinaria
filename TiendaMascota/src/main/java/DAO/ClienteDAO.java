/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAO;

import Modelo.ClienteDTO;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public interface ClienteDAO {
        public boolean almacenarCliente(ClienteDTO cliente);
        public ClienteDTO consultarCliente(String identificacion);
	public List<ClienteDTO> listarCliente();
	public boolean eliminarCliente(String identificacion); //Depende de la venta sin RESOLVER
	public boolean actualizarCliente(ClienteDTO cliente);
}
