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
	public static final int ORDER_COLUMN_INDEX_APARTADOS_PORCENTAJEMAXIMO = 2;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_ACTIVO = 3;
	
	// errores
	public static final String ERROR_BLOQUE_REQUERIDO = "El bloque es requerido";
	public static final String ERROR_APARTADO_NOEXITE = "Bloque no encontrado";
	public static final String ERROR_APARTADO_MISMO_CODIGO = "Ya existe un apartado con el código introducido";
	public static final String ERROR_APARTADO_USANDOSE = "El bloque está asociado en algún mérito no se puede editar.";
	public static final String ERROR_SUMA_FACTORES_INVALIDA = "La suma de los factores de los bloques activos suman más de 1";
	
	public static final Integer COLUMN_CODIGO_MAXLENGTH = 3; 
	public static final Integer COLUMN_NOMBRE_MAXLENGTH = 100;

	public static final Double MAXIMO_VALOR_PORCENTAGE = 1.0;
	
	public static final String CODIGO = "CODIGO";
	public static final String CODNUM = "CODNUM";
	
	public static final String COMPARACION_PORCENTAJES_IGUALES = "IGUALES";
	public static final String COMPARACION_PORCENTAJES_MENOS_O_IGUAL = "MENOR_OR_IGUAL";

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
	
	
	/**
	 * Devuelve un apartado de baremación por su id.
	 * @param codNum .
	 * @return apartado o excepción si no existe
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si apartado no es existe
	 */
	public ApartadoBaremacion getApartadoBaremacionById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException(ERROR_BLOQUE_REQUERIDO);	
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
			throw new UVException(ERROR_BLOQUE_REQUERIDO);
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
				stmt.setDouble(parameterIndex++, apartado.getPuntuacionMaxima());
				stmt.setNull(parameterIndex++, Types.NUMERIC);
			} else if (apartado.getPorcentajeMaximo() != null) {
				stmt.setNull(parameterIndex++, Types.NUMERIC);
				stmt.setDouble(parameterIndex++, apartado.getPorcentajeMaximo());
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
	public void desactivarApartado(ApartadoBaremacion apartado, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		this.activaDesactivaApartado(apartado, false, usuarioUpdate);
	}
	
	/** 
	 * Actia un apartado de baremación.
	 * @param apartado .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void activarApartado(ApartadoBaremacion apartado, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		// comprobamos si ya existe otro activo con el mismo código
		String query = ""
			+ " SELECT COUNT(*) as count "
			+ " FROM TBEP_APARTADOSBAREMACION bepapa "
			+ " WHERE bepapa.CODIGO = ? AND bepapa.FLGACTIVO = 'S' ";
		
		int count = 0;
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(query)) {
			int param = 1;
			stmt.setString(param++, apartado.getCodigo());
			
			try (ResultSet rs = stmt.executeQuery()) {
				rs.next();
				count = rs.getInt("count");
			}
		}
		
		if (count > 0) {
			throw new UVException("Ya existe un bloque activo con el código " + apartado.getCodigo());
		}
				
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
		
		String consulta = ""
				+ " UPDATE TBEP_APARTADOSBAREMACION SET "
				+ " CODIGO=?,NOMBRE=?,FLGACTIVO=?,PUNTUACIONMAXIMA=?,PORCENTAJEMAXIMO=?,UID_USUARIO=? "
				+ " WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, apartado.getCodigo());
			stmt.setString(parameterIndex++, apartado.getNombre());
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(apartado.getActivo()) ? "S" : "N");
			
			if (apartado.getPuntuacionMaxima() != null) {
				stmt.setDouble(parameterIndex++, apartado.getPuntuacionMaxima());
				stmt.setNull(parameterIndex++, Types.NUMERIC);
			} else if (apartado.getPorcentajeMaximo() != null) {
				stmt.setNull(parameterIndex++, Types.NUMERIC);
				stmt.setDouble(parameterIndex++, apartado.getPorcentajeMaximo());
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
		return this.getApartados(true);
	}
	
	/** lista todos los apartados.
	 * @param activo indica si filtra por apartado activo o inactivos. Si es null. Todos.
	 * @return vector con todos los apartados .
	 * @throws SQLException si hay un error en la base de datos .
	 * @throws UVException .
	 */
	public List<ApartadoBaremacion> getApartados(Boolean activo) throws SQLException {
		List<ApartadoBaremacion> apartados = new ArrayList<>();
		
		String consulta = ""
				+ " SELECT bepapa.* "
				+ " FROM TBEP_APARTADOSBAREMACION bepapa ";
						
		if (activo != null) {
			consulta += " WHERE bepapa.FLGACTIVO = ?"; 
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			if (activo != null) {
				stmt.setString(1, activo ? "S" : "N");	
			}
			
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
	 * Devuelve si la suma de porcenajes de items es incorrecta.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public boolean checkSumaPorcentagesApartadosInvalido() throws SQLException, UVException {		
		return !compareSumaPorcentagesApartados(null, COMPARACION_PORCENTAJES_IGUALES);
	} 
	
	/**
	 * Devuelve si la suma de porcenajes supera el máximo. Si se pasa el partado, suma su valor y no el de la tabla.
	 * @param apartado .
	 * @return .
	 */
	public boolean checkSumaPorcentagesApartadosSuperaMaximo(ApartadoBaremacion apartado) throws SQLException, UVException {
		return !compareSumaPorcentagesApartados(apartado, COMPARACION_PORCENTAJES_MENOS_O_IGUAL);
	}
	
	/**
	 * Comprueba la suma de porcentajes de los apartados activos es válido según una comparación pasada. 
	 * Si se pasa el apartado, no se suma en el query si no su valor del objecto.
	 * 
	 * @param apartado .
	 * @param comprobacion tipo de comprobación .
	 * @return . 
	 * @throws UVException .
	 * @throws SQLException .
	 */
	private boolean compareSumaPorcentagesApartados(ApartadoBaremacion apartado, String comprobacion) throws SQLException, UVException {
		Double acum = this.getSumanPorcentajesApartadosActivos(apartado);
		boolean check;
		
		switch (comprobacion) {
			case COMPARACION_PORCENTAJES_IGUALES:
				check = Double.compare(acum, MAXIMO_VALOR_PORCENTAGE) == 0;
				break;
			case COMPARACION_PORCENTAJES_MENOS_O_IGUAL:
				check = Double.compare(acum, MAXIMO_VALOR_PORCENTAGE) <= 0;
				break;
			default:
				throw new UVException("Tipo de comprobación no válida");
		}
		
		return check;
	}
	
	/**
	 * Si se pasa apartado, suman su valor, no su valor de la tabla.
	 * @param apartado .
	 * @return .
	 * @throws SQLException .
	 */
	private Double getSumanPorcentajesApartadosActivos(ApartadoBaremacion apartado) throws SQLException {
		String sql = ""
				+ " SELECT SUM(bepapa.PORCENTAJEMAXIMO) AS SUMA "
				+ " FROM TBEP_APARTADOSBAREMACION bepapa "
				+ " WHERE 1=1 "
				+ " 	AND bepapa.FLGACTIVO != 'N' ";
		
		Double acum = 0.0;
		
		if (apartado != null) {
			if (apartado.getPorcentajeMaximo() != null) {
				acum = apartado.getPorcentajeMaximo();
			}
			
			if (apartado.getCodNum() != null) {
				sql += " AND bepapa.CODNUM != ? ";	
			}
		}
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			
			if (apartado != null && apartado.getCodNum() != null) {
				stmt.setInt(parameterIndex++, apartado.getCodNum());
			}
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					acum += rs.getDouble("SUMA");
				}
			}
		}
		
		return acum;
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
	
	/**
	 * Crea un apartado de baremación a partir del resultset.
	 * 
	 * @param rs .
	 * @return .
	 * @throws SQLException .
	 */
	public ApartadoBaremacion createApartadoFromResultSet(ResultSet rs) throws SQLException {
		ApartadoBaremacion apartado = new ApartadoBaremacion();
		apartado.setCodNum(rs.getInt(CODNUM));
		apartado.setCodigo(rs.getString(CODIGO));
		apartado.setNombre(rs.getString("NOMBRE"));
		apartado.setPuntuacionMaxima(Double.compare(rs.getDouble("PUNTUACIONMAXIMA"), 0.0) == 0 ? null : rs.getDouble("PUNTUACIONMAXIMA"));
		apartado.setPorcentajeMaximo(Double.compare(rs.getDouble("PORCENTAJEMAXIMO"), 0.0) == 0 ? null : rs.getDouble("PORCENTAJEMAXIMO"));
		apartado.setActivo("S".equals(rs.getString("FLGACTIVO")));
		return apartado;
	}
	
	private void activaDesactivaApartado(ApartadoBaremacion apartado, boolean activo, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		// si voy a activar comprobar que la suma no supera
		if (activo && this.checkSumaPorcentagesApartadosSuperaMaximo(apartado)) {
			throw new UVException(ERROR_SUMA_FACTORES_INVALIDA);
		}
		
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
		
		if (apartado.getPorcentajeMaximo() == null) {
			throw new UVException("Introduce los factores");
		}
		
		if (this.checkSumaPorcentagesApartadosSuperaMaximo(apartado)) {
			throw new UVException(ERROR_SUMA_FACTORES_INVALIDA);
		}		
	}
	
	/**
	 * Comprueba si el apartado está usandose en algun mérito.
	 * 
	 * @param apartado .
	 * @return .
	 * @throws SQLException .
	 */
	/*
	private boolean chequearUsandose(ApartadoBaremacion apartado) throws SQLException {
		String sql = ""
			+ " SELECT COUNT(*) AS TOTAL "
			+ " FROM TBEP_APARTADOSBAREMACION bepapa "
			+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.BEPAPA_CODNUM = bepapa.CODNUM "
			+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.BEPBLO_CODNUM = bepblo.CODNUM "
			+ " INNER JOIN TBEP_MERITOS bepmer ON bepmer.BEPITE_CODNUM = bepite.CODNUM "			
			+ " WHERE bepapa.CODNUM = ? ";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, apartado.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("TOTAL") > 0;
				}
			}
		}
		
		return false;
	}*/
}
