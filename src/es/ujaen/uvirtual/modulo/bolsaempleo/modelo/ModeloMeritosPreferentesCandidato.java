package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.MeritoPreferenteUsuario;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
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
				+ "FROM TBEP_MERITOS_PREFERENTES_USUARIO bepmpu "
				+ "INNER JOIN TBEP_MERITOS_PREFERENTES bepmep ON bepmep.CODNUM = bepmpu.BEPMEP_CODNUM "
				+ "WHERE bepmep.TIPO = ? AND bepmpu.BEPUSU_CODNUM = ? ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepmpu.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_CODIGO, "bepmep.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE, "bepmep.DESCRIPCION");
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
	 * @throws SQLException .
	 */
	public void insertaMeritoUsuario(MeritoPreferenteUsuario mp) throws SQLException {
		String consulta = "INSERT INTO TBEP_MERITOS_PREFERENTES_USUARIO (BEPMEP_CODNUM,BEPUSU_CODNUM,BEPMOP_CODNUM,DESCRIPCION,ARCHIVO) VALUES (?,?,?,?,?)";
		
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
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Devuelve el merito preferente de un usuario.
	 * @param codNum .
	 * @param usuario .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public MeritoPreferenteUsuario getMeritoPreferenteUsuarioById(Integer codNum, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException("El mérito preferente del canditato es requerido");
		}
		if (usuario == null) {
			throw new UVException("El usuario es requerido");
		}
		
		String consulta = "SELECT * FROM TBEP_MERITOS_PREFERENTES_USUARIO WHERE CODNUM = ? AND BEPUSU_CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			
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
		String query = "SELECT COUNT(*) as count FROM TBEP_MERITOS_PREFERENTES_USUARIO bepmpu "
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
	
	private MeritoPreferenteUsuario createMeritoUsuarioFromResultSet(ResultSet rs) throws SQLException, UVException {
		MeritoPreferenteUsuario obj = new MeritoPreferenteUsuario();
		obj.setCodNum(rs.getInt("CODNUM"));
		obj.setMeritoPreferente(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteById(rs.getInt("BEPMEP_CODNUM")));
		obj.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
		if (rs.getInt("BEPMOP_CODNUM") != 0) {
			obj.setMeritoPreferenteOpcion(ModeloMeritosPreferentes.obtenerInstancia().getMeritoPreferenteOpcionById(rs.getInt("BEPMOP_CODNUM")));	
		} else {
			obj.setMeritoPreferenteOpcion(null);
		}		
		obj.setDescripcion(rs.getString("DESCRIPCION"));
		obj.setArchivo(rs.getBinaryStream("ARCHIVO"));
		obj.setBorrado(rs.getBoolean("FLGBORRADO"));
		obj.setFechaBorrado(rs.getDate("FECHA_BORRADO"));
		obj.setValidado(rs.getBoolean("FLGVALIDADO"));
		obj.setFechaValidado(rs.getDate("FECHA_VALIDADO"));
		return obj;
	}

	

	
}
