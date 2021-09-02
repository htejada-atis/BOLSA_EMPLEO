package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Contratacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Plantilla;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la contratación .
 * 
 * @author ATISoluciones 2021
 */
public class ModeloContratacion {
	
	public static final String BEPPLO_CODNUM = "BEPPLO_CODNUM";
	public static final String BEPUSU_CODNUM = "BEPUSU_CODNUM";
	public static final String CODNUM = "CODNUM";
	public static final String FECHA_CITA = "FECHA_CITA";
	public static final String RESULTADO = "RESULTADO";
	
	public static final String RESULTADO_ACEPTADA = "ACEPTADA";
	public static final String RESULTADO_RECHAZADA = "RECHAZADA";
	
	public static final String MENSAJE_ERROR_PLANTILLA_CITA_CONTRATACION_NO_EXISTE = "La plantilla de cita de contratación no existe";
	public static final String MENSAJE_ERROR_OBJETO_VACIO = "No se puede %s una plaza vacía";
	public static final String MENSAJE_ERROR_PARAM_VACIO = "No se puede %s una plaza sin %s";
	
	protected static ModeloContratacion eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloContratacion();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloContratacion obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/** Método para devolver la contratación de una plaza .
	 * @param codnum .
	 * @return contratación .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Contratacion getContratacionById(Integer codnum) throws SQLException, UVException {
		String consulta = "SELECT bepcnt.* FROM TBEP_CONTRATACIONES bepcnt"
				+ " WHERE bepcnt.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codnum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return createContratacionFromResultSet(rs);
				}
			}
		}
		
		return null;
	}
	
	/** Método para devolver la contratación de una plaza .
	 * @param plaza .
	 * @return contratación .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Contratacion getContratacionByPlazaContratada(PlazaOfertada plaza) throws SQLException, UVException {
		String consulta = "SELECT bepcnt.* FROM TBEP_CONTRATACIONES bepcnt"
				+ " WHERE bepcnt.BEPPLO_CODNUM = ? AND bepcnt.RESULTADO = '" + RESULTADO_ACEPTADA + "'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return createContratacionFromResultSet(rs);
				}
			}
		}
		
		return null;
	}
	
	/** Método para comprobar si el candidato tiene una contratación para la plaza .
	 * @param plaza .
	 * @param candidato .
	 * @return booleano true o false .
	 * @throws SQLException .
	 */
	public Boolean comprobarContratoCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato) throws SQLException {
		String consulta = "SELECT bepcnt.* FROM TBEP_CONTRATACIONES bepcnt"
				+ " WHERE bepcnt.BEPUSU_CODNUM = ? AND bepcnt.BEPPLO_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return false;
				}
				
				return true;
			}
		}
	}
	
	/** Método para crear un mensaje de apertura de una plaza y ponerlo como pendiente de envío para los candidatos disponibles .
	 * @param plaza .
	 * @param contratacion .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 * @throws IOException .
	 */
	public void crearMensajeCitaContratacion(PlazaOfertada plaza, Contratacion contratacion, UsuarioBolsaEmpleo usuarioUpdate)
			throws SQLException, UVException, IOException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				ModeloPlantilla modeloPlantilla = ModeloPlantilla.obtenerInstancia();
				Plantilla plantilla = new Plantilla();
				
				// Obtenemos la plantilla de la cita de contratación de la plaza si existe
				String consultaSelectPlantilla = "SELECT beppls.* FROM TBEP_PLANTILLAS beppls"
						+ " INNER JOIN TBEP_PARAMETROS_CONFIG beppac ON beppac.VALOR = beppls.CODNUM "
						+ " WHERE beppac.NOMBRE = '" + ModeloParametrosConfiguracion.PARAMETRO_PLANTILLA_CITA_CONTRATACION + "'";
				
				try (PreparedStatement stmt = conexion.prepareStatement(consultaSelectPlantilla)) {
					try (ResultSet rs = stmt.executeQuery()) {
						if (!rs.next()) {
							throw new UVException(MENSAJE_ERROR_PLANTILLA_CITA_CONTRATACION_NO_EXISTE);
						}
						
						plantilla = modeloPlantilla.createPlantillaFromResultSet(rs);
					}
				}
				
				// Creamos un nuevo mensaje con los datos de la plaza en la plantilla
				Mensaje mensaje = new Mensaje(modeloPlantilla.reemplazaPlazaYContratacionEnPlantilla(plaza, contratacion, plantilla.getTitulo(), false), 
						modeloPlantilla.reemplazaPlazaYContratacionEnPlantilla(plaza, contratacion, plantilla.getCuerpo(), true),
						BolsaEmpleoUtils.getCurrentDateTime(), ModeloMensajes.MENSAJE_ESTADO_BORRADOR);
				
				int idMensaje = ModeloMensajes.obtenerInstancia().nuevoMensajeConexion(mensaje, usuarioUpdate, conexion);
				
				// agregamos el candidato como destinatario para enviar el mensaje de la plaza
				String consultaInsertDest = String.format("INSERT INTO TBEP_MEN_DESTINATARIOS (%s, %s, %s) VALUES (?, ?, ?)", 
						"BEPMEN_CODNUM", "BEPUSU_CODNUM", "UID_USUARIO");
				try (PreparedStatement stmt = conexion.prepareStatement(consultaInsertDest)) {
					int parameterIndex = 1;
					stmt.setInt(parameterIndex++, idMensaje);
					stmt.setInt(parameterIndex++, contratacion.getCandidato().getCodNum());
					stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
					stmt.executeUpdate();
				}
				
				// Actualizamos el estado del mensaje a 'ENVIANDO'
				String consultaUpdateMsg = String.format("UPDATE TBEP_MENSAJES SET %s=?, %s=? WHERE %s=?",
						"ESTADO", "UID_USUARIO", "CODNUM");
				try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdateMsg)) {
					int indexParam = 1;
					
					stmt.setString(indexParam++, ModeloMensajes.MENSAJE_ESTADO_ENVIANDO);
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					stmt.setInt(indexParam++, idMensaje);
					stmt.executeUpdate();
				}
				
				conexion.commit();
			} catch (Exception e) {
				conexion.rollback();
				throw e;
			} finally {
				conexion.setAutoCommit(true);
			}
		}
	}
	
	/** inserta contratación .
	 * @param plaza .
	 * @param fechaCita .
	 * @param candidato .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error de base de datos .
	 */
	public void insertarContratacion(PlazaOfertada plaza, java.util.Date fechaCita, UsuarioBolsaEmpleo candidato, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String consulta = String.format("INSERT INTO TBEP_CONTRATACIONES (%s,%s,%s,%s) VALUES (?,?,?,?)", 
				BEPPLO_CODNUM, BEPUSU_CODNUM, FECHA_CITA, "UID_USUARIO");
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.setDate(parameterIndex++, new Date(fechaCita.getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();
		}
	}
	
	/** aceptar cita de contratación .
	 * @param plaza .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void aceptarContrato(PlazaOfertada plaza, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plaza == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "aceptar contrato"));
		}
		
		String consulta = String.format("UPDATE TBEP_CONTRATACIONES SET %s=?, %s=?, %s=?"
				+ " WHERE %s=?",
				RESULTADO, FECHA_CITA, "UID_USUARIO", CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, RESULTADO_ACEPTADA);
			stmt.setString(parameterIndex++, usuarioUpdate == null ? "TAREA PROGRAMADA" : usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** rechazar cita de contratación .
	 * @param plaza .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void rechazarContrato(PlazaOfertada plaza, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (plaza == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "rechazar contrato"));
		}
		
		String consulta = String.format("UPDATE TBEP_PLAZAS_OFERTADAS SET %s=?, %s=?, %s=?"
				+ " WHERE %s=?",
				RESULTADO, FECHA_CITA, "UID_USUARIO", CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, RESULTADO_RECHAZADA);
			stmt.setDate(parameterIndex++, new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate == null ? "TAREA PROGRAMADA" : usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	public Contratacion createContratacionFromResultSet(ResultSet rs) throws SQLException, UVException {
		Contratacion contratacion = new Contratacion();
		
		contratacion.setCodNum(rs.getInt(CODNUM));
		contratacion.setCandidato(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt(BEPUSU_CODNUM)));
		contratacion.setPlaza(ModeloPlazaOfertada.obtenerInstancia().getPlazaOfertadaById(rs.getInt(BEPPLO_CODNUM)));
		contratacion.setFechaCita(rs.getDate(FECHA_CITA));
		contratacion.setResultado(rs.getString(RESULTADO));
		
		return contratacion;
	}
	
}
