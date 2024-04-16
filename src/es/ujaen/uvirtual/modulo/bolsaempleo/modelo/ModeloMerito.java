package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Convocatoria;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Merito;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.UVException;
import es.ujaen.uvirtual.utilidades.ficheros.FileSystemUtils;

/**
 * Clase de modelo para la gestión de meritos. 
 * @author ATISoluciones 2021
 */
public class ModeloMerito {
	
	private static final Logger ELOGGER = Logger.getLogger(ModeloMerito.class.getName());
	
	public static final int ORDER_COLUMN_INDEX_ID = 1;
	public static final int ORDER_COLUMN_INDEX_BLOQUE = 2;
	public static final int ORDER_COLUMN_INDEX_ITEM = 3;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_ITEM = 4;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION = 5;
	public static final int ORDER_COLUMN_INDEX_VALOR = 6;
	public static final int ORDER_COLUMN_INDEX_OBSERVACION = 7;
	
	public static final int ORDER_COLUMN_INDEX_ID_CANDIDATO = 0;
	public static final int ORDER_COLUMN_INDEX_BLOQUE_CANDIDATO = 1;
	public static final int ORDER_COLUMN_INDEX_ITEM_CANDIDATO = 2;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_ITEM_CANDIDATO = 3;
	public static final int ORDER_COLUMN_INDEX_DESCRIPCION_CANDIDATO = 4;
	public static final int ORDER_COLUMN_INDEX_VALOR_CANDIDATO = 5;
	public static final int ORDER_COLUMN_INDEX_OBSERVACION_CANDIDATO = 6;
	
	public static final int COLUMN_DESCRIPCION_MAXLENGTH = 200;
	public static final int COLUMN_OBSERVACION_MAXLENGTH = 300;
	
	// mensajes
	public static final String MENSAJE_ERROR_VALOR_ENTERO_NO_PERMITIDO = "El valor debe ser entero para %s";
	public static final String MENSAJE_ERROR_VALOR_DECIMAL_NO_PERMITIDO = "El valor debe ser decimal para %s";
	public static final String MENSAJE_ERROR_VALOR_MAXIMO_PERMITIDO = "El valor máximo permitido es %s para %s";
	public static final String MENSAJE_ERROR_VALOR_MINIMO_PERMITIDO = "El valor mínimo permitido es %s para %s";
	
	// paso a ficheros
	public static final String ESQUEMA_TBEP_MERITOS = "G_INTRANET";
	public static final String TABLA_TBEP_MERITOS = "TBEP_MERITOS";
	public static final String COLUMNA_TBEP_MERITOS = "ARCHIVO";
	public static final String ID_TBEP_MERITOS = "CODNUM";

