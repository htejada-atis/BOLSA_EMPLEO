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
import es.ujaen.uvirtual.modelo.conexion.ConexionArcos;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
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

	public static final int ORDER_COLUMN_INDEX_TIPO_DOCUMENTO = 1;
	public static final int ORDER_COLUMN_INDEX_NUMDOCUMENTO = 2;
	public static final int ORDER_COLUMN_INDEX_COD_CUENTA = 3;
	public static final int ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS = 4;
	public static final int ORDER_COLUMN_INDEX_ROL = 5;
	public static final int ORDER_COLUMN_INDEX_LISTA_DIST = 6;
	public static final int ORDER_COLUMN_INDEX_EXCLUIDO = 7;
	public static final int ORDER_COLUMN_INDEX_BORRADO = 8;

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
	public static final String MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE = "No existe el usuario en UJA con el documento indicado";
	public static final String MENSAJE_ERROR_USUARIO_CON_ID_NO_EXISTE = "No existe el usuario con el id indicando";
	public static final String MENSAJE_ERROR_NO_HAY_USUARIO_LOGEADO = "No hay usuario logeado";
	public static final String MENSAJE_ERROR_USUARIO_BORRADO = "El usuario está borrado de la bolsa de empleo";
	public static final String MENSAJE_ERROR_USUARIO_EXCLUIDO = "El usuario está excluido de la bolsa de empleo";
	public static final String MENSAJE_ERROR_NO_ES_CANDIDATO = "El usuario no es un candidato";

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
	 * Obtiene un usuario a partir de su nif.
	 * 
	 * @param documento del usuario
	 * @return usuario con el nombre especificado
	 * @throws SQLException en caso de error en la BD
	 */
	public UsuarioBolsaEmpleo getUsuarioByNumeroDocumento(String documento) throws SQLException, UVException {
		if (documento == null) {
			throw new UVException(MENSAJE_ERROR_DOCUMENTO_REQUERIDO);
		}

		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE bepusu.PRSNIF = ?";
		String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				Connection conexionArcos = ConexionArcos.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);
				PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos)) {

			stmt.setString(1, documento);
			stmtArcos.setString(1, documento);

			try (ResultSet rs = stmt.executeQuery(); ResultSet rsArcos = stmtArcos.executeQuery()) {
				if (!rsArcos.next()) {
					LOGGER.log(Level.SEVERE,
							String.format("No existe el usuario en VUJA_NET_BEP_AR_PERSONA [%s]", documento));
					throw new UVException(MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE);
				}

				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_USUARIO_CON_DOCUMENTO_NO_EXISTE);
				}

				return setUsuario(rs, rsArcos);
			}
		}
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
		String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				Connection conexionArcos = ConexionArcos.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);
				PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos)) {
			stmt.setInt(1, codNum);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_USUARIO_CON_ID_NO_EXISTE);
				}

				stmtArcos.setString(1, rs.getString("PRSNIF"));
				try (ResultSet rsArcos = stmtArcos.executeQuery()) {
					if (!rsArcos.next()) {
						throw new UVException(MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE);
					}

					return setUsuario(rs, rsArcos);
				}
			}
		}
	}

	/**
	 * Devuelve el usuario de bolsa de empleo logeado en el sistema. Si el usuario
	 * está borrado o excluido de la bolsa saltará un excepción.
	 * 
	 * @param datos .
	 * @return usuario o null si hay login
	 * @throws UVException  .
	 * @throws SQLException .
	 */
	public UsuarioBolsaEmpleo getUsuarioLogeado(UVDatos datos) throws SQLException, UVException {
		Usuario usuario = datos.getUsuario();

		if (usuario == null) {
			throw new UVException(MENSAJE_ERROR_NO_HAY_USUARIO_LOGEADO);
		}

		UsuarioBolsaEmpleo usuarioBolsa = getUsuarioByCodCuenta(usuario.getUid());

		if (Boolean.TRUE.equals(usuarioBolsa.getBorrado())) {
			throw new UVException(MENSAJE_ERROR_USUARIO_BORRADO);
		}

		if (Boolean.TRUE.equals(usuarioBolsa.getExcluido())) {
			throw new UVException(MENSAJE_ERROR_USUARIO_EXCLUIDO);
		}

		return usuarioBolsa;
	}

	/**
	 * Devuelve el usuario candidato o excepción si no está logeado o no es de tipo
	 * candidato.
	 * 
	 * @param datos .
	 * @return candidato .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public UsuarioBolsaEmpleo getUsuarioCandidato(UVDatos datos) throws UVException, SQLException {
		UsuarioBolsaEmpleo usuario = this.getUsuarioLogeado(datos);

		if (!usuario.getRol().getValor().equals(ModeloRol.ROL_CANDIDATO)) {
			throw new UVException(MENSAJE_ERROR_NO_ES_CANDIDATO);
		}

		return usuario;
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
	 * Devuelve un listado de usuarios por su id.
	 * 
	 * @param codcuenta codcuenta de usuarios
	 * @return listado de usuarios
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public UsuarioBolsaEmpleo getUsuarioByCodCuenta(String codcuenta) throws SQLException, UVException {
		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE bepusu.CODCUENTA = ?";
		String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				Connection conexionArcos = ConexionArcos.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);
				PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos)) {
			stmt.setString(1, codcuenta);

			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException(MENSAJE_ERROR_USUARIO_CON_DOCUMENTO_NO_EXISTE);
				}

				stmtArcos.setString(1, rs.getString("PRSNIF"));
				try (ResultSet rsArcos = stmtArcos.executeQuery()) {
					if (!rsArcos.next()) {
						throw new UVException(MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE);
					}

					return setUsuario(rs, rsArcos);
				}
			}
		}
	}

	/**
	 * Listado de areas.
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException  error si no existe la area
	 */
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaUsuarioBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();

		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE 1=1";
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";

					try (Connection conexionArcos = ConexionArcos.obtenerInstancia();
							PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos)) {
						stmtArcos.setString(1, rs.getString("PRSNIF"));
						try (ResultSet rsArcos = stmtArcos.executeQuery()) {
							if (!rsArcos.next()) {
								throw new UVException(MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE);
							}

							UsuarioBolsaEmpleo usuario = setUsuario(rs, rsArcos);
							usuarios.add(usuario);
						}
					}
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

		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE bepusu.rol = ?";

		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				Connection conexionArcos = ConexionArcos.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			
			int paramIndex = 1;
			stmt.setInt(paramIndex, ModeloRol.ID_ROL_CANDIDATO);
			stmtCount.setInt(paramIndex++, ModeloRol.ID_ROL_CANDIDATO);
			dataTable.setFiltersParams(stmt, stmtCount, paramIndex);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";

					try (PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos)) {
						stmtArcos.setString(1, rs.getString("PRSNIF"));
						try (ResultSet rsArcos = stmtArcos.executeQuery()) {
							if (!rsArcos.next()) {
								throw new UVException(MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE);
							}

							UsuarioBolsaEmpleo usuario = setUsuario(rs, rsArcos);
							usuarios.add(usuario);
						}
					}
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
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaUsuarioBorradoBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();

		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE bepusu.FLGBORRADO='S'";

		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				Connection conexionArcos = ConexionArcos.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			dataTable.setFiltersParams(stmt, stmtCount, 1);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";

					try (PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos)) {
						stmtArcos.setString(1, rs.getString("PRSNIF"));
						try (ResultSet rsArcos = stmtArcos.executeQuery()) {
							if (!rsArcos.next()) {
								throw new UVException(MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE);
							}

							UsuarioBolsaEmpleo usuario = setUsuario(rs, rsArcos);
							usuarios.add(usuario);
						}
					}
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
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaUsuarioExcluidoBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();

		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE FLGEXCLUIDO = 'S' AND FLGBORRADO <> 'S'";

		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				Connection conexionArcos = ConexionArcos.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {

			dataTable.setFiltersParams(stmt, stmtCount, 1);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String consultaArcos = "SELECT * FROM arcos.VUJA_NET_BEP_AR_PERSONA WHERE PRSNIF = ?";

					try (PreparedStatement stmtArcos = conexionArcos.prepareStatement(consultaArcos)) {
						stmtArcos.setString(1, rs.getString("PRSNIF"));
						try (ResultSet rsArcos = stmtArcos.executeQuery()) {
							if (!rsArcos.next()) {
								throw new UVException(MENSAJE_ERROR_USUARIO_UJA_CON_DOCUMENTO_NO_EXISTE);
							}

							UsuarioBolsaEmpleo usuario = setUsuario(rs, rsArcos);
							usuarios.add(usuario);
						}
					}
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}

		return dataTable;
	}

	/**
	 * Listado de areas excluidas de un usuario.
	 * 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param codnum .
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException  error si no existe la area
	 */
	public BolsaEmpleoDataTable<Bolsa> listaAreasExcluidasPorUsuarioDatatable(Map<String, String[]> params, Integer codnum) throws SQLException, UVException {
		List<Bolsa> bolsas = new ArrayList<>();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();

		String consulta = "SELECT bepare.CODNUM as CODNUMAREA, bepbol.CODNUM AS CODNUMBOLSA, bepare.ID_AREA_CONOCIMIENTO, "
				+ "bepare.DES_AREA_CONOCIMIENTO, bepbol.FLGBAREMABLE , bepbol.BEPARE_CODNUM, "
				+ "bepbol.ESTADO, bepbol.FECHAACTUALIZACION , bepbol.FECHABLOQUEO ,bepbol.FECHADEBLOQUEO "
				+ "FROM TBEP_USU_EXCLUIDOS_AREA bepuea " + "INNER JOIN TBEP_AREAS bepare ON bepuea.AREA=bepare.CODNUM "
				+ "INNER JOIN TBEP_BOLSAS bepbol ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
				+ "INNER JOIN TBEP_USUARIOS bepusu ON bepuea.USUARIO = bepusu.CODNUM " + "WHERE bepuea.USUARIO = ? ";

		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<>(params);

		dataTable.setColumn(ModeloArea.ORDER_COLUMN_INDEX_ID, "bepare.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ModeloArea.ORDER_COLUMN_INDEX_CODIGO, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ModeloArea.ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ModeloArea.ORDER_COLUMN_INDEX_BAREMALE, "bepbol.FLGBAREMABLE",
				DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery())) {
			int indexParam = 1;
			stmt.setInt(indexParam, codnum);
			stmtCount.setInt(indexParam++, codnum);

			dataTable.setFiltersParams(stmt, stmtCount, indexParam);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Bolsa bolsa = new Bolsa();
					bolsa.setCodNum(rs.getInt("CODNUMBOLSA"));
					bolsa.setArea(modeloArea.getAreaById(rs.getInt("BEPARE_CODNUM")));
					bolsa.setEstado(rs.getString("ESTADO"));
					bolsa.setBaremable(rs.getString("FLGBAREMABLE").equals("S"));
					bolsa.setFechaActualizacion(rs.getTimestamp("FECHAACTUALIZACION"));
					bolsa.setFechaBloqueo(rs.getTimestamp("FECHABLOQUEO"));
					bolsa.setFechaDesBloqueo(rs.getTimestamp("FECHADEBLOQUEO"));

					bolsas.add(bolsa);
				}
			}

			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(bolsas);
		}

		return dataTable;
	}

	/**
	 * Set usuario.
	 * 
	 * @param rs      resultado de la consulta de UVIRTUAL
	 * @param rsArcos .
	 * @return usuario
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException  en caso de no poder crear el usuario
	 */
	public UsuarioBolsaEmpleo setUsuario(ResultSet rs, ResultSet rsArcos) throws SQLException, UVException {
		ModeloRol modeloRol = ModeloRol.obtenerInstancia();
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();

		usuario.setNumDocumento(rsArcos.getString("PRSNIF"));
		usuario.setTipoDocumento(rsArcos.getString("STRTIPODOCUMENTO"));
		usuario.setEmail(rsArcos.getString("EMAIL_ALTA"));
		usuario.setNombre(rsArcos.getString("STRNOMBRE"));
		usuario.setPrimerApellido(rsArcos.getString("STRAPELLIDO1"));
		usuario.setSegundoApellido(rsArcos.getString("STRAPELLIDO2"));

		usuario.setCodNum(rs.getInt("CODNUM"));
		usuario.setCodCuenta(rs.getString("CODCUENTA"));
		usuario.setDireccion(rs.getString("DIRECCION"));
		usuario.setCodigoPostal(rs.getString("CODIGOPOSTAL"));
		usuario.setLocalidad(rs.getString("LOCALIDAD"));
		usuario.setProvincia(rs.getString("PROVINCIA"));
		usuario.setNacionalidad(rs.getString("NACIONALIDAD"));
		usuario.setTelefono(rs.getString("TELEFONO"));
		usuario.setRol(modeloRol.getRoleById(rs.getInt("ROL")));
		usuario.setListaDist(rs.getString("FLGLISTADISTRIBUCION").equals("S"));
		usuario.setExcluido(rs.getString("FLGEXCLUIDO").equals("S"));
		usuario.setExcluidoTipo(rs.getString("FLGEXCLUIDOTIPO"));
		usuario.setFechaExclusionInicio(rs.getTimestamp("FECHA_EXCLUSION_INICIO"));
		usuario.setFechaExclusionFin(rs.getTimestamp("FECHA_EXCLUSION_FIN"));
		usuario.setRazonExcluido(rs.getString("RAZON_EXCLUSION"));
		usuario.setFechaExclusion(rs.getTimestamp("FECHA_EXCLUSION"));
		usuario.setBorrado(rs.getString("FLGBORRADO").equals("S"));
		usuario.setFechaBorrado(rs.getTimestamp("FECHA_BORRADO"));

		return usuario;
	}

	/**
	 * Get datatable.
	 * 
	 * @param params   parametrospara tabla
	 * @param consulta query de la consulta
	 * @return usuario
	 * @throws UVException en caso de error de paquete datatables
	 */
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> setDatatable(Map<String, String[]> params, String consulta) throws UVException {
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = new BolsaEmpleoDataTable<>(params);

		dataTable.setColumn(ORDER_COLUMN_INDEX_TIPO_DOCUMENTO, "PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO, "PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COD_CUENTA, "CODCUENTA");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS, "PRSNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ROL, "ROL", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_LISTA_DIST, "FLGLISTADISTRIBUCION", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(ORDER_COLUMN_INDEX_EXCLUIDO, "FLGEXCLUIDO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(ORDER_COLUMN_INDEX_BORRADO, "FLGBORRADO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);

		return dataTable;
	}

	/**
	 * Función que inserta un usuario en la BD.
	 * 
	 * @param usuario a insertar en la BD
	 * @throws SQLException en caso de error en la BD
	 */
	public void insertaUsuario(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioQueInserta) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("No se puede insertar un usuario vacio");
		}
		if (usuario.getRol() == null) {
			throw new UVException("No se puede insertar un usuario sin rol");
		}

		ArrayList<String> columns = new ArrayList<>();
		columns.add("UID_USUARIO");
		columns.add("CODCUENTA");
		columns.add("PRSNIF");
		columns.add("ROL");
		columns.add("FLGLISTADISTRIBUCION");

		if (Boolean.TRUE.equals(usuario.getExcluido())) {
			columns.add("FLGEXCLUIDO");
			columns.add("FLGEXCLUIDOTIPO");
			columns.add("RAZON_EXCLUSION");
			columns.add("FECHA_EXCLUSION");

			if (usuario.getExcluidoTipo() != null && usuario.getExcluidoTipo().equals(EXCLUSION_TIPO_TEMPORAL)) {
				columns.add("FECHA_EXCLUSION_INICIO");
				columns.add("FECHA_EXCLUSION_FIN");
			}
		}

		String query = "INSERT INTO TBEP_USUARIOS (" + String.join(",", columns) + ") VALUES ("
				+ BolsaEmpleoUtils.consultaMultiplesParametros(columns.size()) + ")";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, usuarioQueInserta.getCodCuenta());
			stmt.setString(parameterIndex++, usuario.getCodCuenta());
			stmt.setString(parameterIndex++, usuario.getNumDocumento());
			stmt.setInt(parameterIndex++, usuario.getRol().getCodNum());
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(usuario.getListaDist()) ? "S" : "N");

			if (Boolean.TRUE.equals(usuario.getExcluido())) {
				stmt.setString(parameterIndex++, Boolean.TRUE.equals(usuario.getExcluido()) ? "S" : "N");
				stmt.setString(parameterIndex++, usuario.getExcluidoTipo());
				stmt.setString(parameterIndex++, usuario.getRazonExcluido());
				stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusion().getTime()));

				if (usuario.getExcluidoTipo() != null && usuario.getExcluidoTipo().equals(EXCLUSION_TIPO_TEMPORAL)) {
					stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusionInicio().getTime()));
					stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusionFin().getTime()));
				}
			}

			stmt.executeUpdate();
		}
	}

	/**
	 * Actualiza un usuario.
	 * 
	 * @param usuario UsuarioBolsaEmpleo con los datos nuevos a actualizar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  en caso de errores de validacion
	 */
	public void actualizaUsuario(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioQueActualiza) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("usuario obligatorio");
		}
		if (usuario.getCodNum() == null) {
			throw new UVException("id usuario no válido");
		}

		ArrayList<String> columns = new ArrayList<>();
		columns.add("UID_USUARIO");
		columns.add("ROL");
		columns.add("FLGLISTADISTRIBUCION");
		columns.add("FLGEXCLUIDO");
		columns.add("FLGEXCLUIDOTIPO");
		columns.add("RAZON_EXCLUSION");
		columns.add("FECHA_EXCLUSION");

		if (usuario.getExcluidoTipo() != null && usuario.getExcluidoTipo().equals(EXCLUSION_TIPO_TEMPORAL)) {
			columns.add("FECHA_EXCLUSION_INICIO");
			columns.add("FECHA_EXCLUSION_FIN");
		}

		String query = "UPDATE TBEP_USUARIOS SET "
				+ columns.stream().map(c -> c + "=?").collect(Collectors.joining(",")) + " WHERE CODNUM = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, usuarioQueActualiza.getCodCuenta());
			stmt.setInt(parameterIndex++, usuario.getRol().getCodNum());
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(usuario.getListaDist()) ? "S" : "N");
			stmt.setString(parameterIndex++, Boolean.TRUE.equals(usuario.getExcluido()) ? "S" : "N");
			stmt.setString(parameterIndex++, usuario.getExcluidoTipo());
			stmt.setString(parameterIndex++, usuario.getRazonExcluido());

			if (usuario.getFechaExclusion() != null) {
				stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusion().getTime()));
			} else {
				stmt.setNull(parameterIndex++, Types.NULL);
			}

			if (usuario.getExcluidoTipo() != null && usuario.getExcluidoTipo().equals(EXCLUSION_TIPO_TEMPORAL)) {
				stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusionInicio().getTime()));
				stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusionFin().getTime()));
			}

			stmt.setInt(parameterIndex++, usuario.getCodNum());
			stmt.executeUpdate();
		}

		actualizaRol(usuario);
	}

	/**
	 * Actualiza los datos personale de un usuario.
	 * 
	 * @param usuario             UsuarioBolsaEmpleo con los datos nuevos a
	 *                            actualizar
	 * @param usuarioQueActualiza .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  en caso de errores de validacion
	 */
	public void actualizaUsuarioMisDatos(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioQueActualiza) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("usuario obligatorio");
		}
		if (usuario.getCodNum() == null) {
			throw new UVException("id usuario no válido");
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
		
		refrescarUsuario(usuario.getCodCuenta(), usuario.getNumDocumento());
	}

	/**
	 * Establece el usuario como borrado.
	 * 
	 * @param usu               .
	 * @param areas             .
	 * @param usuarioQueExcluye .
	 * @throws SQLException .
	 */
	public void excluirUsuarioArea(UsuarioBolsaEmpleo usu, List<Area> areas, UsuarioBolsaEmpleo usuarioQueExcluye) throws SQLException {
		this.excluirUsuarioAreas(areas, usu, usuarioQueExcluye);
	}

	private void excluirUsuarioAreas(List<Area> areas, UsuarioBolsaEmpleo usu, UsuarioBolsaEmpleo usuarioQueExcluye) throws SQLException {
		try (Connection conexion = ConexionUvirtual.obtenerInstancia()) {
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
				stmt.setInt(1, usu.getCodNum());
				stmt.setInt(2, area.getCodNum());
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
		
		refrescarUsuario(usuario.getCodCuenta(), usuario.getNumDocumento());
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
		
		refrescarUsuario(usuario.getCodCuenta(), usuario.getNumDocumento());
	}

	/**
	 * Establece el usuario como borrado.
	 * 
	 * @param usuarios .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void ponerUsuarioComoBorrado(List<UsuarioBolsaEmpleo> usuarios, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		this.cambiarFlagBorradoUsuario(usuarios, USUARIO_BORRADO, usuarioUpdate);
	}

	/**
	 * Establece el usuario como NO borrado.
	 * 
	 * @param usuarios .
	 * @param usuarioUpdate .
	 * @throws SQLException .
	 */
	public void ponerUsuarioComoNoBorrado(List<UsuarioBolsaEmpleo> usuarios, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		this.cambiarFlagBorradoUsuario(usuarios, USUARIO_NO_BORRADO, usuarioUpdate);
	}

	private void cambiarFlagBorradoUsuario(List<UsuarioBolsaEmpleo> usuarios, String borrado, UsuarioBolsaEmpleo usuarioUpdate) throws SQLException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(usuarios.size());
		String query = "UPDATE TBEP_USUARIOS SET UID_USUARIO=?,FLGBORRADO=?,FECHA_BORRADO=? WHERE CODNUM IN  (" + params + ")";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;

			stmt.setString(indexParam++, usuarioUpdate.getCodCuenta());
			stmt.setString(indexParam++, borrado);
			stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDate().getTime()));			
			for (UsuarioBolsaEmpleo usuario : usuarios) {
				stmt.setInt(indexParam++, usuario.getCodNum());
			}
			stmt.executeUpdate();
		}
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
		if (usuario == null) {
			usuario = refrescarUsuario(usuArcos.getUid(), usuArcos.getDocumentoNumero());
		}
		
		// comprobamos si está borrado o excluido
		if (Boolean.TRUE.equals(usuario.getExcluido())) {
			throw new UVException(String.format("No puede acceder, su perfil ha sido excluido: [%s]", usuario.getCodCuenta()));
		} else if (Boolean.TRUE.equals(usuario.getBorrado())) {
			throw new UVException(String.format("No puede acceder, su perfil ha sido borrado de la base de datos: [%s]", usuario.getCodCuenta()));
		}
		
		return usuario; 
	}
	
	/**
	 * Crear el usuario si es necesario como candidato y lo guarda en el memcaché.
	 * @param uid del usuario.
	 * @param documentoNumero nif/dni/etc
	 * @return .
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public UsuarioBolsaEmpleo refrescarUsuario(String uid, String documentoNumero) throws SQLException, UVException {
		UsuarioBolsaEmpleo usuario;

		// comprobamos si existe el usuario en uvirtual
		LOGGER.log(Level.FINER, String.format("Chequeando si existe usuario en Bolsa Empleo [%s]", documentoNumero));

		if (!existeUsuarioBolsaEmpleoByNumeroDocumento(documentoNumero)) {
			LOGGER.log(Level.FINER, String.format("No existe usuario en Bolsa Empleo [%s]. Lo creamos como candidato.", documentoNumero));

			// no existe el usuario en la bolsa de empleo lo creamos como candidato
			UsuarioBolsaEmpleo usuarioFinal = new UsuarioBolsaEmpleo(
					documentoNumero, uid, ModeloRol.obtenerInstancia().getRoleById(ModeloRol.ID_ROL_CANDIDATO), true, false, null, null, null
			);

			insertaUsuario(usuarioFinal, usuarioFinal);
			usuario = getUsuarioByNumeroDocumento(documentoNumero);
			CrearUsuario.refrescarUsuario(usuario.getCodCuenta());
		} else {
			// cargamos los datos del usuario de bolsa de empleo
			LOGGER.log(Level.FINER, String.format("Existe usuario en Bolsa Empleo [%s]. Leemos sus datos.", documentoNumero));

			usuario = getUsuarioByNumeroDocumento(documentoNumero);
		}

		LOGGER.log(Level.FINER, String.format("Fin chequeo usuario Bolsa Empleo [%s]. Todo correcto, cacheamos", documentoNumero));
		
		Memcache mc = Memcache.getInstance();
		mc.set("usuarioBEP." + uid, usuario);

		return usuario;
	}

	/**
	 * Comprueba si existe un usuario de bolsa de empleo por su documento.
	 * 
	 * @param documento del usuario
	 * @return usuario con el nombre especificado
	 * @throws SQLException en caso de error en la BD
	 */
	public boolean existeUsuarioBolsaEmpleoByNumeroDocumento(String documento) throws SQLException, UVException {
		if (documento == null) {
			throw new UVException(MENSAJE_ERROR_DOCUMENTO_REQUERIDO);
		}

		String consulta = "SELECT * FROM TBEP_USUARIOS bepusu WHERE bepusu.PRSNIF = ?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta)) {
			stmt.setString(1, documento);

			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		}
	}

	/**
	 * Actualiza Rol usuario.
	 * 
	 * @param usu para asociar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException  si noticia no es valida
	 */
	public void actualizaRol(UsuarioBolsaEmpleo usu) throws SQLException, UVException {
		String consultaRol = "UPDATE ADM_USUARIO_ROL SET ROL_CODNUM=?, FLG_ADMIN=? WHERE USERUID=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consultaRol)) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, usu.getRol().getCodNum());
			stmt.setString(parameterIndex++, "N");
			stmt.setString(parameterIndex++, usu.getCodCuenta());
			stmt.executeUpdate();
		}
		
		refrescarUsuario(usu.getCodCuenta(), usu.getNumDocumento());
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
			try (ResultSet rs = stmt.executeQuery();) {
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
	public void cambiarFlagBorradoUsuarioRazon(UsuarioBolsaEmpleo usuario, UsuarioBolsaEmpleo usuarioQueBorra) throws SQLException {
		String query = "UPDATE TBEP_USUARIOS SET UID_USUARIO=?,FLGBORRADO=?,RAZON_BORRADO=?,FECHA_BORRADO=? WHERE CODNUM=?";

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;

			stmt.setString(indexParam++, usuarioQueBorra.getCodCuenta());
			stmt.setString(indexParam++, "S");
			stmt.setString(indexParam++, usuario.getRazonBorrado());
			stmt.setDate(indexParam++, new java.sql.Date(BolsaEmpleoUtils.getCurrentDateTime().getTime()));
			stmt.setInt(indexParam++, usuario.getCodNum());
			stmt.executeUpdate();
		}
	}

	/**
	 * método que crea un usuario en bolsa empleo y devuelve su id .
	 * 
	 * @param rol            .
	 * @param nombreUsuario  .
	 * @param usuarioQueCrea .
	 * @return usuario .
	 * @throws SQLException .
	 * @throws UVException  .
	 */
	public UsuarioBolsaEmpleo crearUsuarioBolsaEmpleo(Integer rol, String nombreUsuario, UsuarioBolsaEmpleo usuarioQueCrea) throws SQLException, UVException {
		Usuario usuArcos = CrearUsuario.usuario(nombreUsuario);

		Rol role = ModeloRol.obtenerInstancia().getRoleById(rol);
		UsuarioBolsaEmpleo usuarioNuevo = new UsuarioBolsaEmpleo(usuArcos.getDocumentoNumero(), usuArcos.getUid(), role,
				true, false, null, null, null);

		insertaUsuario(usuarioNuevo, usuarioQueCrea);
		CrearUsuario.refrescarUsuario(usuarioNuevo.getCodCuenta());

		return getUsuarioByNumeroDocumento(usuarioNuevo.getNumDocumento());
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
			return getUsuarioByNumeroDocumento(usuArcos.getDocumentoNumero());
		} catch (UVException ex) {
			return null;
		}
	}

}
