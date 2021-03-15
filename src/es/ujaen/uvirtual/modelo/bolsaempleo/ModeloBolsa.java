package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Bolsa;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de dotencia 
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * 
 * @author acr00058
 *
 */
public class ModeloBolsa {	
	/**
	 * Listado de bolsas de empleo. 
	 * @return listado de bolsas de empleo
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public List<Bolsa> listaBolsaEmpleo() throws SQLException, UVException {
		ModeloArea modeloArea = new ModeloArea();
		List<Bolsa> bolsas = new ArrayList<>();
		
		String consulta =
			"SELECT bepbol.* "
		  + "FROM TBEP_BOLSAS bepbol "
		  + "WHERE 1=1 ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = new Bolsa();
					bolsa.setIdBolsa(rs.getInt("CODNUM"));
					bolsa.setArea(modeloArea.getAreaById(rs.getInt("BEPARE_CODNUM")));
					bolsa.setEstado(rs.getString("ESTADO"));
					bolsa.setBaremable(rs.getString("FLGBAREMABLE").equals("S"));					
					bolsa.setFechaActualizacion(rs.getTimestamp("FECHAACTUALIZACION"));
					bolsa.setFechaBloqueo(rs.getTimestamp("FECHABLOQUEO"));
					bolsa.setFechaDesBloqueo(rs.getTimestamp("FECHADEBLOQUEO"));
					
					bolsas.add(bolsa);					
				}
			}
		}
		
		return bolsas;		
	}	
}
