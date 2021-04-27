package es.ujaen.uvirtual.modelo.bolsaempleo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ApartadoBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.BloqueBaremacion;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.ItemBaremacion;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.utilidades.UVException;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoDataTable.DataTableColumn;

/**
 * Clase de modelo para la gestión de items de baremación.
 * 
 * @author ATISoluciones 2021 
 */
public class ModeloBaremacion {
	// ordenación apartados generales de baremación
	public static final int ORDER_COLUMN_INDEX_APARTADOS_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_PUNTUACIONMAXIMA = 2;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_PORCENTAJEMAXIMO = 3;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_MERITOSPREFERENTES = 4;
	public static final int ORDER_COLUMN_INDEX_APARTADOS_ACTIVO = 5;
	
	// ordenación bloques de baremación
	public static final int ORDER_COLUMN_INDEX_BLOQUES_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_BLOQUES_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_BLOQUES_NUMERO_MAXIMO_MERITOS = 2;
	public static final int ORDER_COLUMN_INDEX_BLOQUES_ACTIVO = 3;
	 
	// ordenación ítems de baremación
	public static final int ORDER_COLUMN_INDEX_ITEMS_CODIGO = 0;
	public static final int ORDER_COLUMN_INDEX_ITEMS_NOMBRE = 1;
	public static final int ORDER_COLUMN_INDEX_ITEMS_UNIDADES = 2;
	public static final int ORDER_COLUMN_INDEX_ITEMS_VALOR = 3;
	public static final int ORDER_COLUMN_INDEX_ITEMS_VALOR_MINIMO = 4; 
	public static final int ORDER_COLUMN_INDEX_ITEMS_VALOR_MAXIMO = 5;
	public static final int ORDER_COLUMN_INDEX_ITEMS_AFINIDAD = 6;
	public static final int ORDER_COLUMN_INDEX_ITEMS_ACTIVO = 7;
	
	// tipos de unidades de los items
	public static final String ITEM_UNIDADES_UNIDADES = "UNIDADES";
	public static final String ITEM_UNIDADES_PUNTOS = "PUNTOS";
	public static final String ITEM_UNIDADES_CREDITOS = "CREDITOS";
	public static final String ITEM_UNIDADES_MESES = "MESES";
	public static final String ITEM_UNIDADES_ANIOS = "ANIOS";
		
	// errores
	public static final String ERROR_APARTADO_NOEXITE = "Apartado no encontrado";
	public static final String ERROR_ITEM_NOEXITE = "Item no encontrado";
	public static final String ERROR_BLOQUE_NOEXITE = "Bloque no encontrado";
	
	public static final String ERROR_APARTADO_OBTENIENDO_TOTAL = "No hay conteo de apartados";
	public static final String ERROR_PUNTUACION_PORCENTAJE_MAXIMO = "Debe introducir una puntuación o un porcentaje máximo, pero no ambos";
	public static final String ERROR_APARTADO_EXISTEOTROCONMISMOCODIGO = "Ya existe otro apartado activo con el código introducido";
	public static final String ERROR_APARTADO_EXISTEN_APARTADOS_CON_PORCENTAJE = "Existen apartados que se evaluan con porcentaje";
	public static final String ERROR_APARTADO_EXISTEN_APARTADOS_CON_PUNTUACION = "Existen apartados que se evaluan con puntuación";
	
	public static final Integer COLUMN_CODIGO_MAXLENGTH = 3; // logitud máxima de los códigos
	public static final Integer COLUMN_NOMBRE_MAXLENGTH = 100; // logitud máxima de los nombres
	
	public static final Float MINIMO_VALOR_FLOAT = (float) 0.01;
	
    protected static ModeloBaremacion eInstancia = null;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloBaremacion();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloBaremacion obtenerInstancia() {
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
			throw new UVException("El bloque es requerido");	
		}
			
