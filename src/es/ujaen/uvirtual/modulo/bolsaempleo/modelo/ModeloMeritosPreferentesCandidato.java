package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de méritos preferentes de un candidato.
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloMeritosPreferentesCandidato {
	// ordenación 
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_CODIGO = 2;
	public static final int ORDER_COLUMN_INDEX_NOMBRE = 3;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 4;
	
	public static final int MAX_LENGTH_COLUMN_DESCRIPCION = 250;
	public static final int MAX_LENGTH_COLUMN_FACTOR = 20;
	
	public static final String ERROR_ACREDITACION_OBLIGATORIA = "Acreditacion obligatoria";
	public static final String ERROR_CANDIDATO_OBLIGATORIO = "Candidato obligatorio";
	public static final String ERROR_ID_ACREDITACION_OBLIGATORIA = "Id acreditacion no válida";
	public static final String ERROR_ID_USUARIO_NO_VALIDO = "Id usuario no válido";
	public static final String ERROR_SIN_MERITO_PREFERENTE = "Mérito preferente requerido";
	
	private static final String BORRADO = "S";
	private static final String VALIDADO = "S";
		
	protected static ModeloMeritosPreferentesCandidato eInstancia;

	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloMeritosPreferentesCandidato();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloMeritosPreferentesCandidato obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/**
	 * Listado. 
	 * @param params .
	 * @param usuario .
	 * @return . 
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public BolsaEmpleoDataTable<MeritoPreferenteUsuario> listaMeritosCandidatoDatatable(Map<String, String[]> params, UsuarioBolsaEmpleo usuario) 
			throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("El usuario es requerido");
		}
		
		List<MeritoPreferenteUsuario> meritos = new ArrayList<>();
		BolsaEmpleoDataTable<MeritoPreferenteUsuario> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = 
				"SELECT bepmpu.* "
				+ "FROM TBEP_MER_PRE_USUARIO bepmpu "
				+ "INNER JOIN TBEP_MERITOS_PREFERENTES bepmep ON bepmep.CODNUM = bepmpu.BEPMEP_CODNUM "
				+ "WHERE bepmep.TIPO = ? AND bepmpu.BEPUSU_CODNUM = ? AND bepmpu.FLGBORRADO != 'S'";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepmpu.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepmep.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "bepmpu.DESCRIPCION");
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int paramIndex = 1;
			stmt.setString(paramIndex, ModeloMeritosPreferentes.TIPO_POSESION);
			stmtCount.setString(paramIndex++, ModeloMeritosPreferentes.TIPO_POSESION);
			stmt.setInt(paramIndex, usuario.getCodNum());
			stmtCount.setInt(paramIndex++, usuario.getCodNum());			
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					MeritoPreferenteUsuario row = this.createMeritoUsuarioFromResultSet(rs);
					meritos.add(row);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(meritos);
		}
		
		return dataTable;
	}
	
	/**
	 * Inserta un merito preferente del candidato.
	 * @param mp .
	 * @param usuarioInsert .
	 * @throws SQLException .
	 */
	public void insertaMeritoUsuario(MeritoPreferenteUsuario mp, UsuarioBolsaEmpleo usuarioInsert) throws SQLException {
		String consulta = "INSERT INTO TBEP_MER_PRE_USUARIO (BEPMEP_CODNUM,BEPUSU_CODNUM,BEPMPO_CODNUM,DESCRIPCION,ARCHIVO,UID_USUARIO) VALUES (?,?,?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, mp.getMeritoPreferente().getCodNum());
			stmt.setInt(parameterIndex++, mp.getUsuario().getCodNum());
			if (mp.getMeritoPreferenteOpcion() != null) {
				stmt.setInt(parameterIndex++, mp.getMeritoPreferenteOpcion().getCodNum());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			stmt.setString(parameterIndex++, mp.getDescripcion());
			stmt.setBinaryStream(parameterIndex++, mp.getArchivo());
			stmt.setString(parameterIndex++, usuarioInsert.getCodCuenta());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Devuelve el merito preferente de un usuario.
	 * @param codNum .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public MeritoPreferenteUsuario getMeritoPreferenteUsuarioById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException("El mérito preferente del canditato es requerido");
		}
		
		String consulta = "SELECT * FROM TBEP_MER_PRE_USUARIO WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("Merito preferente no encontrado");
				}
				
				return this.createMeritoUsuarioFromResultSet(rs);
			}	
		}
	}
	
	/**
	 * Comprueba que existe un solo tipo de mérito activo por usuario.
	 * @param mpu .
	 * @return .
	 * @throws SQLException .
	 */
	public boolean compruebaSoloUnTipoDeMeritoPrefenteActivo(MeritoPreferenteUsuario mpu) throws SQLException {
		String query = "SELECT COUNT(*) as count FROM TBEP_MER_PRE_USUARIO bepmpu "
				+ "WHERE bepmpu.BEPMEP_CODNUM = ? AND bepmpu.BEPUSU_CODNUM = ? AND bepmpu.FLGBORRADO = 'N'";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(query)) {
			int param = 1;
			stmt.setInt(param++, mpu.getMeritoPreferente().getCodNum());
			stmt.setInt(param++, mpu.getUsuario().getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				
				if (rs.getInt("count") > 0) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public MeritoPreferenteUsuario createMeritoUsuarioFromResultSet(ResultSet rs) throws SQLException, UVException {
		MeritoPreferenteUsuario obj = new MeritoPreferenteUsuario();
		obj.setCodNum(rs.getInt("CODNUM"));
		obj.setMeritoPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(rs.getInt("BEPMEP_CODNUM")));
		obj.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
		if (rs.getInt("BEPMPO_CODNUM") != 0) {
			obj.setMeritoPreferenteOpcion(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteOpcionById(rs.getInt("BEPMPO_CODNUM")));	
		} else {
			obj.setMeritoPreferenteOpcion(null);
		}		
		obj.setDescripcion(rs.getString("DESCRIPCION"));
		obj.setArchivo(rs.getBinaryStream("ARCHIVO"));
		obj.setBorrado(rs.getString("FLGBORRADO").equals(BORRADO));
		obj.setFechaBorrado(rs.getDate("FECHA_BORRADO"));
		obj.setValidado(rs.getString("FLGVALIDADO").equals(VALIDADO));
		obj.setFechaValidado(rs.getDate("FECHA_VALIDADO"));
		return obj;
	}
	
	
	/**
	 * cambia flag de meritos a borrado.
	 * @param meritos .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void cambiarFlagBorradoMeritos(List<MeritoPreferenteUsuario> meritos, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(meritos.size());
		String query = "UPDATE TBEP_MER_PRE_USUARIO SET FLGBORRADO=?,FECHA_BORRADO=?,UID_USUARIO=? WHERE CODNUM IN  (" + params + ")";		
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;

			stmt.setString(indexParam++, "S");
			stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDate().getTime()));
			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			for (MeritoPreferenteUsuario mer : meritos) {
				stmt.setInt(indexParam++, mer.getCodNum()); 
			}
			stmt.executeUpdate();
		}	
	}
	
	/**
	 * Desvalida una acreditacion .
	 * @param acreditacion .
	 * @param candidato .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void desvalidaAcreditacion(MeritoPreferenteUsuario acreditacion, UsuarioBolsaEmpleo candidato, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		checkeosValidadDesvalida(acreditacion, candidato);
		
		String consulta = "UPDATE TBEP_MER_PRE_USUARIO SET FLGVALIDADO='N', FECHA_VALIDADO=null, UID_USUARIO=? WHERE CODNUM = ? AND BEPUSU_CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, acreditacion.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Valida una acreditacion .
	 * @param acreditacion .
	 * @param candidato .
	 * @param date .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void validaAcreditacion(MeritoPreferenteUsuario acreditacion, UsuarioBolsaEmpleo candidato, Date date, UsuarioBolsaEmpleo usuarioUpdate) 
			throws SQLException, UVException {
		checkeosValidadDesvalida(acreditacion, candidato);
		
		String consulta = "UPDATE TBEP_MER_PRE_USUARIO SET FLGVALIDADO='S', FECHA_VALIDADO=?, UID_USUARIO=? WHERE CODNUM = ? AND BEPUSU_CODNUM = ?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setDate(parameterIndex++, new java.sql.Date(date.getTime()));
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, acreditacion.getCodNum());
			stmt.setInt(parameterIndex++, candidato.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	private void checkeosValidadDesvalida(MeritoPreferenteUsuario acreditacion, UsuarioBolsaEmpleo candidato) throws UVException {
		if (acreditacion == null) {
			throw new UVException(ERROR_ACREDITACION_OBLIGATORIA);
		}
		if (acreditacion.getCodNum() == null) {
			throw new UVException(ERROR_ID_ACREDITACION_OBLIGATORIA);
		}
		if (candidato == null) {
			throw new UVException(ERROR_CANDIDATO_OBLIGATORIO);
		}
		if (candidato.getCodNum() == null) {
			throw new UVException(ERROR_ID_USUARIO_NO_VALIDO);
		}
		if (acreditacion.getMeritoPreferente() == null) {
			throw new UVException(ERROR_SIN_MERITO_PREFERENTE);
		}
	}
	
}
