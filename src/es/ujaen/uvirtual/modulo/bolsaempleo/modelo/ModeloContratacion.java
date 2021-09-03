package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Contratacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Mensaje;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Plantilla;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
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
	public static final String FECHA_RESULTADO = "FECHA_RESULTADO";
	public static final String RESULTADO = "RESULTADO";
	
	public static final String RESULTADO_ACEPTADA = "ACEPTADA";
	public static final String RESULTADO_PENDIENTE = "PENDIENTE";
	public static final String RESULTADO_RECHAZADA = "RECHAZADA";
	public static final String RESULTADO_SUSPENDIDO = "SUSPENDIDO";
	
	public static final Map<String, String> RESULTADOS = new HashMap<>();
	
	public static final String MENSAJE_ERROR_PLANTILLA_CITA_CONTRATACION_NO_EXISTE = "La plantilla de cita de contratación no existe";
	public static final String MENSAJE_ERROR_OBJETO_VACIO = "No se puede %s una plaza vacía";
	public static final String MENSAJE_ERROR_PARAM_VACIO = "No se puede %s una plaza sin %s";
	
	static {
		RESULTADOS.put(RESULTADO_ACEPTADA, "Aceptada");
		RESULTADOS.put(RESULTADO_PENDIENTE, "Pendiente");
		RESULTADOS.put(RESULTADO_RECHAZADA, "Rechazada");
		RESULTADOS.put(RESULTADO_SUSPENDIDO, "Suspendido");
	}
	
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
	
	/** Lista de candidatos con contrato en una plaza .
	 * @param params .
	 * @param plaza .
	 * @return datatable de candidatos .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<OfertaCandidato> listadoCandidatosContrato(Map<String, String[]> params, PlazaOfertada plaza)
			throws SQLException, UVException {
		List<OfertaCandidato> rows = new ArrayList<>();
		BolsaEmpleoDataTable<OfertaCandidato> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepusu.CODNUM AS BEPUSU_CODNUM, bepsob.TOTAL AS PUNTUACION, bepofc.CODNUM, bepofc.FLGRESULTADO, "
				+ "     bepofc.FECHA_RESULTADO, bepofc.PREFERENCIA, bepsol.BEPUSU_CODNUM AS BEPUSU_CODNUM, bepofc.BEPPLO_CODNUM AS BEPPLO_CODNUM,"
				+ "     bepcnt.CODNUM AS CONTRATACION"
				+ " FROM TBEP_USUARIOS bepusu"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPUSU_CODNUM = bepusu.CODNUM"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.BEPSOL_CODNUM = bepsol.CODNUM AND bepsob.BEPBOL_CODNUM = ?"
				+ "     AND bepsob.FECHABAREMACION IS NOT NULL"
				+ " INNER JOIN TBEP_OFERTAS_CANDIDATOS bepofc ON bepofc.BEPUSU_CODNUM = bepusu.CODNUM"
				+ " INNER JOIN TBEP_CONTRATACIONES bepcnt ON bepcnt.BEPUSU_CODNUM = bepusu.CODNUM AND bepcnt.BEPPLO_CODNUM = bepofc.BEPPLO_CODNUM"
				+ " WHERE bepofc.BEPPLO_CODNUM = ?";
		
		String whereNombre = String.format("(%s || ' ' || %s || ' ' || %s)", "bepusu.VUAJA_STRNOMBRE", "bepusu.VUAJA_STRAPELLIDO1", "bepusu.VUAJA_STRAPELLIDO2");
				
		
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int paramIndex = 1;
			stmt.setInt(paramIndex, plaza.getArea().getCodNum());
			stmtCount.setInt(paramIndex++, plaza.getArea().getCodNum());
			stmt.setInt(paramIndex, plaza.getCodNum());
			stmtCount.setInt(paramIndex++, plaza.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					OfertaCandidato oferta = ModeloOfertaCandidato.obtenerInstancia().createOfertaCandidatoFromResultSet(rs);
					oferta.setPuntuacion(rs.getDouble("PUNTUACION"));
					rows.add(oferta);
				}
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(rows);
		}
		
		return dataTable;
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
		this.cambiarResultadoContratacion(plaza, usuarioUpdate, RESULTADO_ACEPTADA, usuarioUpdate);
	}
	
	/** rechazar cita de contratación .
	 * @param plaza .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void rechazarContrato(PlazaOfertada plaza, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		this.cambiarResultadoContratacion(plaza, usuarioUpdate, RESULTADO_RECHAZADA, usuarioUpdate);
	}
	
	/** suspender cita de contratación .
	 * @param plaza .
	 * @param candidato .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void suspenderContrato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato, UsuarioBolsaEmpleo usuarioUpdate)throws SQLException, UVException {
		this.cambiarResultadoContratacion(plaza, candidato, RESULTADO_SUSPENDIDO, usuarioUpdate);
	}
	
	/** cambia el estado de una contratación .
	 * @param plaza .
	 * @param candidato .
	 * @param estado .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void cambiarResultadoContratacion(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato, String estado, UsuarioBolsaEmpleo usuarioUpdate)
			throws SQLException, UVException {
		if (plaza == null) {
			throw new UVException(String.format(MENSAJE_ERROR_OBJETO_VACIO, "cambiar resultado contratacion"));
		}
		
		String consulta = String.format("UPDATE TBEP_CONTRATACIONES SET %s=?, %s=?, %s=?"
				+ " WHERE %s=? AND %s=?",
				RESULTADO, FECHA_RESULTADO, "UID_USUARIO", BEPPLO_CODNUM, BEPUSU_CODNUM);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, estado);
			stmt.setDate(parameterIndex++, new Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate == null ? "TAREA PROGRAMADA" : usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** crea una contratación de un ResultSet .
	 * @param rs .
	 * @return contratación .
	 * @throws SQLException .
	 * @throws UVException .
	 */
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
