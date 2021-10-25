package es.ujaen.uvirtual.modulo.bolsaempleo.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.AdaptadorDocumentoIdentidad;
import es.ujaen.uvirtual.utilidades.Memcache;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para obtener la información de usuarios de UVIRTUAL. Modelo -
 * Operaciones con nombres: lista, actualiza, borra, inserta Controlador -
 * Opers. con nombres: obtener, cambiar, eliminar, agregar
 * 
 * @author ATISoluciones 2021
 */
public class ModeloUsuarioBolsaEmpleo {
	private static final String NOMBREDEESTACLASE = ModeloUsuarioBolsaEmpleo.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);

	// columnas datatable usuario	
	public static final int ORDER_COLUMN_INDEX_USUARIO_DOCUMENTO = 0;
	public static final int ORDER_COLUMN_INDEX_USUARIO_CODCUENTA = 1;
	public static final int ORDER_COLUMN_INDEX_USUARIO_NOMBRE = 2;
	public static final int ORDER_COLUMN_INDEX_USUARIO_ROL = 3;
	public static final int ORDER_COLUMN_INDEX_USUARIO_EXCLUIDO = 4;
	public static final int ORDER_COLUMN_INDEX_USUARIO_ELIMINADO = 5;
	
	// columnas datatable candidato
	public static final int ORDER_COLUMN_INDEX_CANDIDATO_DOCUMENTO = 0;
	public static final int ORDER_COLUMN_INDEX_CANDIDATO_CODCUENTA = 1;
	public static final int ORDER_COLUMN_INDEX_CANDIDATO_NOMBRE = 2;
	public static final int ORDER_COLUMN_INDEX_CANDIDATO_LISTADISTRIBUCION = 3;
	public static final int ORDER_COLUMN_INDEX_CANDIDATO_EXCLUIDO = 4;
	public static final int ORDER_COLUMN_INDEX_CANDIDATO_BORRADO = 5;
		
	public static final String USUARIO_BORRADO = "S";
	public static final String USUARIO_NO_BORRADO = "N";
	public static final String USUARIO_EXCLUIDO = "S";
	public static final String USUARIO_NO_EXCLUIDO = "N";

	public static final String EXCLUSION_TIPO_TEMPORAL = "T";
	public static final String EXCLUSION_TIPO_INDEFINIDO = "I";

	public static final int COLUMN_NOMBRE_MAXLENGTH = 20;
	public static final int COLUMN_PRIMER_APELLIDO_MAXLENGTH = 40;
	public static final int COLUMN_SEGUNDO_APELLIDO_MAXLENGTH = 40;
	public static final int COLUMN_DIRECCION_MAXLENGTH = 50;
	public static final int COLUMN_CODIGO_POSTAL_MAXLENGTH = 5;
	public static final int COLUMN_LOCALIDAD_MAXLENGTH = 50;
	public static final int COLUMN_PROVINCIA_MAXLENGTH = 20;
	public static final int COLUMN_MOVIL_MAXLENGTH = 9;
	public static final int COLUMN_TELEFONO_MAXLENGTH = 9;
	public static final int COLUMN_NACIONALIDAD_MAXLENGTH = 20;
	public static final int COLUMN_EMAIL_MAXLENGTH = 50;
	public static final int COLUMN_RAZON_EXCLUSION_MAXLENGTH = 200;
	public static final int COLUMN_RAZON_BORRADO_MAXLENGTH = 500;

	public static final String MENSAJE_BUSCAR_USUARIO_NO_EXISTE = "El usuario no existe en el sistema: introduzca cuenta TIC sin @ujaen.es";
	public static final String MENSAJE_ERROR_DOCUMENTO_REQUERIDO = "El documento es requerido";
	public static final String MENSAJE_ERROR_USUARIO_CON_DOCUMENTO_NO_EXISTE = "No existe el usuario con el documento indicado";
	public static final String MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE = "No existe el usuario en UJA con el documento indicado [%s]";
	public static final String MENSAJE_ERROR_USUARIO_CON_ID_NO_EXISTE = "No existe el usuario con el id indicando";
	public static final String MENSAJE_ERROR_NO_HAY_USUARIO_LOGEADO = "No hay usuario logeado";
	public static final String MENSAJE_ERROR_USUARIO_BORRADO = "El usuario está borrado de la bolsa de empleo";
	public static final String MENSAJE_ERROR_USUARIO_EXCLUIDO = "El usuario está excluido de la bolsa de empleo";
	public static final String MENSAJE_ERROR_NO_ES_CANDIDATO = "El usuario no es un candidato";
	public static final String MENSAJE_ERROR_USUARIO_VACIO = "No se puede insertar un usuario vacio";
	public static final String MENSAJE_ERROR_USUARIO_SIN_ROL = "No se puede insertar un usuario sin rol";
	public static final String MENSAJE_ERROR_CODNUM_REQUERIDO = "Id de usuario requerido";
	
	public static final String BAREMABLE = "S";
	public static final String EXCLUIDO = "S";
	public static final String BORRADO = "S";
	public static final String EN_LISTA_DISTRIBUCION = "S";

	protected static ModeloUsuarioBolsaEmpleo eInstancia;

	/**
	 * Crea una instancia del objeto. de forma sincronizada para protegerse de
	 * posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloUsuarioBolsaEmpleo();
		}
	}

	/**
	 * Obtiene una instancia de la conexión.
	 * 
	 * @return instancia
	 */
	public static ModeloUsuarioBolsaEmpleo obtenerInstancia() {
		if (eInstancia == null) {
			crearInstancia();
		}
		return eInstancia;
	}

	/**
	 * Devuelve un usuario por su id.
	 * 
	 * @param codNum id de usuario
	 * @return usuarioBolsaEmpleo
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  si bolsa no es existe
	 */
	public UsuarioBolsaEmpleo getUsuarioById(int codNum) throws SQLException, UVException {
		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE bepusu.CODNUM = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setInt(1, codNum);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_USUARIO_CON_ID_NO_EXISTE);
				}
				return getUsuarioFromResultSet(rs);				
			}
		}
	}

	/**
	 * Devuelve un listado de usuarios por su id.
	 * 
	 * @param ids codnum de usurios
	 * @return listado de usuarios
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public List<UsuarioBolsaEmpleo> getUsuariosByIds(int[] ids) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();

		for (int i = 0; i < ids.length; i++) {
			usuarios.add(this.getUsuarioById(ids[i]));
		}

		return usuarios;
	}

	/**
	 * Devuelve un usuario bep por su codigo de cuenta.
	 * 
	 * @param codcuenta codcuenta de usuarios
	 * @return listado de usuarios
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public UsuarioBolsaEmpleo getUsuarioByCodCuenta(String codcuenta) throws SQLException, UVException {
		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, codcuenta);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_USUARIO_CON_DOCUMENTO_NO_EXISTE);
				}
				
				return getUsuarioFromResultSet(rs);
			}
		}
	}
	
	/** Devuelve un usuario bep por su codigo de cuenta si existe, en caso contrario devuelve null.
	 * @param codcuenta codigo de usuario .
	 * @return usuario o null si no existe .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public UsuarioBolsaEmpleo compruebaUsuarioByCodCuenta(String codcuenta) throws SQLException, UVException {
		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = ?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, codcuenta);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				
				return getUsuarioFromResultSet(rs);
			}
		}
	}

	/**
	 * Listado de usuarios de bolsa empleo.
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException  error si no existe la area
	 */
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaUsuarioBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE 1=1 ";
		
		String whereNombre = String.format("(%s || ' ' || %s || ' ' || %s)", "bepusu.VUAJA_STRNOMBRE", "bepusu.VUAJA_STRAPELLIDO1", "bepusu.VUAJA_STRAPELLIDO2");
		
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_USUARIO_DOCUMENTO, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_USUARIO_CODCUENTA, "bepusu.CODCUENTA");
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_USUARIO_NOMBRE, whereNombre, DataTableColumn.COLUMN_TYPE_TEXT);
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_USUARIO_ROL, "bepusu.ROL");
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_USUARIO_EXCLUIDO, "bepusu.FLGEXCLUIDO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_USUARIO_ELIMINADO, "bepusu.FLGBORRADO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = getUsuarioFromResultSet(rs);
					usuarios.add(usuario);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}

		return dataTable;
	}

	/**
	 * Listado de areas.
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException  error si no existe la area
	 */
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaUsuarioCandidatosBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = new BolsaEmpleoDataTable<>(params);

		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE bepusu.rol = ?";
		
		String whereNombre = String.format("(%s || ' ' || %s || ' ' || %s)", "bepusu.VUAJA_STRNOMBRE", "bepusu.VUAJA_STRAPELLIDO1", "bepusu.VUAJA_STRAPELLIDO2");
		
		
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_CANDIDATO_DOCUMENTO, "bepusu.VUAJA_PRSNIF");
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_CANDIDATO_CODCUENTA, "bepusu.CODCUENTA");
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_CANDIDATO_NOMBRE, whereNombre, DataTableColumn.COLUMN_TYPE_TEXT);
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_CANDIDATO_LISTADISTRIBUCION, "bepusu.FLGLISTADISTRIBUCION", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_CANDIDATO_EXCLUIDO, "bepusu.FLGEXCLUIDO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(ModeloUsuarioBolsaEmpleo.ORDER_COLUMN_INDEX_CANDIDATO_BORRADO, "bepusu.FLGBORRADO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			
			int paramIndex = 1;
			stmt.setInt(paramIndex, ModeloRol.ID_ROL_CANDIDATO);
			stmtCount.setInt(paramIndex++, ModeloRol.ID_ROL_CANDIDATO);
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = getUsuarioFromResultSet(rs);
					usuarios.add(usuario);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}

		return dataTable;
	}

	/**
	 * Crea un usuario de bolsa de empleo mediante un resultset.
	 * 
	 * @param rs      resultado de la consulta de UVIRTUAL
	 * @return usuario
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException  en caso de no poder crear el usuario
	 */
	public UsuarioBolsaEmpleo getUsuarioFromResultSet(ResultSet rs) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		
		usuario.setCodNum(rs.getInt("CODNUM"));
		usuario.setRol(ModeloRol.obtenerInstancia().getRoleById(rs.getInt("ROL")));
		usuario.setCodCuenta(rs.getString("CODCUENTA"));
		
		usuario.setTipoDocumento(rs.getString("VUAJA_STRTIPODOCUMENTO"));
		usuario.setIdNif(rs.getString("VUAJA_IDNIF"));
		usuario.setLetraNif(rs.getString("VUAJA_LETRANIF"));
		usuario.setPrsNif(rs.getString("VUAJA_PRSNIF"));
		usuario.setNombre(rs.getString("VUAJA_STRNOMBRE"));
		usuario.setPrimerApellido(rs.getString("VUAJA_STRAPELLIDO1"));
		usuario.setSegundoApellido(rs.getString("VUAJA_STRAPELLIDO2"));
		usuario.setEmail(rs.getString("VUAJA_EMAIL_ALTA"));

		usuario.setDireccion(rs.getString("DIRECCION"));
		usuario.setCodigoPostal(rs.getString("CODIGOPOSTAL"));
		usuario.setLocalidad(rs.getString("LOCALIDAD"));
		usuario.setProvincia(rs.getString("PROVINCIA"));
		usuario.setNacionalidad(rs.getString("NACIONALIDAD"));
		usuario.setTelefono(rs.getString("TELEFONO"));
		
		usuario.setListaDist(rs.getString("FLGLISTADISTRIBUCION").equals(EN_LISTA_DISTRIBUCION));
		usuario.setExcluido(rs.getString("FLGEXCLUIDO").equals(EXCLUIDO));
		usuario.setExcluidoTipo(rs.getString("FLGEXCLUIDOTIPO"));
		usuario.setFechaExclusionInicio(rs.getTimestamp("FECHA_EXCLUSION_INICIO"));
		usuario.setFechaExclusionFin(rs.getTimestamp("FECHA_EXCLUSION_FIN"));
		usuario.setRazonExcluido(rs.getString("RAZON_EXCLUSION"));
		usuario.setFechaExclusion(rs.getTimestamp("FECHA_EXCLUSION"));
		usuario.setBorrado(rs.getString("FLGBORRADO").equals(BORRADO));
		usuario.setFechaBorrado(rs.getTimestamp("FECHA_BORRADO"));
		usuario.setRazonBorrado(rs.getString("RAZON_BORRADO"));

		return usuario;
	}

	/**
	 * Función que inserta un usuario en la BD y devuelve el codNum del usuario creado.
	 * 
	 * @param usuario a insertar en la BD
	 * @param usuarioQueInserta .
	 * @return codnum usuario insertado 
	 * @throws SQLException en caso de error en la BD
	 */
	public Integer insertaUsuario(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioQueInserta) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException(MENSAJE_ERROR_USUARIO_VACIO);
		}
		if (usuario.getRol() == null) {
			throw new UVException(MENSAJE_ERROR_USUARIO_SIN_ROL);
		}

		ArrayList<String> columns = getColumnsFromInsertOrUpdate();
		
		String query = "INSERT INTO TBEP_USUARIOS (" + String.join(",", columns) + ") VALUES ("
				+ BolsaEmpleoUtils.consultaMultiplesParametros(columns.size()) + ")";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); 
				PreparedStatement stmt = conexion.prepareStatement(query, new String[]{"CODNUM"})) {
			
			setColumnsFromInsertOrUpdate(stmt, usuario, usuarioQueInserta);
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			
			return rs.getInt(1);
		}
	}

	/**
	 * Actualiza un usuario.
	 * 
	 * @param usuario UsuarioBolsaEmpleo con los datos nuevos a actualizar
	 * @param usuarioQueActualiza .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  en caso de errores de validacion
	 */
	public void actualizaUsuario(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioQueActualiza) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException(MENSAJE_ERROR_USUARIO_VACIO);
		}
		if (usuario.getCodNum() == null) {
			throw new UVException(MENSAJE_ERROR_CODNUM_REQUERIDO);
		}

		ArrayList<String> columns = getColumnsFromInsertOrUpdate();

		String query = "UPDATE TBEP_USUARIOS SET "
				+ columns.stream().map(c -> c + "=?").collect(Collectors.joining(",")) + " WHERE CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int paramIndex = setColumnsFromInsertOrUpdate(stmt, usuario, usuarioQueActualiza);
			stmt.setInt(paramIndex++, usuario.getCodNum());
			stmt.executeUpdate();
		}

		refrescarUsuarioBEP(usuario);
	}
	
	private ArrayList<String> getColumnsFromInsertOrUpdate() {
		ArrayList<String> columns = new ArrayList<>();
		
		columns.add("UID_USUARIO");
		columns.add("ROL");
		columns.add("CODCUENTA");
		columns.add("VUAJA_STRTIPODOCUMENTO");
		columns.add("VUAJA_IDNIF");
		columns.add("VUAJA_LETRANIF");
		columns.add("VUAJA_PRSNIF");
		columns.add("VUAJA_STRNOMBRE");
		columns.add("VUAJA_STRAPELLIDO1");
		columns.add("VUAJA_STRAPELLIDO2");
		columns.add("VUAJA_EMAIL_ALTA");
		columns.add("DIRECCION");
		columns.add("CODIGOPOSTAL");
		columns.add("LOCALIDAD");
		columns.add("PROVINCIA");
		columns.add("NACIONALIDAD");
		columns.add("TELEFONO");
		columns.add("FLGLISTADISTRIBUCION");
		columns.add("FLGEXCLUIDO");
		columns.add("FLGEXCLUIDOTIPO");
		columns.add("RAZON_EXCLUSION");
		columns.add("FECHA_EXCLUSION");
		columns.add("FECHA_EXCLUSION_INICIO");
		columns.add("FECHA_EXCLUSION_FIN");
		
		return columns;
	}

	/**
	 * Establece los parametros para una consulta de insercion o update de un usuario.
	 * @param stmt .
	 * @param usuario .
	 * @param usuarioInsertOrUpdate .
	 * @return .
	 * @see getColumnsFromInsertOrUpdate para orden.
	 */
	@SuppressWarnings({"checkstyle:ExecutableStatementCount"})
	private int setColumnsFromInsertOrUpdate(PreparedStatement stmt, UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioInsertOrUpdate) throws SQLException {
		int parameterIndex = 1;
		
		stmt.setString(parameterIndex++, usuarioInsertOrUpdate.getCodCuenta());
		stmt.setInt(parameterIndex++, usuario.getRol().getCodNum());
		stmt.setString(parameterIndex++, usuario.getCodCuenta());			
		
		// arcos
		stmt.setString(parameterIndex++, usuario.getTipoDocumento());
		stmt.setString(parameterIndex++, usuario.getIdNif());
		stmt.setString(parameterIndex++, usuario.getLetraNif());
		stmt.setString(parameterIndex++, usuario.getPrsNif());
		stmt.setString(parameterIndex++, usuario.getNombre());
		stmt.setString(parameterIndex++, usuario.getPrimerApellido());
		stmt.setString(parameterIndex++, usuario.getSegundoApellido());
		stmt.setString(parameterIndex++, usuario.getEmail());
		
		// bep
		stmt.setString(parameterIndex++, usuario.getDireccion());
		stmt.setString(parameterIndex++, usuario.getCodigoPostal());
		stmt.setString(parameterIndex++, usuario.getLocalidad());
		stmt.setString(parameterIndex++, usuario.getProvincia());
		stmt.setString(parameterIndex++, usuario.getNacionalidad());
		stmt.setString(parameterIndex++, usuario.getTelefono());
		stmt.setString(parameterIndex++, Boolean.TRUE.equals(usuario.getListaDist()) ? "S" : "N");

		if (Boolean.TRUE.equals(usuario.getExcluido())) {
			stmt.setString(parameterIndex++, "S");
			stmt.setString(parameterIndex++, usuario.getExcluidoTipo());
			stmt.setString(parameterIndex++, usuario.getRazonExcluido());
			stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusion().getTime()));

			if (usuario.getExcluidoTipo() != null && usuario.getExcluidoTipo().equals(EXCLUSION_TIPO_TEMPORAL)) {
				stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusionInicio().getTime()));
				stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusionFin().getTime()));
			} else {
				stmt.setDate(parameterIndex++, null);
				stmt.setDate(parameterIndex++, null);
			}
		} else {
			stmt.setString(parameterIndex++, "N");
			stmt.setString(parameterIndex++, "");
			stmt.setString(parameterIndex++, "");
			stmt.setDate(parameterIndex++, null);
			stmt.setDate(parameterIndex++, null);
			stmt.setDate(parameterIndex++, null);
		}
		
		return parameterIndex;
	}
	
	/**
	 * Actualiza los datos personales de un usuario.
	 * 
	 * @param usuario             UsuarioBolsaEmpleo con los datos nuevos a
	 *                            actualizar
	 * @param usuarioQueActualiza .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  en caso de errores de validacion
	 */
	public void actualizaUsuarioMisDatos(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioQueActualiza) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException(MENSAJE_ERROR_USUARIO_VACIO);
		}
		if (usuario.getCodNum() == null) {
			throw new UVException(MENSAJE_ERROR_CODNUM_REQUERIDO);
		}

		String consulta = "UPDATE tbep_usuarios SET UID_USUARIO=?,DIRECCION=?,CODIGOPOSTAL=?,LOCALIDAD=?,PROVINCIA=?,NACIONALIDAD=?,TELEFONO=?,FLGLISTADISTRIBUCION=?"
				+ " WHERE codnum=?";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, usuarioQueActualiza.getCodCuenta());
			stmt.setString(parameterIndex++, usuario.getDireccion());
			stmt.setString(parameterIndex++, usuario.getCodigoPostal());
			stmt.setString(parameterIndex++, usuario.getLocalidad());
			stmt.setString(parameterIndex++, usuario.getProvincia());
			stmt.setString(parameterIndex++, usuario.getNacionalidad());
			stmt.setString(parameterIndex++, usuario.getTelefono());
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(usuario.getListaDist()) ? "S" : "N");
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			stmt.executeUpdate();
		}
		
		refrescarUsuarioBEP(usuario);
	}
	
	/** Comprueba si alguno de los datos del usuario es nulo .
	 * @param usuario .
	 * @return true o false dependiendo de si falta algún dato .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public boolean compruebaUsuarioMisDatosValidos(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("usuario obligatorio");
		}
		if (usuario.getCodNum() == null) {
			throw new UVException("id usuario no válido");
		}
		
		String consulta = "SELECT * FROM TBEP_USUARIOS"
				+ "	WHERE (CODNUM = ?) AND (CODIGOPOSTAL IS NULL OR LOCALIDAD IS NULL OR PROVINCIA IS NULL"
				+ "	OR TELEFONO IS NULL OR DIRECCION IS NULL)";
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			stmt.executeUpdate();
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return true;
				}
			}
		}
		
		return false;
	}

	/** Establece el usuario como borrado.
	 * @param usu .
	 * @param areas  .
	 * @param usuarioQueExcluye .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void excluirUsuarioArea(UsuarioBolsaEmpleo usu, List<Area> areas, UsuarioBolsaEmpleo usuarioQueExcluye) throws SQLException, UVException {
		this.excluirUsuarioAreas(areas, usu, usuarioQueExcluye);
	}

	private void excluirUsuarioAreas(List<Area> areas, UsuarioBolsaEmpleo usu, UsuarioBolsaEmpleo usuarioQueExcluye) throws SQLException, UVException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
			conexion.setAutoCommit(false);
			try {
				for (Area a : areas) {
					String query = "INSERT INTO TBEP_USU_EXCLUIDOS_AREA (USUARIO,AREA,UID_USUARIO) VALUES (?,?,?)";
					
					try (PreparedStatement stmt = conexion.prepareStatement(query)) {
						int paramIndex = 1;
						stmt.setInt(paramIndex++, usu.getCodNum());
						stmt.setInt(paramIndex++, a.getCodNum());
						stmt.setString(paramIndex++, usuarioQueExcluye.getCodCuenta());
						stmt.executeUpdate();
					}
				}
				conexion.commit();
			} catch (Exception e) {
				conexion.rollback();
				throw new UVException(e.getMessage());
			} finally {
				conexion.setAutoCommit(true);
			}
		}
	}

	/**
	 * Establece el usuario como borrado.
	 * 
	 * @param usu   .
	 * @param areas .
	 * @throws SQLException .
	 */
	public void incluirUsuarioArea(UsuarioBolsaEmpleo usu, List<Area> areas) throws SQLException {
		this.incluirUsuarioAreas(areas, usu);
	}

	private void incluirUsuarioAreas(List<Area> areas, UsuarioBolsaEmpleo usu) throws SQLException {
		String query = "DELETE FROM TBEP_USU_EXCLUIDOS_AREA WHERE usuario = ? AND area = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {

			for (Area area : areas) {
				int paramIndex = 1;
				stmt.setInt(paramIndex++, usu.getCodNum());
				stmt.setInt(paramIndex++, area.getCodNum());
				stmt.executeUpdate();
			}
		}
	}

	/**
	 * Establece el usuario como excluido.
	 * 
	 * @param usuario .
	 * @param usuarioQueExcluye .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void ponerUsuarioComoExcluido(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioQueExcluye) throws SQLException, UVException {
		String query = "UPDATE TBEP_USUARIOS SET UID_USUARIO=?,FLGEXCLUIDO=?,FLGEXCLUIDOTIPO=?,RAZON_EXCLUSION=?,FECHA_EXCLUSION=?";

		if (EXCLUSION_TIPO_TEMPORAL.equals(usuario.getExcluidoTipo())) {
			query += ",FECHA_EXCLUSION_INICIO=?,FECHA_EXCLUSION_FIN=?";
		}

		query += " WHERE CODNUM IN ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioQueExcluye.getCodCuenta());
			stmt.setString(indexParam++, USUARIO_EXCLUIDO);
			stmt.setString(indexParam++, usuario.getExcluidoTipo());
			stmt.setString(indexParam++, usuario.getRazonExcluido());
			stmt.setDate(indexParam++, new java.sql.Date(usuario.getFechaExclusion().getTime()));

			if (usuario.getExcluidoTipo().equals(EXCLUSION_TIPO_TEMPORAL)) {
				stmt.setDate(indexParam++, new java.sql.Date(usuario.getFechaExclusionInicio().getTime()));
				stmt.setDate(indexParam++, new java.sql.Date(usuario.getFechaExclusionFin().getTime()));
			}

			stmt.setInt(indexParam++, usuario.getCodNum());
			stmt.executeUpdate();
		}
		
		refrescarUsuarioBEP(usuario);
	}

	/**
	 * Establece el usuario como NO excluido.
	 * 
	 * @param usuario .
	 * @param usuarioQueNoExcluye .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void ponerUsuarioComoNoExcluido(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioQueNoExcluye) throws SQLException, UVException {
		String query = "UPDATE TBEP_USUARIOS SET UID_USUARIO=?,FLGEXCLUIDO=?,FLGEXCLUIDOTIPO=?,FECHA_EXCLUSION_INICIO=?,FECHA_EXCLUSION_FIN=?,"
				+ "RAZON_EXCLUSION=?,FECHA_EXCLUSION=? WHERE CODNUM IN ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioQueNoExcluye.getCodCuenta());
			stmt.setString(indexParam++, "N");
			stmt.setNull(indexParam++, Types.NULL);
			stmt.setNull(indexParam++, Types.NULL);
			stmt.setNull(indexParam++, Types.NULL);
			stmt.setNull(indexParam++, Types.NULL);
			stmt.setNull(indexParam++, Types.NULL);
			stmt.setInt(indexParam++, usuario.getCodNum());
			stmt.executeUpdate();
		}
		
		refrescarUsuarioBEP(usuario);
	}

	/** Establece el usuario como borrado.
	 * @param usuario .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void ponerUsuarioComoBorrado(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		this.cambiarFlagBorradoUsuario(usuario, USUARIO_BORRADO, usuarioUpdate);
	}

	/** Establece el usuario como NO borrado.
	 * @param usuario .
	 * @param usuarioUpdate .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public void ponerUsuarioComoNoBorrado(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		this.cambiarFlagBorradoUsuario(usuario, USUARIO_NO_BORRADO, usuarioUpdate);
	}

	private void cambiarFlagBorradoUsuario(UsuarioBolsaEmpleo usuario, String borrado, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException, UVException {
		String query = "UPDATE TBEP_USUARIOS SET UID_USUARIO=?,FLGBORRADO=?,FECHA_BORRADO=?,RAZON_BORRADO=? WHERE CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;

			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setString(indexParam++, borrado);
			if (borrado.equals(USUARIO_BORRADO)) {
				stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
				stmt.setString(indexParam++, usuario.getRazonBorrado());
			} else {
				stmt.setNull(indexParam++, Types.DATE);
				stmt.setNull(indexParam++, Types.VARCHAR);
			}

			stmt.setInt(indexParam++, usuario.getCodNum());
			stmt.executeUpdate();
		}
		
		this.refrescarUsuarioBEP(usuario);
	}

	/**
	 * Si hay usuario logeado, comprueba si está registrado en sistema. Si tenemos
	 * usuario en nuestro sistema, comprueba si está excluido o borrado (lanzando
	 * una excepción). Si no tenemos usuario en nuestro sistema, lo crea como
	 * candidato.
	 * 
	 * @param datos .
	 * @return Boolean true si hay usuario logeado en el sistema o false en otro caso.
	 * @throws SQLException en caso de error en la BD.
	 * @throws UVException  si noticia no es valida.
	 */
	public UsuarioBolsaEmpleo getOrCreateUsuario(UVDatos datos) throws SQLException, UVException {
		Usuario usuArcos = datos.getUsuario();

		// no hay usuario logeado, salimos
		if (usuArcos == null) {
			return null;
		}
		
		// comprobamos si está en caché, lo devolvemos
		Memcache mc = Memcache.getInstance();
		UsuarioBolsaEmpleo usuario = (UsuarioBolsaEmpleo) mc.get("usuarioBEP." + usuArcos.getUid());
		LOGGER.log(Level.FINER, String.format("LEYENDO USUARIO CACHEADO BEP [%s]", usuArcos.getUid()));
		
		if (usuario == null) {
			usuario = refrescarUsuarioBEP(usuArcos);
		}
		
		// comprobamos si está borrado o excluido
		if (Boolean.TRUE.equals(usuario.getExcluido())) {
			throw new UVException(usuario.getRazonBorrado() != null
					? String.format("No puede acceder, su perfil ha sido excluido: [%s]. Razón: %s", usuario.getCodCuenta(), usuario.getRazonBorrado())
					: String.format("No puede acceder, su perfil ha sido excluido: [%s].", usuario.getCodCuenta()));
		} else if (Boolean.TRUE.equals(usuario.getBorrado())) {
			throw new UVException(usuario.getRazonBorrado() != null
					? String.format("No puede acceder, su perfil ha sido dado de baja: [%s]. Razón: %s", usuario.getCodCuenta(), usuario.getRazonBorrado())
					: String.format("No puede acceder, su perfil ha sido dado de baja: [%s].", usuario.getCodCuenta()));
		}
		
		return usuario; 
	}
	
	/**
	 * Refresca un usuario BEP.
	 * @param usuarioBep .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public UsuarioBolsaEmpleo refrescarUsuarioBEP(UsuarioBolsaEmpleo usuarioBep) throws SQLException, UVException {
		Usuario usuArcos = CrearUsuario.usuario(usuarioBep.getCodCuenta());
		return refrescarUsuarioBEP(usuArcos);
	}
	
	/**
	 * Crear el usuario si es necesario como candidato y lo guarda en el memcaché.
	 * @param usuArcos usuario vuja.
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public UsuarioBolsaEmpleo refrescarUsuarioBEP(Usuario usuArcos) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario;
		String uid = usuArcos.getUid();

		// comprobamos si existe el usuario en uvirtual
		LOGGER.log(Level.FINER, String.format("Chequeando si existe usuario en Bolsa Empleo [%s]", uid));

		if (!existeUsuarioBolsaEmpleoByUid(usuArcos.getUid())) {
			// no existe el usuario en la bolsa de empleo lo creamos como candidato
			LOGGER.log(Level.FINER, String.format("No existe usuario en Bolsa Empleo [%s]. Lo creamos como candidato.", uid));
			usuario = crearUsuarioBolsaEmpleo(ModeloRol.ID_ROL_CANDIDATO, usuArcos.getUid(), null);
		} else {
			// cargamos los datos del usuario de bolsa de empleo
			LOGGER.log(Level.FINER, String.format("Existe usuario en Bolsa Empleo [%s]. Leemos sus datos.", uid));
			usuario = getUsuarioByCodCuenta(usuArcos.getUid());
		}
				
		LOGGER.log(Level.FINER, String.format("Fin chequeo usuario Bolsa Empleo [%s]. Todo correcto, refrescamos usuario y cacheamos", uid));
		
		// para que el usuario vuja, coga los roles del usuario de bep
		CrearUsuario.refrescarUsuario(usuario.getCodCuenta());
		
		Memcache mc = Memcache.getInstance();
		mc.set("usuarioBEP." + uid, usuario);
		
		LOGGER.log(Level.FINER, String.format("CACHEANDO USUARIO BEP [%s]", uid));

		return usuario;
	}

	/**
	 * Comprueba si existe un usuario de bolsa de empleo por su uid.
	 * 
	 * @param uid del usuario
	 * @return usuario 
	 * @throws SQLException en caso de error en la BD
	 */
	public boolean existeUsuarioBolsaEmpleoByUid(String uid) throws SQLException, UVException {
		if (uid == null) {
			throw new UVException(MENSAJE_BUSCAR_USUARIO_NO_EXISTE);
		}

		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, uid);

			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Devuelve una lista con los roles de un usuario .
	 * 
	 * @param uid del usuario
	 * @return Lista de roles del usuario
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  si noticia no es valida
	 */
	public List<String> listaRolesUsuario(String uid) throws SQLException {
		ArrayList<String> roles = new ArrayList<>();
		String consultaRol = "SELECT admrol.valor FROM TBEP_USUARIOS bepusu LEFT JOIN ADM_ROL admrol ON admrol.ROL_CODNUM = bepusu.ROL WHERE bepusu.CODCUENTA = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consultaRol)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, uid);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					roles.add(rs.getString("valor"));
				}
			}
		}

		return roles;
	}

	/**
	 * Cambia flag usuario a borrado con razon de borrado .
	 * 
	 * @param usuario .
	 * @param usuarioQueBorra .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  si noticia no es valida
	 */
	public void cambiarFlagBorradoUsuarioRazon(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioQueBorra) throws SQLException, UVException {
		String query = "UPDATE TBEP_USUARIOS SET UID_USUARIO=?,FLGBORRADO=?,RAZON_BORRADO=?,FECHA_BORRADO=? WHERE CODNUM=?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, usuarioQueBorra.getCodCuenta());
			stmt.setString(indexParam++, "S");
			stmt.setString(indexParam++, usuario.getRazonBorrado());
			stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setInt(indexParam++, usuario.getCodNum());
			stmt.executeUpdate();
		}
		
		refrescarUsuarioBEP(usuario);
	}

	/**
	 * método que crea un usuario en bolsa empleo y lo devuelve.
	 * 
	 * @param idRol            .
	 * @param uidUsuario codcuenta (uid) del usuario arcos a partir del cual se crea
	 * @param usuarioQueCrea .
	 * @return usuario .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public UsuarioBolsaEmpleo crearUsuarioBolsaEmpleo(Integer idRol, String uidUsuario, UsuarioBolsaEmpleo usuarioQueCrea) throws SQLException, UVException {
		Usuario usuArcos = CrearUsuario.usuario(uidUsuario);
		
		UsuarioBolsaEmpleo usuarioFinal = new UsuarioBolsaEmpleo();
		usuarioFinal.setRol(ModeloRol.obtenerInstancia().getRoleById(idRol));
		usuarioFinal.setCodCuenta(usuArcos.getUid());
		usuarioFinal.setTipoDocumento(usuArcos.getDocumentoTipo());
		usuarioFinal.setIdNif(AdaptadorDocumentoIdentidad.numeroDocumento(AdaptadorDocumentoIdentidad.UXXIAC, usuArcos));
		usuarioFinal.setLetraNif(AdaptadorDocumentoIdentidad.letraNIF(usuArcos.getDocumentoTipo(), usuArcos.getDocumentoNumero()));
		usuarioFinal.setPrsNif(usuArcos.getDocumentoNumero());
		usuarioFinal.setNombre(usuArcos.getNombre());
		usuarioFinal.setPrimerApellido(usuArcos.getApellido1());
		usuarioFinal.setSegundoApellido(usuArcos.getApellido2());
		usuarioFinal.setEmail(usuArcos.getEmailCalculado());
		usuarioFinal.setListaDist(true);
		
		Integer codNum = insertaUsuario(usuarioFinal, usuarioQueCrea == null ? usuarioFinal : usuarioQueCrea);
		
		return getUsuarioById(codNum);
	}

	/**
	 * Devuelve el usuario de la bolsa del empleo por su nombre de usuario. O null
	 * si no está creado en la bolsa de empleo.
	 * 
	 * @param nombreUsuario .
	 * @return si existe o no .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public UsuarioBolsaEmpleo obtenerUsuarioBolsaEmpleoSiExiste(String nombreUsuario) throws UVException, SQLException {
		Usuario usuArcos = CrearUsuario.usuario(nombreUsuario);

		if (usuArcos == null) {
			throw new UVException(MENSAJE_BUSCAR_USUARIO_NO_EXISTE);
		}

		try {
			return getUsuarioByCodCuenta(usuArcos.getUid());
		} catch (UVException ex) {
			return null;
		}
	}
	
	/**
	 * Devuelve un resultset con los datos del usuario de la vista VUJA_NET_BEP_AR_PERSONA.
	 * Buscando por PRSNIF.
	 * @param documento .
	 * @return .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	
//	public UsuarioBolsaEmpleoVuja getResultSetPersonaArcosByPrsnif(String documento) throws SQLException, UVException {
//		String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";
//
//		try (Connection conexionArcos = ConexionArcos.obtenerInstancia(); PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos)) {
//			stmtArcos.setString(1, documento);
//
//			try (ResultSet rsArcos = stmtArcos.executeQuery()) {
//				if (!rsArcos.next()) {
//					LOGGER.log(Level.SEVERE, String.format("No existe el usuario en VUJA_NET_BEP_AR_PERSONA [%s]", documento));
//					throw new UVException(String.format(MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE, documento));
//				}
//				
//				return new UsuarioBolsaEmpleoVuja(
//					rsArcos.getInt("CODINT"),
//					rsArcos.getString("STRTIPODOCUMENTO"),
//					rsArcos.getString("IDNIF"),
//					rsArcos.getString("LETRANIF"),
//					rsArcos.getString("PRSNIF"),
//					rsArcos.getString("STRNOMBRE"),
//					rsArcos.getString("STRAPELLIDO1"),
//					rsArcos.getString("STRAPELLIDO2"),
//					rsArcos.getString("EMAIL_ALTA")					
//				);
//			}
//		}
//	}
}