		String sql = "SELECT bepapa.* FROM TBEP_APARTADOSBAREMACION bepapa WHERE bepapa.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return this.createApartadoFromResultSet(rs);					
				}
			}
		}
		
		throw new UVException(ERROR_APARTADO_NOEXITE);		
	}
	
    /** Consulta para obtener el último código de los apartados .
	 * @return apartado .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public String getUltimoCodigoApartado() throws SQLException, UVException {
		String consulta = "SELECT bepapa.CODIGO FROM TBEP_APARTADOSBAREMACION bepapa "
				+ " ORDER BY bepapa.CODIGO DESC"
				+ " FETCH FIRST 1 ROW ONLY";
		String ultimoCodigo = "";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					ultimoCodigo = "1";		
				} else {
					ultimoCodigo = rs.getString("CODIGO");					
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
		BolsaEmpleoDataTable<ApartadoBaremacion> dataTable = new BolsaEmpleoDataTable<ApartadoBaremacion>(params);
		
		String consulta =
			"SELECT bepapa.* "
		  + "FROM TBEP_APARTADOSBAREMACION bepapa "		  
		  + "WHERE 1=1 ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_CODIGO, "bepapa.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_NOMBRE, "bepapa.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_PUNTUACIONMAXIMA, "bepapa.PUNTUACIONMAXIMA");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_PORCENTAJEMAXIMO, "bepapa.PORCENTAJEMAXIMO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_MERITOSPREFERENTES, "bepapa.MERITOS_PREFERENTES", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(ORDER_COLUMN_INDEX_APARTADOS_ACTIVO, "bepapa.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
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
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void insertaApartado(ApartadoBaremacion apartado) throws SQLException, UVException {
		this.chequearApartadoParaInsertarOActualizar(apartado);
		
		String consulta = "INSERT INTO TBEP_APARTADOSBAREMACION (CODIGO,NOMBRE,FLGACTIVO,PUNTUACIONMAXIMA,PORCENTAJEMAXIMO) "
				+ " VALUES (?,?,?,?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, apartado.getCodigo());
			stmt.setString(parameterIndex++, apartado.getNombre());
			stmt.setString(parameterIndex++, apartado.getActivo() ? "S" : "N");
			
			if (apartado.getPuntuacionMaxima() != null) {
				stmt.setFloat(parameterIndex++, apartado.getPuntuacionMaxima());
				stmt.setNull(parameterIndex++, Types.NULL);
			} else if (apartado.getPorcentajeMaximo() != null) {
				stmt.setNull(parameterIndex++, Types.NULL);
				stmt.setFloat(parameterIndex++, apartado.getPorcentajeMaximo());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
				stmt.setNull(parameterIndex++, Types.NULL);				
			}
			
			stmt.executeUpdate();
		}
	}
		
	/** 
	 * Desactiva un apartado de baremación.
	 * @param apartado .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void desactivarApartado(ApartadoBaremacion apartado) throws SQLException, UVException {
		this.activaDesactivaApartado(apartado, false);
	}
	
	/** 
	 * Actia un apartado de baremación.
	 * @param apartado .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void activarApartado(ApartadoBaremacion apartado) throws SQLException, UVException {
		this.activaDesactivaApartado(apartado, true);
	}
	
	/** Actualiza un apartado .
	 * @param apartado con los datos nuevos a actualizar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de errores de validacion .
	 */
	public void actualizaApartado(ApartadoBaremacion apartado) throws SQLException, UVException {
		this.chequearApartadoParaInsertarOActualizar(apartado);
		
		String consulta = "UPDATE TBEP_APARTADOSBAREMACION SET "
				+ "CODIGO = ?, "
				+ "NOMBRE = ?, "
				+ "FLGACTIVO = ?, "
				+ "PUNTUACIONMAXIMA = ?, "
				+ "PORCENTAJEMAXIMO = ? "
				+ "WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, apartado.getCodigo());
			stmt.setString(parameterIndex++, apartado.getNombre());
			stmt.setString(parameterIndex++, apartado.getActivo() ? "S" : "N");
			
			if (apartado.getPuntuacionMaxima() != null) {
				stmt.setFloat(parameterIndex++, apartado.getPuntuacionMaxima());
				stmt.setNull(parameterIndex++, Types.NULL);
			} else if (apartado.getPorcentajeMaximo() != null) {
				stmt.setNull(parameterIndex++, Types.NULL);
				stmt.setFloat(parameterIndex++, apartado.getPorcentajeMaximo());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
				stmt.setNull(parameterIndex++, Types.NULL);				
			}
			
			stmt.setInt(parameterIndex++, apartado.getCodNum());
			stmt.executeUpdate();
		}		
	}
		
	/** lista todos los apartados activos.
	 * @return vector con todos los apartados .
	 * @throws SQLException si hay un error en la base de datos .
	 * @throws UVException .
	 */
	public List<ApartadoBaremacion> getApartadosActivos() throws SQLException, UVException {
		List<ApartadoBaremacion> apartados = new ArrayList<>();
		
		String consulta =
				"SELECT bepapa.* "
			  + "FROM TBEP_APARTADOSBAREMACION bepapa "		  
			  + "WHERE bepapa.FLGACTIVO = 'S'";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
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
	public List<ApartadoBaremacion> listaApartadoBaremacion() throws SQLException, UVException {
		return listaApartadoBaremacion(" WHERE FLGACTIVO = 'S' ORDER BY nombre");
	} 
	
	private List<ApartadoBaremacion> listaApartadoBaremacion(String clausula) throws SQLException, UVException {
		List<ApartadoBaremacion> items = new ArrayList<>();
		String consulta = "SELECT bepapa.* FROM TBEP_APARTADOSBAREMACION bepapa " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
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
		apartado.setCodNum(rs.getInt("CODNUM"));
		apartado.setCodigo(rs.getString("CODIGO"));
		apartado.setNombre(rs.getString("NOMBRE"));
		apartado.setPuntuacionMaxima(rs.getFloat("PUNTUACIONMAXIMA") == 0 ? null : rs.getFloat("PUNTUACIONMAXIMA"));
		apartado.setPorcentajeMaximo(rs.getFloat("PORCENTAJEMAXIMO") == 0 ? null : rs.getFloat("PORCENTAJEMAXIMO"));
		apartado.setActivo(rs.getString("FLGACTIVO").equals("S"));
		return apartado;
	}
	
	private void activaDesactivaApartado(ApartadoBaremacion apartado, Boolean activo) throws SQLException, UVException {
		String consulta = "UPDATE TBEP_APARTADOSBAREMACION SET FLGACTIVO = ? WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			
			stmt.setString(parameterIndex++, activo ? "S" : "N");
			stmt.setInt(parameterIndex++, apartado.getCodNum());
			stmt.executeUpdate();
		}	
	}
	
	private boolean existeOtroApartadoActivoPorCodigo(ApartadoBaremacion apartado) throws SQLException {		
		String sql = "SELECT bepapa.* "
				+ "FROM TBEP_APARTADOSBAREMACION bepapa "
				+ "WHERE bepapa.CODIGO = ? "
				+ "AND bepapa.FLGACTIVO = 'S' ";
		
		if (apartado.getCodNum() != null) {
			sql += " AND bepapa.CODNUM <> ? ";
		}
		
		sql += " FETCH FIRST 1 ROW ONLY";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, apartado.getCodigo());
			
			if (apartado.getCodNum() != null) {
				stmt.setInt(parameterIndex++, apartado.getCodNum());
			}
						
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return true;									
				}
			}
		}
		
		return false;
	} 
	
	private void chequearApartadoParaInsertarOActualizar(ApartadoBaremacion apartado) throws SQLException, UVException {
		if (this.existeOtroApartadoActivoPorCodigo(apartado)) {
			throw new UVException("Ya existe un apartado con el código introducido");
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
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
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
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
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
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS BLOQUEBAREMACION  ********************************************/
	
	/**
	 * Devuelve un bloque de baremación por su id.
	 * @param codNum .
	 * @return .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si bloque no es existe
	 */
	public BloqueBaremacion getBloqueBaremacionById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException("El bloque de baremación es requerido");
		}
		
		String sql = "SELECT bepblo.* FROM TBEP_BLOQUESBAREMACION bepblo WHERE bepblo.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return this.createBloqueFromResultSet(rs);
				}
			}
		}
		
		throw new UVException(ERROR_BLOQUE_NOEXITE);	
	}
	
	/**
	 * Listado de bloques de baremación . 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param apartado id del apartado .
	 * @return listado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<BloqueBaremacion> listadoBloquesBaremacionDatatable(Map<String, String[]> params, ApartadoBaremacion apartado) 
			throws SQLException, UVException {
		
		List<BloqueBaremacion> bloques = new ArrayList<>();
		BolsaEmpleoDataTable<BloqueBaremacion> dataTable = new BolsaEmpleoDataTable<BloqueBaremacion>(params);
		
		String consulta = "SELECT bepblo.* "
				+ "FROM TBEP_BLOQUESBAREMACION bepblo "
				+ "WHERE bepblo.BEPAPA_CODNUM = ? ";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_CODIGO, "bepblo.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_NOMBRE, "bepblo.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_NUMERO_MAXIMO_MERITOS, "bepblo.NUMERO_MAXIMO_MERITOS");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUES_ACTIVO, "bepblo.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, apartado.getCodNum());
			stmtCount.setInt(indexParam++, apartado.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bloques.add(this.createBloqueFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bloques);
		}
		
		return dataTable;
	}

	/** 
	 * Desactiva un bloque de baremación.
	 * @param bloque .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void desactivarBloque(BloqueBaremacion bloque) throws SQLException, UVException {
		this.activaDesactivaBloque(bloque, false);
	}
	
	/** 
	 * Actia un apartado de baremación.
	 * @param bloque .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void activarBloque(BloqueBaremacion bloque) throws SQLException, UVException {
		this.activaDesactivaBloque(bloque, true);
	}
	
	/** Actualiza un bloque .
	 * @param bloque con los datos nuevos a actualizar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de errores de validacion .
	 */
	public void actualizaBloque(BloqueBaremacion bloque) throws SQLException, UVException {
		this.chequearBloqueParaInsertarOActualizar(bloque);
		
		String consulta = "UPDATE TBEP_BLOQUESBAREMACION SET "
				+ "CODIGO = ?, "
				+ "NOMBRE = ?, "
				+ "FLGACTIVO = ?, "
				+ "NUMERO_MAXIMO_MERITOS = ? "
				+ "WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, bloque.getCodigo());
			stmt.setString(parameterIndex++, bloque.getNombre());
			stmt.setString(parameterIndex++, bloque.isActivo() ? "S" : "N");
			
			if (bloque.getNumeroMaximoMeritos() != null) {
				stmt.setInt(parameterIndex++, bloque.getNumeroMaximoMeritos());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			
			stmt.setInt(parameterIndex++, bloque.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Función que inserta un bloque en la BD.
	 * @param bloque .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void insertaBloque(BloqueBaremacion bloque) throws SQLException, UVException {
		this.chequearBloqueParaInsertarOActualizar(bloque);
		
		String consulta = "INSERT INTO TBEP_BLOQUESBAREMACION " 
				+ " (CODIGO,NOMBRE,BEPAPA_CODNUM,NUMERO_MAXIMO_MERITOS)"
				+ " VALUES (?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, bloque.getCodigo());
			stmt.setString(parameterIndex++, bloque.getNombre());
			stmt.setInt(parameterIndex++, bloque.getApartadoBaremacion().getCodNum());
			if (bloque.getNumeroMaximoMeritos() != null) {
				stmt.setInt(parameterIndex++, bloque.getNumeroMaximoMeritos());
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}
			stmt.executeUpdate();
		}
	}
		
	/** Consulta para obtener el último código de los bloques de un apartado .
	 * @param apartado .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public String getUltimoCodigoBloque(ApartadoBaremacion apartado) throws SQLException, UVException {
		String consulta = "SELECT bepblo.CODIGO, bepblo.BEPAPA_CODNUM FROM TBEP_BLOQUESBAREMACION bepblo "
				+ " WHERE bepblo.BEPAPA_CODNUM = ? ORDER BY bepblo.CODIGO DESC"
				+ " FETCH FIRST 1 ROW ONLY";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, apartado.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return "1";
				} else {
					return rs.getString("CODIGO");
				}
			}
		}
	}
	
	/**
	 * Devuelve los bloques de bareamación activos.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<BloqueBaremacion> listaBloqueBaremacion() throws SQLException, UVException {
		return listaBloqueBaremacion(" WHERE FLGACTIVO = 'S' ORDER BY nombre");
	} 
	
	private List<BloqueBaremacion> listaBloqueBaremacion(String clausula) throws SQLException, UVException {
		List<BloqueBaremacion> items = new ArrayList<>();
		String consulta = "SELECT bepblo.* FROM TBEP_BLOQUESBAREMACION bepblo " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						items.add(this.createBloqueFromResultSet(rs));
					}
				}
			}
		return items;
	}
	
	private BloqueBaremacion createBloqueFromResultSet(ResultSet rs) throws SQLException, UVException {
		BloqueBaremacion obj = new BloqueBaremacion();
		obj.setCodNum(rs.getInt("CODNUM"));
		obj.setCodigo(rs.getString("CODIGO"));
		obj.setNombre(rs.getString("NOMBRE"));
		obj.setActivo(rs.getString("FLGACTIVO").equals("S"));
		obj.setNumeroMaximoMeritos(rs.getInt("NUMERO_MAXIMO_MERITOS") == 0 ? null : rs.getInt("NUMERO_MAXIMO_MERITOS"));
		obj.setApartadoBaremacion(this.getApartadoBaremacionById(rs.getInt("BEPAPA_CODNUM")));
		return obj;
	}
	
	private void chequearBloqueParaInsertarOActualizar(BloqueBaremacion bloque) throws SQLException, UVException {
		if (this.existeOtroBloqueActivoPorCodigo(bloque)) {
			throw new UVException("Ya existe un bloque con el código introducido");
		}		
	}

	private void activaDesactivaBloque(BloqueBaremacion bloque, Boolean activo) throws SQLException, UVException {
		String consulta = "UPDATE TBEP_BLOQUESBAREMACION SET FLGACTIVO = ? WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			
			stmt.setString(parameterIndex++, activo ? "S" : "N");
			stmt.setInt(parameterIndex++, bloque.getCodNum());
			stmt.executeUpdate();
		}	
	}
	
	private boolean existeOtroBloqueActivoPorCodigo(BloqueBaremacion bloque) throws SQLException {		
		String sql = "SELECT bepblo.* "
				+ "FROM TBEP_BLOQUESBAREMACION bepblo "
				+ "WHERE 1=1 "
				+ "AND bepblo.BEPAPA_CODNUM = ? "
				+ "AND bepblo.CODIGO = ? "
				+ "AND bepblo.FLGACTIVO = 'S' ";
		
		if (bloque.getCodNum() != null) {
			sql += " AND bepblo.CODNUM <> ? ";
		}
		
		sql += " FETCH FIRST 1 ROW ONLY";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, bloque.getApartadoBaremacion().getCodNum());
			stmt.setString(parameterIndex++, bloque.getCodigo());
			
			if (bloque.getCodNum() != null) {
				stmt.setInt(parameterIndex++, bloque.getCodNum());
			}
						
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return true;									
				}
			}
		}
		
		return false;
	} 
	
	/********************************************** METODOS PÚBLICOS PARA CONSULTAS BLOQUEBAREMACION  ********************************************/	

	/**
	 * Devuelve un item de baremación por su id.
	 * @param codNum .
	 * @return .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si bloque no es existe
	 */
	public ItemBaremacion getItemBaremacionById(Integer codNum) throws SQLException, UVException {
		if (codNum == null) {
			throw new UVException("El mérito es requerido");
		}
		
		String sql = "SELECT bepite.* FROM TBEP_ITEMSBAREMACION bepite WHERE bepite.CODNUM = ?";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, codNum);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return this.createItemFromResultSet(rs);					
				}
			}
		}
		
		throw new UVException(ERROR_ITEM_NOEXITE);			
	}
	
	/**
	 * Listado de ítems de baremación . 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param bloque id del bloque .
	 * @return listado .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe la area .
	 */
	public BolsaEmpleoDataTable<ItemBaremacion> listadoItemsBaremacionDatatable(Map<String, String[]> params, BloqueBaremacion bloque) throws SQLException, UVException {
		List<ItemBaremacion> items = new ArrayList<>();
		BolsaEmpleoDataTable<ItemBaremacion> dataTable = new BolsaEmpleoDataTable<ItemBaremacion>(params);
		
		String consulta = "SELECT bepite.* "
				+ "FROM TBEP_ITEMSBAREMACION bepite "
				+ "INNER JOIN TBEP_AFINIDADES bepafi ON bepafi.CODNUM = bepite.AFINIDAD "
				+ "WHERE bepite.BEPBLO_CODNUM = ?";
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_CODIGO, "bepite.CODIGO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_NOMBRE, "bepite.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_UNIDADES, "bepite.UNIDADES");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_VALOR, "bepite.VALOR");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_VALOR_MINIMO, "bepite.VALOR_MINIMO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_VALOR_MAXIMO, "bepite.VALOR_MAXIMO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_AFINIDAD, "bepite.VALOR_AFINIDAD");		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEMS_ACTIVO, "bepite.FLGACTIVO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, bloque.getCodNum());
			stmtCount.setInt(indexParam++, bloque.getCodNum());
			
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {					
					items.add(this.createItemFromResultSet(rs));
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(items);
		}
		
		return dataTable;
	}
	
	/** Consulta para obtener el último código de los items de un bloque .
	 * @param bloque .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public String getUltimoCodigoItem(BloqueBaremacion bloque) throws SQLException, UVException {		
		String consulta = "SELECT bepite.CODIGO, bepite.BEPBLO_CODNUM "
				+ "FROM TBEP_ITEMSBAREMACION bepite "
				+ "WHERE bepite.BEPBLO_CODNUM = ? "
				+ "ORDER BY bepite.CODIGO DESC "
				+ "FETCH FIRST 1 ROW ONLY";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, bloque.getCodNum());
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return "1";
				} else {
					return rs.getString("CODIGO");
				}
			}
		}
	}
	
	/** Actualiza un item .
	 * @param item con los datos nuevos a actualizar .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de errores de validacion .
	 */
	public void actualizaItem(ItemBaremacion item) throws SQLException, UVException {
		this.chequearItemParaInsertarOActualizar(item);
		
		String consulta = "UPDATE TBEP_ITEMSBAREMACION SET "
				+ "CODIGO = ?, "
				+ "NOMBRE = ?, "
				+ "FLGACTIVO = ?, "
				+ "UNIDADES = ?, "
				+ "VALOR = ?, "
				+ "VALOR_MINIMO = ?, "
				+ "VALOR_MAXIMO = ?, "
				+ "AFINIDAD = ? "
				+ "WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, item.getCodigo());
			stmt.setString(parameterIndex++, item.getNombre());
			stmt.setString(parameterIndex++, item.getActivo() ? "S" : "N");
			stmt.setString(parameterIndex++, item.getUnidades());
			stmt.setFloat(parameterIndex++, item.getValor());
			stmt.setFloat(parameterIndex++, item.getValorMinimo());
			stmt.setFloat(parameterIndex++, item.getValorMaximo());
			stmt.setInt(parameterIndex++, item.getAfinidad());			
			stmt.setInt(parameterIndex++, item.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** 
	 * Desactiva un item de baremación.
	 * @param item .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void desactivarItem(ItemBaremacion item) throws SQLException, UVException {
		this.activaDesactivaItem(item, false);
	}
	
	/** 
	 * Activa un item de baremación.
	 * @param item .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void activarItem(ItemBaremacion item) throws SQLException, UVException {
		this.activaDesactivaItem(item, true);
	}

	/** Función que inserta un ítem en la BD.
	 * @param item .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void insertaItem(ItemBaremacion item) throws SQLException, UVException {
		this.chequearItemParaInsertarOActualizar(item);
		
		String consulta = "INSERT INTO TBEP_ITEMSBAREMACION "
				+ " (CODIGO,NOMBRE,BEPBLO_CODNUM,UNIDADES,VALOR,VALOR_MINIMO,VALOR_MAXIMO,AFINIDAD)"
				+ " VALUES (?,?,?,?,?,?,?,?)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, item.getCodigo());
			stmt.setString(parameterIndex++, item.getNombre());
			stmt.setInt(parameterIndex++, item.getBloqueBaremacion().getCodNum());
			stmt.setString(parameterIndex++, item.getUnidades());
			stmt.setFloat(parameterIndex++, item.getValor());
			stmt.setFloat(parameterIndex++, item.getValorMinimo());
			stmt.setFloat(parameterIndex++, item.getValorMaximo());
			stmt.setInt(parameterIndex++, item.getAfinidad());
			stmt.executeUpdate();
		}
	}
	
	/** Lista todos los ítems de un apartado.
	 * @param apartado .
	 * @return vector con todos los ítems .
	 * @throws SQLException si hay un error en la base de datos .
	 * @throws UVException .
	 */
	public List<ItemBaremacion> getItemsDeApartado(ApartadoBaremacion apartado) throws SQLException, UVException {
		List<ItemBaremacion> items = new ArrayList<>();
		
		String consulta = "SELECT bepite.* "
				+ "FROM TBEP_ITEMSBAREMACION bepite "
				+ "INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM "
				+ "WHERE bepblo.BEPAPA_CODNUM = ? ";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, apartado.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ItemBaremacion item = this.getItemBaremacionById(rs.getInt("CODNUM")); 							
					items.add(item);
				}
			}
		}
		
		return items;
	}

	/**
	 * Devuelve los items de bareamación activos.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<ItemBaremacion> listaItemBaremacion() throws SQLException, UVException {
		return listaItemBaremacion(" WHERE FLGACTIVO = 'S' ORDER BY nombre");
	} 
	
	private List<ItemBaremacion> listaItemBaremacion(String clausula) throws SQLException, UVException {
		List<ItemBaremacion> items = new ArrayList<>();
		String consulta = "SELECT bepite.* FROM TBEP_ITEMSBAREMACION bepite " + clausula;
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						items.add(this.createItemFromResultSet(rs));
					}
				}
			}
		return items;
	}
	
	private ItemBaremacion createItemFromResultSet(ResultSet rs) throws SQLException, UVException {
		ModeloAfinidad modelo = ModeloAfinidad.obtenerInstancia();	
		ItemBaremacion item = new ItemBaremacion();
		item.setCodNum(rs.getInt("CODNUM"));
		item.setCodigo(rs.getString("CODIGO"));
		item.setNombre(rs.getString("NOMBRE"));
		item.setActivo(rs.getString("FLGACTIVO").equals("S"));
		item.setBloqueBaremacion(this.getBloqueBaremacionById(rs.getInt("BEPBLO_CODNUM")));
		item.setUnidades(rs.getString("UNIDADES"));
		item.setValor(rs.getFloat("VALOR"));
		item.setValorMinimo(rs.getFloat("VALOR_MINIMO"));
		item.setValorMaximo(rs.getFloat("VALOR_MAXIMO"));
		item.setAfinidad(rs.getInt("AFINIDAD"));
		item.setAfinidadOBJ(modelo.getAfinidadById(rs.getInt("AFINIDAD")));
		return item;
	}
	
	private void activaDesactivaItem(ItemBaremacion item, Boolean activo) throws SQLException, UVException {
		String consulta = "UPDATE TBEP_ITEMSBAREMACION SET FLGACTIVO = ? WHERE CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			
			stmt.setString(parameterIndex++, activo ? "S" : "N");
			stmt.setInt(parameterIndex++, item.getCodNum());
			stmt.executeUpdate();
		}	
	}
	
	private void chequearItemParaInsertarOActualizar(ItemBaremacion item) throws SQLException, UVException {
		if (this.existeOtroItemActivoPorCodigo(item)) {
			throw new UVException("Ya existe un item con el código introducido");
		}
		
		ArrayList<String> unidades = new ArrayList<String>();
		unidades.add(ITEM_UNIDADES_UNIDADES);
		unidades.add(ITEM_UNIDADES_PUNTOS);
		unidades.add(ITEM_UNIDADES_CREDITOS);
		unidades.add(ITEM_UNIDADES_MESES);
		unidades.add(ITEM_UNIDADES_ANIOS);
			
		if (!unidades.contains(item.getUnidades())) {
			throw new UVException("Tipo de unidad no válida");
		}
		
		if (item.getValorMinimo() < MINIMO_VALOR_FLOAT) {
			throw new UVException("El valor mínimo deber al menos " + MINIMO_VALOR_FLOAT);
		}
		
		if (item.getValorMaximo() < MINIMO_VALOR_FLOAT) {
			throw new UVException("El valor máximo deber al menos " + MINIMO_VALOR_FLOAT);
		}
		
		if (item.getValorMinimo() > item.getValorMaximo()) {
			throw new UVException("El valor máximo deber mayor que el valor mínimo");
		}
	}

	private boolean existeOtroItemActivoPorCodigo(ItemBaremacion item) throws SQLException {		
		String sql = "SELECT bepite.* "
				+ "FROM TBEP_ITEMSBAREMACION bepite "
				+ "WHERE 1=1 "
				+ "AND bepite.BEPBLO_CODNUM = ? "
				+ "AND bepite.CODIGO = ? "
				+ "AND bepite.FLGACTIVO = 'S' ";
		
		if (item.getCodNum() != null) {
			sql += " AND bepite.CODNUM <> ? ";
		}
		
		sql += " FETCH FIRST 1 ROW ONLY";
		
		try (Connection con = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = con.prepareStatement(sql);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, item.getBloqueBaremacion().getCodNum());
			stmt.setString(parameterIndex++, item.getCodigo());
			
			if (item.getCodNum() != null) {
				stmt.setInt(parameterIndex++, item.getCodNum());
			}
						
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					return true;									
				}
			}
		}
		
		return false;
	} 
		
}
