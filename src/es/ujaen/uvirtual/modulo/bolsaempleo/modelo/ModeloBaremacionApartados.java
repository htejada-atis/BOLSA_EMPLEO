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
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.ApartadoBaremacion;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase de modelo para la gestión de los apartados de baremación (BLOQUES).
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloBaremacionApartados {
	// ordenación apartados generales de baremación
	
	public static final int ORDER_COLUMN_INDEX_APARTADOS_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_PUNTUACIONMAXIMA = 2;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_PORCENTAJEMAXIMO = 3;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_ACTIVO = 4;
	
	// errores
	public static final String ERROR_APARTADO_REQUERIDO = "El bloque es requerido";
	public static final String ERROR_APARTADO_NOEXITE = "Bloque no encontrado";
	public static final String ERROR_APARTADO_OBTENIENDO_TOTAL = "No hay conteo de bloques";
	public static final String ERROR_PUNTUACION_PORCENTAJE_MAXIMO = "Debe introducir una puntuación o un porcentaje máximo, pero no ambos";
	public static final String ERROR_APARTADO_EXISTEN_APARTADOS_CON_PORCENTAJE = "Existen bloques que se evaluan con porcentaje";
	public static final String ERROR_APARTADO_EXISTEN_APARTADOS_CON_PUNTUACION = "Existen bloques que se evaluan con puntuación";
	public static final String ERROR_APARTADO_MISMO_CODIGO = "Ya existe un apartado con el código introducido";
	
	public static final Integer COLUMN_CODIGO_MAXLENGTH = 3; 
	public static final Integer COLUMN_NOMBRE_MAXLENGTH = 100;

	public static final Float MAXIMO_VALOR_PORCENTAGE = (float) 100.0;
	
	public static final String CODIGO = "CODIGO";
	public static final String CODNUM = "CODNUM";

	protected static ModeloBaremacionApartados eInstancia;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloBaremacionApartados();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
	public static ModeloBaremacionApartados obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS APARTADOBAREMACION  ********************************************/
	
    /**
	 * Devuelve un apartado de baremación por su id.
	 * @param codNum .
	 * @return apartado o excepción si no existe
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si apartado no es existe
	 */
	public ApartadoBaremacion getApartadoBaremacionById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(ERROR_APARTADO_REQUERIDO);	
		}
			
		String sql = "SELECT bepapa.* FROM TBEP_APARTADOSBAREMACION bepapa WHERE bepapa.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(ERROR_APARTADO_NOEXITE);
				}
				return this.createApartadoFromResultSet(rs);									
			}
		}
	}
	
    /** Consulta para obtener el último código de los apartados .
	 * @return apartado .
	 * @throws SQLException .
	 */
	public String getUltimoCodigoApartado() throws SQLException {
		String consulta = 
				"SELECT bepapa.CODIGO FROM TBEP_APARTADOSBAREMACION bepapa WHERE bepapa.FLGACTIVO = 'S' "
				+ " ORDER BY bepapa.CODIGO DESC FETCH FIRST 1 ROW ONLY";
		
		String ultimoCodigo = "";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					ultimoCodigo = "0";		
				} else {
					ultimoCodigo = rs.getString(CODIGO);					
				}
			}
		}
		
		return ultimoCodigo;
	}
    
	/**
	 * Listado de apartados generales de baremación. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado 
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<ApartadoBaremacion> listadoApartadosGeneralesBaremacionDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<ApartadoBaremacion> apartados = new ArrayList<>();
		BolsaEmpleoDataTable<ApartadoBaremacion> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = "SELECT bepapa.* FROM TBEP_APARTADOSBAREMACION bepapa WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_CODIGO, "bepapa.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_NOMBRE, "bepapa.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_PUNTUACIONMAXIMA, "bepapa.PUNTUACIONMAXIMA");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_PORCENTAJEMAXIMO, "bepapa.PORCENTAJEMAXIMO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_ACTIVO, "bepapa.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ApartadoBaremacion apartado = this.createApartadoFromResultSet(rs);
					apartados.add(apartado);					
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(apartados);
		}
		
		return dataTable;
	}

	/** Función que inserta un apartado en la BD.
	 * @param apartado .
	 * @param usuarioUpdate .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Integer insertaApartado(ApartadoBaremacion apartado, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (apartado == null) {
			throw new UVException(ERROR_APARTADO_REQUERIDO);
		}
		
		this.chequearApartadoParaInsertarOActualizar(apartado);
		
		String consulta = "INSERT INTO TBEP_APARTADOSBAREMACION (CODIGO,NOMBRE,FLGACTIVO,PUNTUACIONMAXIMA,PORCENTAJEMAXIMO,UID_USUARIO) "
				+ " VALUES (?,?,?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
				PreparedStatement stmt = conexion.prepareStatement(consulta, new String[]{CODNUM})) {
			
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, apartado.getCodigo());
			stmt.setString(parameterIndex++, apartado.getNombre());
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(apartado.getActivo()) ? "S" : "N");
			
			if (apartado.getPuntuacionMaxima() != null) {
				stmt.setFloat(parameterIndex++, apartado.getPuntuacionMaxima());
				stmt.setNull(parameterIndex++, Types.NULL);
			} else if (apartado.getPorcentajeMaximo() != null) {
				stmt.setNull(parameterIndex++, Types.NULL);
				stmt.setFloat(parameterIndex++, apartado.getPorcentajeMaximo());
			}
			
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			
			return rs.getInt(1);
		}
	}
		
	/** 
	 * Desactiva un apartado de baremación.
	 * @param apartado .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void desactivarApartado(ApartadoBaremacion apartado, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		this.activaDesactivaApartado(apartado, false, usuarioUpdate);
	}
	
	/** 
	 * Actia un apartado de baremación.
	 * @param apartado .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void activarApartado(ApartadoBaremacion apartado, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		this.activaDesactivaApartado(apartado, true, usuarioUpdate);
	}
	
	/** Actualiza un apartado .
	 * @param apartado con los datos nuevos a actualizar .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de errores de validacion .
	 */
	public void actualizaApartado(ApartadoBaremacion apartado, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		this.chequearApartadoParaInsertarOActualizar(apartado);
		
		String consulta = "UPDATE TBEP_APARTADOSBAREMACION SET "
				+ "CODIGO=?,NOMBRE=?,FLGACTIVO=?,PUNTUACIONMAXIMA=?,PORCENTAJEMAXIMO=?,UID_USUARIO=? WHERE CODNUM =?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, apartado.getCodigo());
			stmt.setString(parameterIndex++, apartado.getNombre());
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(apartado.getActivo()) ? "S" : "N");
			
			if (apartado.getPuntuacionMaxima() != null) {
				stmt.setFloat(parameterIndex++, apartado.getPuntuacionMaxima());
				stmt.setNull(parameterIndex++, Types.NULL);
			} else if (apartado.getPorcentajeMaximo() != null) {
				stmt.setNull(parameterIndex++, Types.NULL);
				stmt.setFloat(parameterIndex++, apartado.getPorcentajeMaximo());
			}
			
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, apartado.getCodNum());
			stmt.executeUpdate();
		}		
	}
		
	/** lista todos los apartados activos.
	 * @return vector con todos los apartados .
	 * @throws SQLException si hay un error en la base de datos .
	 * @throws UVException .
	 */
	public List<ApartadoBaremacion> getApartadosActivos() throws SQLException {
		List<ApartadoBaremacion> apartados = new ArrayList<>();
		
		String consulta =
				"SELECT bepapa.* FROM TBEP_APARTADOSBAREMACION bepapa WHERE bepapa.FLGACTIVO = 'S'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ApartadoBaremacion apartado = this.createApartadoFromResultSet(rs);	
					apartados.add(apartado);					
				}				
			}				
		}
		
		return apartados;
	}

	/**
	 * Devuelve los apartados de bareamación activos.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<ApartadoBaremacion> listaApartadoBaremacionActivosOrdenadosPorCodigo() throws SQLException {
		return listaApartadoBaremacion(" WHERE FLGACTIVO = 'S' ORDER BY CODIGO");
	} 
	
	/**
	 * Devuelve si la suma de porcenajes de items es correcta.
	 * @param apartado .
	 * @param sentido .
	 * @return .
	 * @throws SQLException .
	 */
	public boolean checkSumaPorcentagesApartados(ApartadoBaremacion apartado, String sentido) throws SQLException {		
		String sql = "SELECT SUM(bepapa.PORCENTAJEMAXIMO) AS SUMA FROM TBEP_APARTADOSBAREMACION bepapa WHERE bepapa.FLGACTIVO != 'N'";
		
		if (apartado.getCodNum() != null) {
			sql += " AND bepapa.CODNUM != ?";
		}
		
		boolean check = false; 
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			
			if (apartado.getCodNum() != null) {
				stmt.setInt(parameterIndex++, apartado.getCodNum());
			}
		
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Float acum = null;
					if (apartado.getPorcentajeMaximo() != null) {
						acum = rs.getFloat("SUMA") + apartado.getPorcentajeMaximo();
					} else {
						acum = rs.getFloat("SUMA");
					}
					
					if ("Superar".equals(sentido)) {
						if (acum > MAXIMO_VALOR_PORCENTAGE) {
							check = true;
							break;
						}
					} else {
						if (acum < MAXIMO_VALOR_PORCENTAGE) {
							check = true;
							break;
						}
					}
				}
			}
		}
		
		return check;
	} 
	
	private List<ApartadoBaremacion> listaApartadoBaremacion(String clausula) throws SQLException {
		List<ApartadoBaremacion> items = new ArrayList<>();
		String consulta = "SELECT bepapa.* FROM TBEP_APARTADOSBAREMACION bepapa " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					items.add(this.createApartadoFromResultSet(rs));
				}
			}
		}
		return items;
	}
	
	private ApartadoBaremacion createApartadoFromResultSet(ResultSet rs) throws SQLException {
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		apartado.setCodNum(rs.getInt(CODNUM));
		apartado.setCodigo(rs.getString(CODIGO));
		apartado.setNombre(rs.getString("NOMBRE"));
		apartado.setPuntuacionMaxima(rs.getFloat("PUNTUACIONMAXIMA") == 0 ? null : rs.getFloat("PUNTUACIONMAXIMA"));
		apartado.setPorcentajeMaximo(rs.getFloat("PORCENTAJEMAXIMO") == 0 ? null : rs.getFloat("PORCENTAJEMAXIMO"));
		apartado.setActivo("S".equals(rs.getString("FLGACTIVO")));
		return apartado;
	}
	
	private void activaDesactivaApartado(ApartadoBaremacion apartado, Boolean activo, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String consulta = "UPDATE TBEP_APARTADOSBAREMACION SET FLGACTIVO = ?, UID_USUARIO = ? WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(activo) ? "S" : "N");
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, apartado.getCodNum());
			stmt.executeUpdate();
		}	
	}
	
	private boolean existeOtroApartadoActivoPorCodigo(ApartadoBaremacion apartado) throws SQLException {		
		String sql = "SELECT bepapa.* FROM TBEP_APARTADOSBAREMACION bepapa WHERE bepapa.CODIGO = ? AND bepapa.FLGACTIVO = 'S' ";
		
		if (apartado.getCodNum() != null) {
			sql += " AND bepapa.CODNUM <> ? ";
		}
		
		sql += " FETCH FIRST 1 ROW ONLY";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, apartado.getCodigo());
			
			if (apartado.getCodNum() != null) {
				stmt.setInt(parameterIndex++, apartado.getCodNum());
			}
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return true;									
				}
			}
		}
		
		return false;
	} 
			
	private void chequearApartadoParaInsertarOActualizar(ApartadoBaremacion apartado) throws SQLException, UVException {
		if (this.existeOtroApartadoActivoPorCodigo(apartado)) {
			throw new UVException(ERROR_APARTADO_MISMO_CODIGO);
		}
		
		if (this.checkSumaPorcentagesApartados(apartado, "Superar")) {
			throw new UVException("Los porcentages maximos de los bloques ya suman el 100%");
		}
		
		// puntuación o porcentaje
		if (apartado.getPuntuacionMaxima() == null && apartado.getPorcentajeMaximo() == null) {
			throw new UVException("Introduce una puntuación máxima o un porcentaje máximo");			
		}
		
		// puntuaciones o porcentaje pero no ambos
		if (apartado.getPuntuacionMaxima() != null && apartado.getPorcentajeMaximo() != null) {
			throw new UVException(ERROR_PUNTUACION_PORCENTAJE_MAXIMO);
		}
		
		// si este apartado tiene puntuacion, el resto tb tiene puntuacion
		if (apartado.getPuntuacionMaxima() != null && !this.todosLosApartadosConPuntuacion()) {
			throw new UVException(ERROR_APARTADO_EXISTEN_APARTADOS_CON_PORCENTAJE);
		}
		
		// si este apartado tiene porcentaje, el resto tb con porcentaje
		if (apartado.getPorcentajeMaximo() != null && !this.todosLosApartadosConPorcentaje()) {
			throw new UVException(ERROR_APARTADO_EXISTEN_APARTADOS_CON_PUNTUACION);
		}
	}
	
	private boolean todosLosApartadosConPuntuacion() throws SQLException, UVException {
		String sql = "SELECT COUNT(*) AS count "
				+ "FROM TBEP_APARTADOSBAREMACION bepapa "
				+ "WHERE bepapa.FLGACTIVO = 'S' "
				+ "AND bepapa.PORCENTAJEMAXIMO IS NOT NULL";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(ERROR_APARTADO_OBTENIENDO_TOTAL);
				}
				
				if (rs.getInt("count") > 0) {
					return false;
				}				
			}			
		}
		
		return true;
	}
	
	private boolean todosLosApartadosConPorcentaje() throws SQLException, UVException {
		String sql = "SELECT COUNT(*) AS count "
				+ "FROM TBEP_APARTADOSBAREMACION bepapa "
				+ "WHERE bepapa.FLGACTIVO = 'S' "
				+ "AND bepapa.PUNTUACIONMAXIMA IS NOT NULL";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(ERROR_APARTADO_OBTENIENDO_TOTAL);
				}
				
				if (rs.getInt("count") > 0) {
					return false;
				}				
			}			
		}
		
		return true;
	}
	
}
