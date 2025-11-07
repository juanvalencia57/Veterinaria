/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import Modelo.MascotaDTO;
import java.util.List;

/**
 *
 * @author juan.valencia57
 */
public interface MascotaDAO {
        public boolean almacenarMascota(MascotaDTO mascota);
        public MascotaDTO consultarMascota(String id);
	public List<MascotaDTO> listarMascota();
	public boolean eliminarMascota(String id); //Depende de la venta sin RESOLVER
	public boolean actualizarMascota(MascotaDTO mascota);
}
