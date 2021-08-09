package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.OfertaCandidato;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.PlazaOfertada;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para las plazas ofertadas para la contratación .
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloOfertaCandidato {
	
	public static final String BEPPLO_CODNUM = "BEPPLO_CODNUM";
	public static final String BEPUSU_CODNUM = "BEPUSU_CODNUM";
	public static final String CODNUM = "CODNUM";
	public static final String FECHA_RESULTADO = "FECHA_RESULTADO";
	public static final String PREFERENCIA = "PREFERENCIA";
	public static final String FLGRESULTADO = "FLGRESULTADO";
	
	
	protected static ModeloOfertaCandidato eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloOfertaCandidato();
		}
	}

	/**
	 * Obtiene una instancia de la conexión .
	 * @return instancia .
	 */
	public static ModeloOfertaCandidato obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	
	public void aceptarOfertaCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato) throws SQLException, UVException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			try {
				
				OfertaCandidato oferta = obtenerOfertaPorPlazaYCandidato(plaza, candidato, conexion);
				if (oferta != null) {
					
				} else {
					
				}
				
				String consulta = ""
						+ " SELECT bepofc.* FROM TBEP_OFERTAS_CANDIDATOS bepofc"
						+ " INNER JOIN TBEP_PLAZAS_OFERTADAS bepplo ON bepplo.CODNUM = bepofc.BEPPLO_CODNUM"
						+ " INNER JOIN TBEP_SOLICITUD_BOLSAS bepsob ON bepsob.BEPBOL_CODNUM = bepbol.CODNUM AND bepsob.FECHABAREMACION IS NOT NULL"
						+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsob.BEPSOL_CODNUM = bepsol.CODNUM"
						+ " WHERE   bepsol.BEPUSU_CODNUM = ?"
						+ "     AND bepplo.ESTADO = '" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "'"
						+ "     AND bepplo.CODNUM = ?";
				try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
					int parameterIndex = 1;
					stmt.setInt(parameterIndex++, plaza.getCodNum());
					stmt.setInt(parameterIndex++, candidato.getCodNum());
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
	
	public void rechazarOfertaCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato) {
		
	}
	
	private OfertaCandidato obtenerOfertaPorPlazaYCandidato(PlazaOfertada plaza, UsuarioBolsaEmpleo candidato, Connection conexion) throws SQLException, UVException {
		String consulta = "SELECT bepofc.* FROM TBEP_OFERTAS_CANDIDATOS bepofc"
				+ " INNER JOIN TBEP_PLAZAS_OFERTADAS bepplo ON bepplo.CODNUM = bepofc.BEPPLO_CODNUM"
				+ " WHERE   bepofc.BEPUSU_CODNUM = ?"
				+ "     AND bepplo.ESTADO = '" + ModeloPlazaOfertada.PLAZA_ESTADO_ABIERTA + "'"
				+ "     AND bepplo.CODNUM = ?";
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, plaza.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					createOfertaCandidatoFromResultSet(rs);
				}
			}
		}
		
		return null;
	}
	
	private void actualizaOfertaCandidato(OfertaCandidato oferta, UsuarioBolsaEmpleo usuarioUpdate, Connection conexion) throws SQLException {
		String consulta = String.format("UPDATE TBEP_OFERTAS_CANDIDATOS SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?",
				BEPPLO_CODNUM, BEPUSU_CODNUM, FLGRESULTADO, FECHA_RESULTADO, PREFERENCIA, "UID_USUARIO", CODNUM);
		try (PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, oferta.getPlaza().getCodNum());
			stmt.setInt(parameterIndex++, oferta.getCandidato().getCodNum());
			stmt.setString(parameterIndex++, oferta.isResultado() == null ? null : oferta.isResultado() ? "S" : "N");
			stmt.setInt(parameterIndex++, oferta.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	private void insertaOfertaCandidato(OfertaCandidato oferta, Connection conexion) {
		
	}
	
	public OfertaCandidato createOfertaCandidatoFromResultSet(ResultSet rs) throws SQLException, UVException {
		OfertaCandidato oferta = new OfertaCandidato();
		
		oferta.setCodNum(rs.getInt(CODNUM));
		oferta.setPlaza(ModeloPlazaOfertada.obtenerInstancia().getPlazaOfertadaById(rs.getInt(BEPPLO_CODNUM)));
		oferta.setCandidato(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt(BEPUSU_CODNUM)));
		oferta.setResultado("S".equals(rs.getString(FLGRESULTADO)));
		oferta.setFechaResultado(rs.getDate(FECHA_RESULTADO));
		oferta.setPreferencia(rs.getInt(PREFERENCIA));
		
		return oferta;
	}
	
}
