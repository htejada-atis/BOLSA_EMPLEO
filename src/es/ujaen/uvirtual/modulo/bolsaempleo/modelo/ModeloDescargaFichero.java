package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Solicitud;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.TitulacionUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la descarga de ficheros .
 * @author ATISoluciones 
 */
public class ModeloDescargaFichero {

	protected static ModeloDescargaFichero eInstancia;

	/** Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloDescargaFichero();
		}
	}

	/** Obtiene una instancia de la conexión.
	 * @return instancia
	 */
	public static ModeloDescargaFichero obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/** Comprueba si un evaluador tiene acceso a un mérito .
	 * @param idMerito .
	 * @param usuario .
	 * @return mérito .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Merito compruebaMeritoEvaluador(int idMerito, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		String consulta = "SELECT bepmer.*"
				+ "	FROM TBEP_SOL_BOL_MERITOS bepsbm"
				+ "	INNER JOIN TBEP_SOLICITUD_BOLSAS bepsbo ON bepsbo.CODNUM = bepsbm.BEPSBO_CODNUM"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.CODNUM = bepsbo.BEPSOL_CODNUM"
				+ "	INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsbo.BEPBOL_CODNUM"
				+ "	INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepbol.BEPARE_CODNUM"
				+ " INNER JOIN TBEP_MERITOS bepmer ON bepmer.CODNUM = bepsbm.BEPMER_CODNUM"
				+ "	WHERE bepsbm.BEPMER_CODNUM = ? AND bepeva.BEPUSU_CODNUM = ? AND bepsol.ESTADO = ? ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, idMerito);
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			stmt.setString(parameterIndex++, ModeloSolicitud.SOLICITUD_ESTADO_CERRADA);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return ModeloMerito.obtenerInstancia().createMeritoFromResultset(rs, false, true);
				}
			}
		}
		
		return null;
	}
	
	/** Comprueba si un mérito pertenece a un candidato .
	 * @param idMerito .
	 * @param usuario .
	 * @return mérito .
	 * @throws UVException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Merito compruebaMeritoCandidato(int idMerito, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		String consulta = "SELECT bepmer.*"
				+ "	FROM TBEP_MERITOS bepmer"
				+ "	WHERE bepmer.CODNUM = ? AND bepmer.BEPUSU_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, idMerito);
			stmt.setInt(parameterIndex++, usuario.getCodNum());
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return ModeloMerito.obtenerInstancia().createMeritoFromResultset(rs, false, true);
				}
			}
		}
		
		return null;
	}
	
	/** Comprueba si una titulación pertenece a un candidato .
	 * @param idTitulacion .
	 * @param usuario .
	 * @return titulación .
	 * @throws UVException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public TitulacionUsuario compruebaTitulacionCandidato(int idTitulacion, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		String consulta = "SELECT beptus.* FROM TBEP_TITULACIONES_USUARIO beptus"
				+ "	WHERE beptus.CODNUM = ? AND beptus.BEPTUS_USU_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, idTitulacion);
			stmt.setInt(parameterIndex++, usuario.getCodNum());
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return ModeloMisTitulaciones.obtenerInstancia().setTitulacionUsuarioFromResultSet(rs, true);
				}
			}
		}
		
		return null;
	}
	
	/** Comprueba si una acreditación pertenece a un candidato .
	 * @param idAcreditacion .
	 * @param usuario .
	 * @return acreditación .
	 * @throws UVException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public MeritoPreferenteUsuario compruebaAcreditacionCandidato(int idAcreditacion, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		String consulta = "SELECT * FROM TBEP_MER_PRE_USUARIO "
				+ "	WHERE CODNUM = ? AND BEPUSU_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, idAcreditacion);
			stmt.setInt(parameterIndex++, usuario.getCodNum());
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return ModeloMeritosPreferentesCandidato.obtenerInstancia().createMeritoUsuarioFromResultSet(rs);
				}
			}
		}
		
		return null;
	}
	
	/** Comprueba si una plaza ofertada está disponible para un candidato .
	 * @param idPlaza .
	 * @param usuario .
	 * @param convocatoria .
	 * @return plaza ofertada .
	 * @throws UVException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public PlazaOfertada compruebaPlazaOfertadaCandidato(int idPlaza, UsuarioBolsaEmpleo usuario, Convocatoria convocatoria)
			throws SQLException, UVException {
		String consulta = "SELECT bepplo.*, "
				+ "     CASE"
				+ "         WHEN bepplo.FECHA_FIN_OFERTA > SYSDATE THEN 1"
				+ "         ELSE 0"
				+ "     END AS ABIERTA_VIGENTE"
				+ " FROM TBEP_USUARIOS bepusu"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPUSU_CODNUM = bepusu.CODNUM AND bepsol.BEPCON_CODNUM = ?"
				+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.BEPSOL_CODNUM = bepsol.CODNUM AND bepsob.FECHABAREMACION IS NOT NULL"
				+ " INNER JOIN TBEP_BOLSAS bepbol ON bepbol.CODNUM = bepsob.BEPBOL_CODNUM"
				+ " INNER JOIN TBEP_AREAS bepare ON bepare.CODNUM = bepbol.BEPARE_CODNUM"
				+ " INNER JOIN TBEP_PLAZAS_OFERTADAS bepplo ON bepplo.BEPARE_CODNUM = bepare.CODNUM"
				+ " INNER JOIN TBEP_DEDICACIONES bepded ON bepded.CODNUM = bepplo.BEPDED_CODNUM"
				+ " LEFT JOIN TBEP_ESTADO_CANDIDATOS bepesc ON bepesc.BEPUSU_CODNUM = bepusu.CODNUM AND bepesc.BEPBOL_CODNUM = bepbol.CODNUM"
				+ " LEFT JOIN ("
				+ "     SELECT"
				+ "         bepesc.BEPUSU_CODNUM AS BEPUSU_CODNUM,"
				+ "     COUNT(DISTINCT bepesc.BEPPLO_CODNUM) AS CONTRATOS"
				+ "     FROM TBEP_ESTADO_CANDIDATOS bepesc"
				+ "     WHERE bepesc.ESTADO NOT IN ('" + ModeloEstadoCandidato.ESTADO_DISPONIBLE + "',"
				+ "         '" + ModeloEstadoCandidato.ESTADO_SUSPENSION_PROVISIONAL + "', '" + ModeloEstadoCandidato.ESTADO_NO_DISPONIBLE + "')"
				+ "     GROUP BY bepesc.BEPUSU_CODNUM"
				+ " ) bepcts ON bepcts.BEPUSU_CODNUM = bepusu.CODNUM"
				+ " WHERE   bepplo.CODNUM = ?"
				+ "     AND bepusu.CODNUM = ?"
				+ "     AND ("
				+ "         CASE"
				+ "             WHEN bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_CONTRATADO_PRIMER_CUATRIMESTRE + "'"
				+ "                 AND bepplo.CUATRIMESTRE = '" + ModeloPlazaOfertada.CUATRIMESTRE_SEGUNDO + "' THEN 1"
				+ "             WHEN bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_CONTRATADO_PARCIAL + "'"
				+ "                 AND bepded.TIPO = '" + ModeloDedicacion.TIPO_TIEMPO_PARCIAL + "' THEN 1"
				+ "             WHEN bepcts.CONTRATOS > 0 THEN 0"
				+ "             WHEN bepesc.CODNUM IS NULL THEN 1"
				+ "             WHEN bepesc.ESTADO = '" + ModeloEstadoCandidato.ESTADO_DISPONIBLE + "' THEN 1"
				+ "             ELSE 0"
				+ "         END) = 1"
				+ "     AND bepplo.FLGACTIVA = 'S'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, convocatoria.getCodNum());
			stmt.setInt(parameterIndex++, idPlaza);
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return ModeloPlazaOfertada.obtenerInstancia().createPlazaOfertadaFromResultSet(rs, true);
				}
			}
		}
		
		return null;
	}
	
	/** Comprueba si una plaza ofertada es visible para un director de departamento .
	 * @param idPlaza .
	 * @param usuario .
	 * @return plaza ofertada .
	 * @throws UVException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public PlazaOfertada compruebaPlazaOfertadaDirector(int idPlaza, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		String consulta = "SELECT bepplo.*,"
				+ "     CASE"
				+ "         WHEN bepplo.FECHA_FIN_OFERTA > SYSDATE THEN 1"
				+ "         ELSE 0"
				+ "     END AS ABIERTA_VIGENTE"
				+ " FROM TBEP_PLAZAS_OFERTADAS bepplo"
				+ " INNER JOIN TBEP_EVALUADORES bepeva ON bepeva.BEPARE_CODNUM = bepplo.BEPARE_CODNUM AND bepeva.FLGACTIVO = 'S'"
				+ " WHERE bepplo.CODNUM = ? AND BEPUSU_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, idPlaza);
			stmt.setInt(parameterIndex++, usuario.getCodNum());
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return ModeloPlazaOfertada.obtenerInstancia().createPlazaOfertadaFromResultSet(rs, true);
				}
			}
		}
		
		return null;
	}
	
	/** Comprueba si una solicitud pertenece a un candidato .
	 * @param idSolicitud .
	 * @param usuario .
	 * @return solicitud .
	 * @throws UVException .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public Solicitud compruebaSolicitudCandidato(int idSolicitud, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		String consulta = "SELECT bepsol.* FROM TBEP_SOLICITUDES bepsol"
				+ " WHERE bepsol.CODNUM = ? AND bepsol.BEPUSU_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, idSolicitud);
			stmt.setInt(parameterIndex++, usuario.getCodNum());
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					Solicitud solicitud = new Solicitud();
					solicitud.setCodNum(rs.getInt("CODNUM"));
					solicitud.setConvocatoria(ModeloConvocatoria.obtenerInstancia().getConvocatoriaById(rs.getInt("BEPCON_CODNUM")));
					solicitud.setEstado(rs.getString("ESTADO"));
					solicitud.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
					solicitud.setFechaConfirmacion(rs.getDate("FECHACONFIRMACION"));
					solicitud.setArchivo(rs.getBlob("ARCHIVO") != null ? rs.getBlob("ARCHIVO").getBinaryStream() : null);
					return solicitud;
				}
			}
		}
		
		return null;
	}

}
