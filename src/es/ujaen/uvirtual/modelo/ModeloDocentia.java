package es.ujaen.uvirtual.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.ujaen.uvirtual.beans.uvirtual.docentia.Convocatoria;
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
public class ModeloDocentia {
	
	/** objeto interno tipo de item. */
	public enum TipoItem { DIMENSION, SUBDIMENSION, VARIABLE, ITEM }
	
	/** objeto interno estado solicitud. */
	public enum EstadoSolicitud { ABIERTA, RECIBIDA }
	
	/** objeto interno estado convocatoria. */
	public enum EstadoConvocatoria { ABIERTA, CREADA, CERRADA } 
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS   ********************************************/

	/** Consulta convocatorias en BBDD y las devuelve.
	 * @param soloAbiertas devuelve solo las convocatorias abiertas
	 * @param id devuelve solo la convocatoria con el id indicado
	 * @return convocatorias que cumplen los criterios
	 * @throws SQLException en caso de error de base de datos
	 */
	private List<Convocatoria> listaConvocatorias(Boolean soloAbiertas, Integer id) throws SQLException {
		List<Convocatoria> convocatorias = new ArrayList<>();
		String consulta = "SELECT c.idconvocatoria,c.nombreconvocatoria,"
						+ "       c.estado,c.observaciones,c.fechalimite, c.fechacomision "
						+ "  FROM dct_convocatorias c "
						+ " WHERE 1=1 ";
		if (soloAbiertas.equals(Boolean.TRUE)) {
			consulta += " AND estado = 'ABIERTA'";
		}
		if (id != null) {
			consulta += " AND c.idConvocatoria = ? ";
		}
		consulta += " ORDER by c.idconvocatoria"; 
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			if (id != null) {
				int parameterIndex = 1; 
				stmt.setInt(parameterIndex++, id);
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int idConvocatoria = rs.getInt("idconvocatoria");
					String nombreConvocatoria = rs.getString("nombreconvocatoria");
					String estado = rs.getString("estado");
					String observaciones = rs.getString("observaciones");
					if (observaciones == null) {
						observaciones = "";
					}
					Date fechaLimite = rs.getTimestamp("fechalimite");
					Date fechaComision = rs.getTimestamp("fechacomision");
					Convocatoria con = new Convocatoria(); 
					con.setIdConvocatoria(idConvocatoria);
					con.setNombreConvocatoria(nombreConvocatoria);
					con.setEstado(estado);
					con.setObservaciones(observaciones);
					con.setFechaLimite(fechaLimite);
					con.setFechaComision(fechaComision);
					convocatorias.add(con);
				}
			}
		}
		return convocatorias;
	}
	
	/** lista todas las convocatorias.
	 * @return vector con todas las convocatorias(abiertas,cerradas y creadas)
	 * @throws SQLException si hay un error en la base de datos
	 */
	public List<Convocatoria> listaConvocatorias() throws SQLException {
		return listaConvocatorias(Boolean.FALSE, null);
	}
	
	/**	Función que inserta una convocatoria en la BD.
	 * @param convocatoria a insertar en la BD
	 * @throws SQLException en caso de error en la BD
	 */
	public void insertaConvocatoria(Convocatoria convocatoria) throws SQLException, UVException {
		if (convocatoria == null) {
			throw new UVException("No se puede insertar una convocatoria vacia");
		}
		if (convocatoria.getNombreConvocatoria() == null || convocatoria.getNombreConvocatoria().equals("")) {
			throw new UVException("No se puede insertar una convocatoria sin nombre");
		}
		if (convocatoria.getFechaLimite() == null) {
			throw new UVException("No se puede insertar una convocatoria sin fecha limite");
		}
		if (convocatoria.getFechaComision() == null) {
			throw new UVException("No se puede insertar una convocatoria sin fecha comision");
		}
		
		String consulta = "INSERT INTO dct_convocatorias " 
						+ " (nombreconvocatoria,estado,observaciones,fechalimite, fechaComision) "
						+ "VALUES (?, ?, ?, ?, ?)";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, convocatoria.getNombreConvocatoria());
			stmt.setString(parameterIndex++, convocatoria.getEstado());
			stmt.setString(parameterIndex++, convocatoria.getObservaciones());
			stmt.setDate(parameterIndex++, new java.sql.Date(convocatoria.getFechaLimite().getTime()));
			stmt.setDate(parameterIndex++, new java.sql.Date(convocatoria.getFechaComision().getTime()));
			stmt.executeUpdate();
		}
	}

	/** Elimina una convocatoria.
	 * @param convocatoria a borrar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si convocatoria no es valida
	 */
	public void borraConvocatoria(Convocatoria convocatoria) throws SQLException, UVException {
		if (convocatoria == null) {
			throw new UVException("No se puede eliminar una convocatoria vacía");
		}
		if (convocatoria.getIdConvocatoria() == null) {
			throw new UVException("No se puede eliminar una convocatoria con id vacío");
		}
		String consulta = "DELETE FROM dct_convocatorias WHERE idconvocatoria = ? ";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, convocatoria.getIdConvocatoria());
			stmt.executeUpdate();
		}
	}

	/** obtiene una convocatoria a partir de su id.
	 * @param id codigo de la convocatoria
	 * @return convocatoria con el id especificado
	 * @throws SQLException en caso de error en la BD
	 */
	public Convocatoria listaConvocatoria(int id) throws SQLException, UVException {
		List<Convocatoria> convocatorias = listaConvocatorias(Boolean.FALSE, id);
		if (convocatorias.isEmpty()) {
			throw new UVException("No existe convocatoria");
		}
		return convocatorias.get(0);
	}

	/** Actualiza una convocatoria.
	 * @param convocatoria Convocatoria con los datos nuevos a actualizar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de errores de validacion
	 */
	public void actualizaConvocatoria(Convocatoria convocatoria) throws SQLException, UVException {
		if (convocatoria == null) {
			throw new UVException("convocatoria obligatoria");
		}
		if (convocatoria.getIdConvocatoria() == null) {
			throw new UVException("id convocatoria no válido");
		}
		if (convocatoria.getFechaLimite() == null) {
			throw new UVException("fecha limite obligatoria");
		}
		if (convocatoria.getFechaComision() == null) {
			throw new UVException("fecha comisión obligatoria");
		}
		String consulta = "UPDATE dct_convocatorias "
						+ "   SET nombreconvocatoria=?, estado=?, "
						+ "       observaciones=?, fechalimite=?, fechaComision=? "
						+ " WHERE idConvocatoria=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, convocatoria.getNombreConvocatoria());
			stmt.setString(parameterIndex++, convocatoria.getEstado());
			stmt.setString(parameterIndex++, convocatoria.getObservaciones());
			stmt.setDate(parameterIndex++, new java.sql.Date(convocatoria.getFechaLimite().getTime()));
			stmt.setDate(parameterIndex++, new java.sql.Date(convocatoria.getFechaComision().getTime()));
			stmt.setInt(parameterIndex++, convocatoria.getIdConvocatoria());
			stmt.executeUpdate();
		}
	}
}