	protected static ModeloMerito eInstancia;

	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloMerito();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
	public static ModeloMerito obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}
	
	/** Consulta méritos en BBDD y los devuelve .
	 * @param id para devolver un mérito .
	 * @return lista de los méritos de la base de datos .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException .
	 */
	private List<Merito> listaMeritos(Integer id) throws SQLException, UVException {
		List<Merito> meritos = new ArrayList<>();
		String consulta = "SELECT bepmer.* FROM tbep_meritos bepmer ";
		
		if (id != null) {
			consulta += "WHERE codnum = ?";
		}
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			if (id != null) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, id);
			}
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Merito mer = this.createMeritoFromResultset(rs, true, true); 								
					meritos.add(mer);
				}
			}
			}
		return meritos;
	}
	
	/** lista todos los méritos de la BBDD .
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public List<Merito> listaMeritos() throws SQLException, UVException {
		return listaMeritos(null);
	}
	
	/** obtiene un mérito a partir de su id.
	 * @param id del mérito .
	 * @return archivo con el id especificado .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public Merito listaMerito(Integer id) throws SQLException, UVException {
		List<Merito> meritos = listaMeritos(id);
		if (meritos.isEmpty()) {
			throw new UVException("No existe mérito");
		}
		return meritos.get(0);
	}
	
	/**	Función que elimina méritos .
	 * @param meritos a eliminar .
	 * @param usuarioUpdate .
	 * @throws SQLException en caso de error en la BD .
	 */
	public void eliminarMeritos(List<Merito> meritos, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			
			try {
				String params = BolsaEmpleoUtils.consultaMultiplesParametros(meritos.size());
				
				// actualiza el usuario de los méritos antes de eliminar
				String consultaUpdate = "UPDATE TBEP_MERITOS SET UID_USUARIO = ? WHERE CODNUM IN (" + params + ")";
				
				try (PreparedStatement stmt = conexion.prepareStatement(consultaUpdate)) {
					int indexParam = 1;
					stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
					for (Merito merito: meritos) {
						stmt.setInt(indexParam++, merito.getCodNum());
					}
					stmt.executeUpdate();
				}
				
				// delete usuarios seleccionados
				String consultaDelete = "DELETE FROM TBEP_MERITOS WHERE CODNUM IN (" + params + ")";
				
				try (PreparedStatement stmt = conexion.prepareStatement(consultaDelete)) {
					int indexParam = 1;
					for (Merito merito: meritos) {
						stmt.setInt(indexParam++, merito.getCodNum());
					}
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
	
	/**	Función que inserta un mérito .
	 * @param merito a insertar .
	 * @param usuarioUpdate .
	 * @return id merito insertado.
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public Integer insertaMerito(Merito merito, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (merito == null) {
			throw new UVException("No se puede insertar un mérito vacío");
		}
		if (merito.getDescripcion() == null || "".equals(merito.getDescripcion())) {
			throw new UVException("No se puede insertar un mérito sin descripción");
		}
		if (merito.getValor() == null) {
			throw new UVException("No se puede insertar un mérito sin valor");
		}
		if (merito.getArchivo() == null) {
			throw new UVException("No se puede insertar un mérito sin archivo. Compruebe que es un fichero pdf válido.");
		}
		if (merito.getItemBaremacion().getCodNum() == null) {
			throw new UVException("No se puede insertar un mérito sin ítem de baremación");
		}
		if (usuarioUpdate == null) {
			throw new UVException("No se puede insertar un mérito sin usuario");
		}
		
		String consulta = "INSERT INTO tbep_meritos " 
				+ " (BEPITE_CODNUM,BEPUSU_CODNUM,VALOR,DESCRIPCION,OBSERVACION,ARCHIVO,UID_USUARIO) "
				+ "VALUES (?,?,?,?,?,?,?)";

		Connection conexion = null;
		PreparedStatement stmt = null;
		try {
			conexion = ConexionUvirtual.obtenerInstancia();
			conexion.setAutoCommit(false);
			stmt = conexion.prepareStatement(consulta, new String[]{ID_TBEP_MERITOS});
			
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, merito.getItemBaremacion().getCodNum());
			stmt.setInt(parameterIndex++, usuarioUpdate.getCodNum());
			stmt.setDouble(parameterIndex++, merito.getValor());
			stmt.setString(parameterIndex++, merito.getDescripcion());
			stmt.setString(parameterIndex++, merito.getObservacion());
			
			stmt.setBinaryStream(parameterIndex++, new ByteArrayInputStream("En fichero".getBytes())); // El campo ARCHIVO no puede ser nulo
						
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			int codnum = rs.getInt(1);
			
			FileSystemUtils.grabarFichero(Long.valueOf((long) codnum), ESQUEMA_TBEP_MERITOS, TABLA_TBEP_MERITOS, COLUMNA_TBEP_MERITOS, FileSystemUtils.fromInputStreamToByteArray(merito.getArchivo()));
			conexion.commit();
			
			return codnum;
		} catch(IOException e) {
			ELOGGER.log(Level.SEVERE , "insertaTitulacionUsuario - Error al grabar archivos: " + e.getMessage());
			throw new UVException("Error al grabar el fichero.");
		} finally {
			if (stmt != null) try { stmt.close(); } catch (Exception e2) {};
			if (conexion != null) try {conexion.rollback(); conexion.setAutoCommit(true); conexion.close(); } catch(Exception e2) {};
		}
	}
	
	/**
	 * Comprueba si se puede actualizar un mérito.
	 * @param merito .
	 * @return true o false si se puede modificar o no.
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public boolean comprobarSiSePuedeActualizarMerito(Merito merito) throws SQLException, UVException {
		ModeloConvocatoria modeloConvocatoria = ModeloConvocatoria.obtenerInstancia();
		
		// no se puede modificar un mérito asociado a una solicitud con convocatoria finalizada
		String consultaConvocatoria = "SELECT bepcon.CODNUM "
				+ " FROM TBEP_CONVOCATORIAS bepcon"
				+ " INNER JOIN TBEP_SOLICITUDES bepsol ON bepsol.BEPCON_CODNUM = bepcon.CODNUM"
				+ " INNER JOIN TBEP_USUARIOS bepusu ON bepusu.CODNUM = bepsol.BEPUSU_CODNUM"
				+ " INNER JOIN TBEP_MERITOS bepmer ON bepmer.BEPUSU_CODNUM = bepusu.CODNUM"
				+ " WHERE bepmer.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consultaConvocatoria)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, merito.getCodNum());
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					int codNumConvocatoria = rs.getInt("CODNUM");
					Convocatoria c = modeloConvocatoria.getConvocatoriaById(codNumConvocatoria);
					if (c.getEstado().equals(ModeloConvocatoria.CONVOCATORIA_ESTADO_FINALIZADA)) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/** Función que edita un mérito .
	 * @param merito .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public void actualizaMerito(Merito merito, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		if (merito == null) {
			throw new UVException("No se puede modificar un mérito vacío");
		}
		if (merito.getCodNum() == null) {
			throw new UVException("No se puede modificar un mérito sin id");
		}
		if (merito.getItemBaremacion().getCodNum() == null) {
			throw new UVException("No se puede modificar un mérito sin ítem de baremación");
		}
		if (merito.getValor() == null) {
			throw new UVException("No se puede modificar un mérito sin valor");
		}
		if (usuarioUpdate == null) {
			throw new UVException("No se puede modificar un mérito sin usuario");
		}
		if (!this.comprobarSiSePuedeActualizarMerito(merito)) {
			throw new UVException("No se puede actualizar el mérito ya está asociado a una convocatoria finalizada");
		}
		
		// actualizamos mérito
		String consulta = "UPDATE TBEP_MERITOS bepmer" 
				+ " SET BEPITE_CODNUM = ?, VALOR = ?, UID_USUARIO = ?"
				+ " WHERE bepmer.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, merito.getItemBaremacion().getCodNum());
			stmt.setDouble(parameterIndex++, merito.getValor());			
			stmt.setString(parameterIndex++, usuarioUpdate.getCodCuenta());
			stmt.setInt(parameterIndex++, merito.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Listado de méritos de un usuario .
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param usuario id del usuario .
	 * @return listado de titulaciones .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe titulación .
	 */
	public BolsaEmpleoDataTable<Merito> listaMeritosDatatable(Map<String, String[]> params, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		
		if (usuario == null) {
			throw new UVException("No se pueden listar méritos sin el id del usuario");
		}
		
		List<Merito> meritos = new ArrayList<>();
		BolsaEmpleoDataTable<Merito> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = ""
				+ " SELECT bepmer.*, bepblo.BEPAPA_CODNUM "
				+ " FROM TBEP_MERITOS bepmer "
				+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON (bepite.CODNUM = bepmer.BEPITE_CODNUM AND bepite.FLGACTIVO = 'S') "
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON (bepblo.CODNUM = bepite.BEPBLO_CODNUM AND bepblo.FLGACTIVO = 'S') "
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON (bepapa.CODNUM  = bepblo.BEPAPA_CODNUM AND bepapa.FLGACTIVO = 'S') "
				+ " WHERE bepmer.BEPUSU_CODNUM = ? ";
		
		String whereCodigo = String.format("(%s || '.' || %s || '.' || %s)", "bepapa.CODIGO", "bepblo.CODIGO", "bepite.CODIGO");
		String orderCodigo = String.format("(%s || '.' || %s || '.' || %s) %%s", "LPAD(bepapa.CODIGO, 3)", "LPAD(bepblo.CODIGO, 3)", "LPAD(bepite.CODIGO, 3)");
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID, "bepmer.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUE, "bepblo.BEPAPA_CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEM, whereCodigo, DataTableColumn.COLUMN_TYPE_TEXT, orderCodigo);
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_ITEM, "bepite.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION, "bepmer.DESCRIPCION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_VALOR, "bepmer.VALOR");
		dataTable.setColumn(ORDER_COLUMN_INDEX_OBSERVACION, "bepmer.OBSERVACION");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, usuario.getCodNum());
			stmtCount.setInt(indexParam++, usuario.getCodNum());
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {					
					Merito mer = this.createMeritoFromResultset(rs, false, false); 
					meritos.add(mer);
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(meritos);
		}
		
		return dataTable;
	}
	
	/**
	 * Listado de méritos en el apartado candidato .
	 * @param params para leer los parametros de paginación, ordenacion, etc .
	 * @param usuario id del usuario .
	 * @return listado de titulaciones .
	 * @throws SQLException en caso de error de base de datos .
	 * @throws UVException error si no existe titulación .
	 */
	public BolsaEmpleoDataTable<Merito> listaMeritosCandidatoDatatable(Map<String, String[]> params, UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		
		if (usuario == null) {
			throw new UVException("No se pueden listar méritos sin el id del usuario");
		}
		
		List<Merito> meritos = new ArrayList<>();
		BolsaEmpleoDataTable<Merito> dataTable = new BolsaEmpleoDataTable<>(params);
		
		String consulta = ""
				+ " SELECT bepmer.*, bepblo.BEPAPA_CODNUM "
				+ " FROM TBEP_MERITOS bepmer "
				+ " INNER JOIN TBEP_ITEMSBAREMACION bepite ON bepite.CODNUM = bepmer.BEPITE_CODNUM "
				+ " INNER JOIN TBEP_BLOQUESBAREMACION bepblo ON bepblo.CODNUM = bepite.BEPBLO_CODNUM "
				+ " INNER JOIN TBEP_APARTADOSBAREMACION bepapa ON bepapa.CODNUM  = bepblo.BEPAPA_CODNUM "
				+ " WHERE bepmer.BEPUSU_CODNUM = ? ";
		
		String whereCodigo = String.format("(%s || '.' || %s || '.' || %s)", "bepapa.CODIGO", "bepblo.CODIGO", "bepite.CODIGO");
		String orderCodigo = String.format("(%s || '.' || %s || '.' || %s) %%s", "LPAD(bepapa.CODIGO, 3)", "LPAD(bepblo.CODIGO, 3)", "LPAD(bepite.CODIGO, 3)");
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_ID_CANDIDATO, "bepmer.CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_BLOQUE_CANDIDATO, "bepblo.BEPAPA_CODNUM");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ITEM_CANDIDATO, whereCodigo, DataTableColumn.COLUMN_TYPE_TEXT, orderCodigo);
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_ITEM_CANDIDATO, "bepite.NOMBRE");
		dataTable.setColumn(ORDER_COLUMN_INDEX_DESCRIPCION_CANDIDATO, "bepmer.DESCRIPCION");
		dataTable.setColumn(ORDER_COLUMN_INDEX_VALOR_CANDIDATO, "bepmer.VALOR");
		dataTable.setColumn(ORDER_COLUMN_INDEX_OBSERVACION_CANDIDATO, "bepmer.OBSERVACION");
		dataTable.setQuery(consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())
		) {
			int indexParam = 1;
			stmt.setInt(indexParam, usuario.getCodNum());
			stmtCount.setInt(indexParam++, usuario.getCodNum());
			dataTable.setFiltersParams(stmt, stmtCount, indexParam);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {					
					Merito mer = this.createMeritoFromResultset(rs, false, false); 
					meritos.add(mer);
				}				
			}	
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(meritos);
		}
		
		return dataTable;
	}
	
	/** obtiene un mérito a partir de su id.
	 * @param codNum id del mérito .
	 * @return archivo con el id especificado .
	 * @throws SQLException en caso de error en la BD .
	 * @throws UVException en caso de error de parametros .
	 */
	public Merito getMeritoById(Integer codNum) throws SQLException, UVException {
		String consulta = "SELECT bepmer.* FROM TBEP_MERITOS bepmer WHERE bepmer.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el merito con id " + codNum);
				}
				
				return this.createMeritoFromResultset(rs, true, false);
			}
		}
	}
	
	/**
	 * Devuelve el merito por su id o excepcion si no existe.
	 * @param id .
	 * @param withUsuario indica si cargar en memoria el usuario asociado al merito o no.
	 * @param withFichero indica si cargar en memoria el fichero del mérito o no .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Merito getMeritoById(Integer id, Boolean withUsuario, Boolean withFichero) throws UVException, SQLException {
		if (id == null) {
			throw new UVException("El mérito es requerido");
		}
		
		String consulta = "SELECT bepmer.* "
				+ "FROM TBEP_MERITOS bepmer "
				+ "WHERE 1=1 "
				+ "AND bepmer.CODNUM = ? ";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, id);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el merito");
				}
				
				return this.createMeritoFromResultset(rs, withUsuario, withFichero);						
			}
		}
	}
	
	/**
	 * Valida el valor de un mérito.
	 * @param valorStr .
	 * @param merito .
	 * @return .
	 * @throws UVException .
	 */
	public static Double validateValorDelMerito(String valorStr, Merito merito) throws UVException {
		// chequeo tipo de valor
		switch (merito.getItemBaremacion().getUnidades()) {
			case ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_SINO:
				break;
			case ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_ENTERO:
				if (!BolsaEmpleoUtils.isInteger(valorStr)) {
					throw new UVException(String.format(MENSAJE_ERROR_VALOR_ENTERO_NO_PERMITIDO, merito.getItemBaremacion().getFullCode()));
				}
				break;
			case ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_DECIMAL:
				if (!BolsaEmpleoUtils.isFloat(valorStr)) {
					throw new UVException(String.format(MENSAJE_ERROR_VALOR_DECIMAL_NO_PERMITIDO, merito.getItemBaremacion().getFullCode()));
				}
				break;
			default:
				throw new UVException("Tipo de unidad no válido");
		}
		
		// chequeo máximo y mínimo
		Double valor = merito.getItemBaremacion().getUnidades().equals(ModeloBaremacionItems.ITEM_UNIDADES_MEDICION_SINO) 
				? Formateador.leeParametroDouble("1.0") : Formateador.leeParametroDouble(valorStr);
		
		if (valor == null) {
			throw new UVException("El valor no es válido");
		}
		if (valor < merito.getItemBaremacion().getValorMinimo()) {
			throw new UVException(String.format(MENSAJE_ERROR_VALOR_MINIMO_PERMITIDO, 
				merito.getItemBaremacion().getValorMinimo().toString(),
				merito.getItemBaremacion().getFullCode()
			));
		}
		if (valor > merito.getItemBaremacion().getValorMaximo()) {
			throw new UVException(String.format(MENSAJE_ERROR_VALOR_MAXIMO_PERMITIDO, 
				merito.getItemBaremacion().getValorMaximo().toString(),
				merito.getItemBaremacion().getFullCode()
			));
		}
		
		return valor;
	}
	
	/**
	 * Crea un mérito a partir de un resultset.
	 * @param rs .
	 * @param withUsuario .
	 * @param withFile .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public Merito createMeritoFromResultset(ResultSet rs, Boolean withUsuario, Boolean withFile) throws SQLException, UVException {
		ModeloBaremacionItems modeloBar = ModeloBaremacionItems.obtenerInstancia();
		
		Merito mer = new Merito();
		mer.setCodNum(rs.getInt("CODNUM"));
		mer.setItemBaremacion(modeloBar.getItemBaremacionById(rs.getInt("BEPITE_CODNUM")));
		mer.setValor(rs.getDouble("VALOR"));
		mer.setDescripcion(rs.getString("DESCRIPCION"));
		mer.setObservacion(rs.getString("OBSERVACION"));
		
		if (Boolean.TRUE.equals(withUsuario)) {
			mer.setUsuario(ModeloUsuarioBolsaEmpleo.obtenerInstancia().getUsuarioById(rs.getInt("BEPUSU_CODNUM")));
		}
		
		if (Boolean.TRUE.equals(withFile)) {
			/*
			 * mer.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
			 */
			byte[] bytes = FileSystemUtils.obtenerFichero(Long.valueOf((long) rs.getInt("CODNUM")), ESQUEMA_TBEP_MERITOS, TABLA_TBEP_MERITOS, COLUMNA_TBEP_MERITOS);
			if (bytes == null) {
				mer.setArchivo(rs.getBlob("ARCHIVO").getBinaryStream());
			} else {
				InputStream archivo = new ByteArrayInputStream(bytes);
				mer.setArchivo(archivo);
			}
				
		}
		
		return mer;
	}
}
