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

import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Area;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Bolsa;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.Rol;
import es.ujaen.uvirtual.modulo.bolsaempleo.beans.UsuarioBolsaEmpleo;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.modulo.bolsaempleo.utilidades.BolsaEmpleoDataTable.DataTableColumn;
import es.ujaen.uvirtual.utilidades.UVException;

/**
 * Clase para obtener la información de usuarios de UVIRTUAL.
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * @author ATISoluciones 2021
 */
public class ModeloUsuarioBolsaEmpleo {
	
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
	
	public static final Integer PARAM_ROL_CANDIDATO_ID = 1051;
	
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
	
	public static final String ROL_SERVICIO_PERSONAL = "bolemppersonal";
	public static final String ROL_DIRECTOR_DEPARTAMENTO = "bolempdirdepartamento";
	public static final String ROL_MIEMBRO_COMISION = "bolempcomision";
	public static final String ROL_CANDIDATO = "bolempcandidato";
	
    protected static ModeloUsuarioBolsaEmpleo eInstancia = null;
	
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloUsuarioBolsaEmpleo();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloUsuarioBolsaEmpleo obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
    }
	
	/** Consulta usuarios en BBDD y las devuelve.
	 * @param clausula para filtrar los usuarios de la bd
	 * @return todos los usuarios de la base de datos
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException .
	 */
	public List<UsuarioBolsaEmpleo> listaUsuarios(String clausula) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		String consulta = "SELECT * FROM tbep_usuarios bepusu " + clausula;
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						UsuarioBolsaEmpleo usuario = setUsuario(rs);
						usuarios.add(usuario);
					}
				}
			}
		return usuarios;
	}
	
	/** lista todos los usuarios.
	 * @return lista de todas las noticias
	 * @throws SQLException si hay un error en la base de datos
	 * @throws UVException .
	 */
	public List<UsuarioBolsaEmpleo> listaUsuarios() throws SQLException, UVException {
		return listaUsuarios(" ORDER BY bepusu.CODCUENTA");
	}
	
	/** obtiene un usuario a partir de su nombre.
	 * @param nombre del usuario
	 * @return usuario con el nombre especificado
	 * @throws SQLException en caso de error en la BD
	 */
	public UsuarioBolsaEmpleo listaUsuario(String nombre) throws SQLException, UVException {
		String clausulaWhere = "WHERE bepusu.codcuenta = '" + nombre + "'";
		List<UsuarioBolsaEmpleo> usuarios = listaUsuarios(clausulaWhere);
		if (usuarios.isEmpty()) {
			throw new UVException("No existe el usuario");
		}
		return usuarios.get(0);
	}	
	
	/**
	 * Devuelve un usuario por su id.
	 * @param codNum id de usuario
	 * @return usuarioBolsaEmpleo
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si bolsa no es existe
	 */
	public UsuarioBolsaEmpleo getUsuarioById(int codNum) throws SQLException, UVException {
		String consulta = "SELECT * "
				+ "FROM TBEP_USUARIOS bepusu "
				+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
				+ "WHERE bepusu.CODNUM = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				stmt.setInt(1, codNum);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el usuario con id " + codNum);
				}
	
				UsuarioBolsaEmpleo usuario = setUsuario(rs);
				
				return usuario;
			}
		}
	}
	
	/**
	 * Devuelve el usuario de bolsa de empleo logeado en el sistema. 
	 * Si el usuario está borrado o excluido de la bolsa saltará un excepción.
	 * 
	 * @param datos .
	 * @return usuario o null si hay login
	 * @throws UVException .
	 * @throws SQLException .
	 */
	public UsuarioBolsaEmpleo getUsuarioLogeado(UVDatos datos) throws SQLException, UVException {
		Usuario usuario = datos.getUsuario();
		
		if (usuario == null) {
			return null;			
		}
		
		UsuarioBolsaEmpleo usuarioBolsa = getUsuarioByCodCuenta(usuario.getUid());
		
		if (usuarioBolsa.getBorrado()) {
			throw new UVException("El usuario está borrado de la bolsa de empleo");
		}
		
		if (usuarioBolsa.getExcluido()) {
			throw new UVException("El usuario está excluido de la bolsa de empleo");			
		}
		
		return usuarioBolsa;
	}
	
	/**
	 * Devuelve el usuario candidato o excepción si no está logeado o no es de tipo candidato.
	 * @param datos .
	 * @return candidato .
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public UsuarioBolsaEmpleo getUsuarioCandidato(UVDatos datos) throws UVException, SQLException {
		UsuarioBolsaEmpleo usuario = this.getUsuarioLogeado(datos);
		
		if (usuario == null) {
			throw new UVException("No hay usuario logeado");
		}
		
		if (!usuario.getRol().getValor().equals(ROL_CANDIDATO)) {
			throw new UVException("El usuario no es un candidato");
		} 
		
		return usuario;
	}
	
	/**
	 * Devuelve un listado de usuarios por su id.
	 * @param ids codnum de usurios
	 * @return listado de usuarios
	 * @throws UVException .
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
	 * @param codcuenta codcuenta de usuarios
	 * @return listado de usuarios
	 * @throws SQLException .
	 * @throws UVException .
	 */
	public UsuarioBolsaEmpleo getUsuarioByCodCuenta(String codcuenta) throws SQLException, UVException {
		String consulta = "SELECT * "
				+ "FROM TBEP_USUARIOS bepusu "
				+ "WHERE bepusu.CODCUENTA = ?";
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta);) {
				stmt.setString(1, codcuenta);
						
			try (ResultSet rs = stmt.executeQuery()) {
				if (!rs.next()) {
					throw new UVException("No existe el usuario con codcuenta = " + codcuenta);
				}
	
				return setUsuario(rs);
			}
		}
	}
	
	/**
	 * Listado de areas. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaUsuarioBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		
		String consulta =
		"SELECT * "
		+ "FROM TBEP_USUARIOS bepusu "
		+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
		+ "WHERE bepusu.rol != 1052";
		
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {		
			
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = setUsuario(rs);
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
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaUsuarioCandidatosBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		
		String consulta =
		"SELECT * "
		+ "FROM TBEP_USUARIOS bepusu "
		+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
		+ "WHERE bepusu.rol = 1052";
		
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {		
			
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = setUsuario(rs);
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
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */	
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaUsuarioBorradoBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		
		String consulta =
		"SELECT * "
		+ "FROM TBEP_USUARIOS bepusu "
		+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
		+ "WHERE bepusu.FLGBORRADO='S'";
		
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					UsuarioBolsaEmpleo usuario = setUsuario(rs);
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
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */	
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> listaUsuarioExcluidoBolsaEmpleoDatatable(Map<String, String[]> params) throws SQLException, UVException {
		List<UsuarioBolsaEmpleo> usuarios = new ArrayList<>();
		
		String consulta =
		"SELECT * "
		+ "FROM TBEP_USUARIOS bepusu "
		+ "INNER JOIN VUJA_NET_BEP_AR_PERSONA uvpersona ON uvpersona.CODINT=bepusu.CODPERSONA "
		+ "WHERE FLGEXCLUIDO='S' "
		+ "AND FLGBORRADO!='S'";
		
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = setDatatable(params, consulta);
			
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
			
			dataTable.setFiltersParams(stmt, stmtCount, 1);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {			
					UsuarioBolsaEmpleo usuario = setUsuario(rs);
					usuarios.add(usuario);					
				}				
			}
			
			dataTable.setRecordsTotalFromQuery(stmtCount);
			dataTable.setData(usuarios);
		}
		
		return dataTable;
	}	
	
	/**
	 * Listado de areas excluidas de un usuario. 
	 * @param params para leer los parametros de paginación, ordenacion, etc
	 * @param codnum .
	 * @return listado de areas
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException error si no existe la area
	 */	
	public BolsaEmpleoDataTable<Bolsa> listaAreasExcluidasPorUsuarioDatatable(Map<String, String[]> params, 
			Integer codnum) throws SQLException, UVException {
		
		List<Bolsa> bolsas = new ArrayList<>();
		ModeloArea modeloArea = ModeloArea.obtenerInstancia();	
		
		String consulta = "SELECT bepare.CODNUM as CODNUMAREA, bepbol.CODNUM AS CODNUMBOLSA, bepare.ID_AREA_CONOCIMIENTO, "
		+ "bepare.DES_AREA_CONOCIMIENTO, bepbol.FLGBAREMABLE , bepbol.BEPARE_CODNUM, "
		+ "bepbol.ESTADO, bepbol.FECHAACTUALIZACION , bepbol.FECHABLOQUEO ,bepbol.FECHADEBLOQUEO "
		+ "FROM UVIRTUAL.TBEP_USUARIOS_EXCLUIDOS_AREA bepuea "
		+ "INNER JOIN UVIRTUAL.TBEP_AREAS bepare ON bepuea.AREA=bepare.CODNUM "
		+ "INNER JOIN TBEP_BOLSAS bepbol ON bepare.CODNUM = bepbol.BEPARE_CODNUM "
		+ "INNER JOIN uvirtual.TBEP_USUARIOS bepusu ON bepuea.USUARIO = bepusu.CODNUM "
		+ "WHERE bepuea.USUARIO = ? ";
		
		BolsaEmpleoDataTable<Bolsa> dataTable = new BolsaEmpleoDataTable<Bolsa>(params);
		
		dataTable.setColumn(ModeloArea.ORDER_COLUMN_INDEX_ID, "bepare.CODNUM", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ModeloArea.ORDER_COLUMN_INDEX_CODIGO, "bepare.ID_AREA_CONOCIMIENTO");
		dataTable.setColumn(ModeloArea.ORDER_COLUMN_INDEX_AREA, "bepare.DES_AREA_CONOCIMIENTO");
		dataTable.setColumn(ModeloArea.ORDER_COLUMN_INDEX_BAREMALE, "bepbol.FLGBAREMABLE", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);

		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmtCount = conexion.prepareStatement(dataTable.getQueryCount());
				PreparedStatement stmt = conexion.prepareStatement(dataTable.getQuery());
		) {					
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
	 * @param rs resultado de la consulta
	 * @return usuario
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException en caso de no poder crear el usuario
	 */	
	public UsuarioBolsaEmpleo setUsuario(ResultSet rs) throws SQLException, UVException {
		ModeloRol modeloRol = ModeloRol.obtenerInstancia();
		
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		usuario.setCodNum(rs.getInt("CODNUM"));
		usuario.setCodPersona(rs.getInt("CODPERSONA"));
		usuario.setCodCuenta(rs.getString("CODCUENTA"));
		//usuario.setTipoDocumento(rs.getString("STRTIPODOCUMENTO"));
		//usuario.setNumDocumento(rs.getString("IDNIF") + rs.getString("LETRANIF"));
		//usuario.setNombre(rs.getString("STRNOMBRE"));
		//usuario.setPrimerApellido(rs.getString("STRAPELLIDO1"));
		//usuario.setSegundoApellido(rs.getString("STRAPELLIDO2"));
		usuario.setEmail(rs.getString("EMAIL"));
		usuario.setDireccion(rs.getString("DIRECCION"));
		usuario.setCodigoPostal(rs.getString("CODIGOPOSTAL"));
		usuario.setLocalidad(rs.getString("LOCALIDAD"));
		usuario.setProvincia(rs.getString("PROVINCIA"));
		usuario.setMovil(rs.getString("MOVIL"));
		usuario.setTelefono(rs.getString("TELEFONO"));
		usuario.setNacionalidad(rs.getString("NACIONALIDAD"));
		usuario.setSexo(rs.getString("SEXO"));
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
	 * Set usuario excluido de area. 
	 * @param rs resultado de la consulta
	 * @return usuario
	 * @throws SQLException en caso de error de base de datos
	 * @throws UVException en caso de no poder crear el usuario
	 */	
	public UsuarioBolsaEmpleo setUsuarioExcluidoArea(ResultSet rs) throws SQLException, UVException {
		ModeloRol modeloRol = ModeloRol.obtenerInstancia();
		
		UsuarioBolsaEmpleo usuario = new UsuarioBolsaEmpleo();
		usuario.setCodNum(rs.getInt("CODNUM"));
		usuario.setCodPersona(rs.getInt("CODPERSONA"));
		usuario.setCodCuenta(rs.getString("CODCUENTA"));
		usuario.setTipoDocumento(rs.getString("STRTIPODOCUMENTO"));
		usuario.setNumDocumento(rs.getString("IDNIF") + rs.getString("LETRANIF"));
		usuario.setNombre(rs.getString("STRNOMBRE"));
		usuario.setPrimerApellido(rs.getString("STRAPELLIDO1"));
		usuario.setSegundoApellido(rs.getString("STRAPELLIDO2"));
		usuario.setEmail(rs.getString("EMAIL"));
		usuario.setDireccion(rs.getString("DIRECCION"));
		usuario.setCodigoPostal(rs.getString("CODIGOPOSTAL"));
		usuario.setLocalidad(rs.getString("LOCALIDAD"));
		usuario.setProvincia(rs.getString("PROVINCIA"));
		usuario.setMovil(rs.getString("MOVIL"));
		usuario.setTelefono(rs.getString("TELEFONO"));
		usuario.setNacionalidad(rs.getString("NACIONALIDAD"));
		usuario.setSexo(rs.getString("SEXO"));
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
	 * @param params parametrospara tabla
	 * @param consulta query de la consulta
	 * @return usuario
	 * @throws UVException en caso de error de paquete datatables
	 */	
	public BolsaEmpleoDataTable<UsuarioBolsaEmpleo> setDatatable(Map<String, String[]> params, String consulta) throws UVException {
		BolsaEmpleoDataTable<UsuarioBolsaEmpleo> dataTable = new BolsaEmpleoDataTable<UsuarioBolsaEmpleo>(params);
		
		dataTable.setColumn(ORDER_COLUMN_INDEX_TIPO_DOCUMENTO, "uvpersona.STRTIPODOCUMENTO");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NUMDOCUMENTO, "uvpersona.IDNIF");
		dataTable.setColumn(ORDER_COLUMN_INDEX_COD_CUENTA, "bepusu.CODCUENTA");
		dataTable.setColumn(ORDER_COLUMN_INDEX_NOMBRE_Y_APELLIDOS, "uvpersona.STRAPELLIDO1");
		dataTable.setColumn(ORDER_COLUMN_INDEX_ROL, "bepusu.ROL", DataTableColumn.COLUMN_TYPE_NUMBER);
		dataTable.setColumn(ORDER_COLUMN_INDEX_LISTA_DIST, "bepusu.FLGLISTADISTRIBUCION", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(ORDER_COLUMN_INDEX_EXCLUIDO, "bepusu.FLGEXCLUIDO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setColumn(ORDER_COLUMN_INDEX_BORRADO, "bepusu.FLGBORRADO", DataTableColumn.COLUMN_TYPE_BOOLEAN);
		dataTable.setQuery(consulta);
		
		return dataTable;
	}
	
	/**	Función que inserta un usuario en la BD.
	 * @param usuario a insertar en la BD
	 * @throws SQLException en caso de error en la BD
	 */
	public void insertaUsuario(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("No se puede insertar un usuario vacio");
		}
		if (usuario.getRol() == null) {
			throw new UVException("No se puede insertar un usuario sin role");
		}
		
		if (!usuario.getExcluido()) {
			String consulta = "INSERT INTO tbep_usuarios " 
					+ " (CODPERSONA,CODCUENTA,ROL,EMAIL,FLGLISTADISTRIBUCION) "
					+ "VALUES (?, ?, ?, ?, ?)";
			
			try (Connection conexion = ConexionUvirtual.obtenerInstancia();
					PreparedStatement stmt = conexion.prepareStatement(consulta);) {
					int parameterIndex = 1;
					stmt.setInt(parameterIndex++, usuario.getCodPersona());
					stmt.setString(parameterIndex++, usuario.getCodCuenta());
					stmt.setInt(parameterIndex++, usuario.getRol().getCodNum());
					stmt.setString(parameterIndex++, usuario.getEmail());
					stmt.setString(parameterIndex++, usuario.getListaDist() ? "S" : "N");
					stmt.executeUpdate();
				}
		} else {
			String consulta = "INSERT INTO tbep_usuarios " 
					+ " (CODPERSONA,CODCUENTA,ROL,EMAIL,FLGLISTADISTRIBUCION,"
					+ "FLGEXCLUIDO,FLGEXCLUIDOTIPO,RAZON_EXCLUSION,FECHA_EXCLUSION";
					
					if (usuario.getExcluidoTipo() != null && usuario.getExcluidoTipo().equals("T")) {
						consulta += ",FECHA_EXCLUSION_INICIO,FECHA_EXCLUSION_FIN) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "; 
					} else {
						consulta += ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";
					}
			
			try (Connection conexion = ConexionUvirtual.obtenerInstancia();
					PreparedStatement stmt = conexion.prepareStatement(consulta);) {
					int parameterIndex = 1;
					stmt.setInt(parameterIndex++, usuario.getCodPersona());
					stmt.setString(parameterIndex++, usuario.getCodCuenta());
					stmt.setInt(parameterIndex++, usuario.getRol().getCodNum());
					stmt.setString(parameterIndex++, usuario.getEmail());
					stmt.setString(parameterIndex++, usuario.getListaDist() ? "S" : "N");
					stmt.setString(parameterIndex++, usuario.getExcluido() ? "S" : "N");
					stmt.setString(parameterIndex++, usuario.getExcluidoTipo());
					stmt.setString(parameterIndex++, usuario.getRazonExcluido());
					stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusion().getTime()));
					
					if (usuario.getExcluidoTipo() != null && usuario.getExcluidoTipo().equals("T")) {
						stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusionInicio().getTime()));
						stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusionFin().getTime()));
					}
					
					stmt.executeUpdate();
				}
		}
	}
	
	/** Actualiza un usuario.
	 * @param usuario UsuarioBolsaEmpleo con los datos nuevos a actualizar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de errores de validacion
	 */
	public void actualizaUsuario(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("usuario obligatorio");
		}
		if (usuario.getCodNum() == null) {
			throw new UVException("id usuario no válido");
		}
		
		String consulta = "UPDATE tbep_usuarios "
			+ " SET ROL=?, FLGLISTADISTRIBUCION=?, FLGEXCLUIDO=?, FLGEXCLUIDOTIPO=?,"
			+ " RAZON_EXCLUSION=?, FECHA_EXCLUSION=?";
		
		if (usuario.getExcluidoTipo() != null && usuario.getExcluidoTipo().equals("T")) {
			consulta += ", FECHA_EXCLUSION_INICIO = ?, FECHA_EXCLUSION_FIN = ?";
		}
		
		consulta += " WHERE CODNUM IN ?";	
		
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
		PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, usuario.getRol().getCodNum());
			stmt.setString(parameterIndex++, usuario.getListaDist() ? "S" : "N");
			stmt.setString(parameterIndex++, usuario.getExcluido() ? "S" : "N");
			stmt.setString(parameterIndex++, usuario.getExcluidoTipo());
			stmt.setString(parameterIndex++, usuario.getRazonExcluido());
			stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusion().getTime()));
			
			if (usuario.getExcluidoTipo() != null && usuario.getExcluidoTipo().equals("T")) {
				stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusionInicio().getTime()));
				stmt.setDate(parameterIndex++, new java.sql.Date(usuario.getFechaExclusionFin().getTime()));
			}
			
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			stmt.executeUpdate();
		}
		
		actualizaRol(usuario);
	}
	
	/** Actualiza los datos personale de un usuario.
	 * @param usuario UsuarioBolsaEmpleo con los datos nuevos a actualizar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException en caso de errores de validacion
	 */
	public void actualizaUsuarioMisDatos(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("usuario obligatorio");
		}
		if (usuario.getCodNum() == null) {
			throw new UVException("id usuario no válido");
		}
		
		String consulta = "UPDATE tbep_usuarios "
			+ " SET EMAIL=?, DIRECCION=?, CODIGOPOSTAL=?, LOCALIDAD=?, PROVINCIA=?, MOVIL=?, TELEFONO=?, NACIONALIDAD=?, SEXO=?, FLGLISTADISTRIBUCION=?"
			+ " WHERE codnum=?";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setString(parameterIndex++, usuario.getEmail());
			stmt.setString(parameterIndex++, usuario.getDireccion());
			stmt.setString(parameterIndex++, usuario.getCodigoPostal());
			stmt.setString(parameterIndex++, usuario.getLocalidad());
			stmt.setString(parameterIndex++, usuario.getProvincia());
			stmt.setString(parameterIndex++, usuario.getMovil());
			stmt.setString(parameterIndex++, usuario.getTelefono());
			stmt.setString(parameterIndex++, usuario.getNacionalidad());
			stmt.setString(parameterIndex++, usuario.getSexo());
			stmt.setString(parameterIndex++, usuario.getListaDist() ? "S" : "N");
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			stmt.executeUpdate();
		}
		
		String consulta2 = "UPDATE VUJA_NET_BEP_AR_PERSONA "
				+ " SET STRNOMBRE=?, STRAPELLIDO1=?, STRAPELLIDO2=?, EMAIL_ALTA=?"
				+ " WHERE codint=?";
			try (Connection conexion = ConexionUvirtual.obtenerInstancia();
				PreparedStatement stmt = conexion.prepareStatement(consulta2);) {
				int parameterIndex = 1;
				stmt.setString(parameterIndex++, usuario.getNombre());
				stmt.setString(parameterIndex++, usuario.getPrimerApellido());
				stmt.setString(parameterIndex++, usuario.getSegundoApellido());
				stmt.setString(parameterIndex++, usuario.getEmail());
				stmt.setInt(parameterIndex++, usuario.getCodPersona());
				stmt.executeUpdate();
		}
	}
	
	/**
	 * Establece el usuario como borrado. 
	 * @param usu .
	 * @param areas .
	 * @throws SQLException .
	 */
	public void excluirUsuarioArea(UsuarioBolsaEmpleo usu, List<Area> areas) throws SQLException {
		this.excluirUsuarioAreas(areas, usu);
	}
	
	private void excluirUsuarioAreas(List<Area> areas, UsuarioBolsaEmpleo usu) throws SQLException {
		String query = "INSERT INTO TBEP_USUARIOS_EXCLUIDOS_AREA (USUARIO,AREA) VALUES (?,?)";		
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {

			for (Area area : areas) {
				stmt.setInt(1, usu.getCodNum());
				stmt.setInt(2, area.getCodNum());	
				stmt.executeUpdate();
			}
		}
	}
	
	/**
	 * Establece el usuario como borrado. 
	 * @param usu .
	 * @param areas .
	 * @throws SQLException .
	 */
	public void incluirUsuarioArea(UsuarioBolsaEmpleo usu, List<Area> areas) throws SQLException {
		this.incluirUsuarioAreas(areas, usu);
	}
	
	private void incluirUsuarioAreas(List<Area> areas, UsuarioBolsaEmpleo usu) throws SQLException {
		String query = "DELETE FROM TBEP_USUARIOS_EXCLUIDOS_AREA WHERE usuario = ? AND area = ?";		
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {

			for (Area area : areas) {
				stmt.setInt(1, usu.getCodNum());
				stmt.setInt(2, area.getCodNum());	
				stmt.executeUpdate();
			}
		}
	}
	
	/**
	 * Establece el usuario como excluido. 
	 * @param usuario .
	 * @throws SQLException .
	 */
	public void ponerUsuarioComoExcluido(UsuarioBolsaEmpleo usuario) throws SQLException {
		
		String query = "UPDATE TBEP_USUARIOS SET FLGEXCLUIDO = ?, FLGEXCLUIDOTIPO = ?, "
				+ "RAZON_EXCLUSION = ?, FECHA_EXCLUSION = ?";
		
		if (usuario.getExcluidoTipo().equals("T")) {
			query += ", FECHA_EXCLUSION_INICIO = ?, FECHA_EXCLUSION_FIN = ?";
		}
		
		query += " WHERE CODNUM IN ?";	
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, USUARIO_EXCLUIDO);
			stmt.setString(indexParam++, usuario.getExcluidoTipo());
			stmt.setString(indexParam++, usuario.getRazonExcluido());
			stmt.setDate(indexParam++, new java.sql.Date(usuario.getFechaExclusion().getTime()));
			
			if (usuario.getExcluidoTipo().equals("T")) {
				stmt.setDate(indexParam++, new java.sql.Date(usuario.getFechaExclusionInicio().getTime()));
				stmt.setDate(indexParam++, new java.sql.Date(usuario.getFechaExclusionFin().getTime()));
			}
			
			stmt.setInt(indexParam++, usuario.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Establece el usuario como NO excluido. 
	 * @param usuario .
	 * @throws SQLException .
	 */
	public void ponerUsuarioComoNoExcluido(UsuarioBolsaEmpleo usuario) throws SQLException {
		String query = "UPDATE TBEP_USUARIOS SET FLGEXCLUIDO = ?, FLGEXCLUIDOTIPO = ?, "
				+ "FECHA_EXCLUSION_INICIO=?, FECHA_EXCLUSION_FIN=?, RAZON_EXCLUSION = ?, FECHA_EXCLUSION = ? WHERE CODNUM IN ?";		
		
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;
			stmt.setString(indexParam++, "N");
			stmt.setNull(indexParam++, Types.NULL);
			stmt.setNull(indexParam++, Types.NULL);
			stmt.setNull(indexParam++, Types.NULL);
			stmt.setNull(indexParam++, Types.NULL);
			stmt.setNull(indexParam++, Types.NULL);
			stmt.setInt(indexParam++, usuario.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/**
	 * Establece el usuario como borrado. 
	 * @param usuarios .
	 * @throws SQLException .
	 */
	public void ponerUsuarioComoBorrado(List<UsuarioBolsaEmpleo> usuarios) throws SQLException {
		this.cambiarFlagBorradoUsuario(usuarios, USUARIO_BORRADO);
	}
	
	/**
	 * Establece el usuario como NO borrado. 
	 * @param usuarios .
	 * @throws SQLException .
	 */
	public void ponerUsuarioComoNoBorrado(List<UsuarioBolsaEmpleo> usuarios) throws SQLException {
		this.cambiarFlagBorradoUsuario(usuarios, USUARIO_NO_BORRADO);
	}
	
	private void cambiarFlagBorradoUsuario(List<UsuarioBolsaEmpleo> usuarios, String borrado) throws SQLException {
		String params = BolsaEmpleoUtils.consultaMultiplesParametros(usuarios.size());
		String query = "UPDATE TBEP_USUARIOS SET FLGBORRADO = ?, FECHA_BORRADO = ? WHERE CODNUM IN  (" + params + ")";		
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;

			
			stmt.setString(indexParam++, borrado);
			
			Date date = new Date(System.currentTimeMillis());
			
			stmt.setDate(indexParam++, new java.sql.Date(date.getTime()));
			for (UsuarioBolsaEmpleo usuario : usuarios) {
				stmt.setInt(indexParam++, usuario.getCodNum()); 
			}
			stmt.executeUpdate();
		}	
	}
	
	/** 
	 * Si hay usuario logeado, comprueba si está registrado en sistema. 
	 * Si tenemos usuario en nuestro sistema, comprueba si está excluido o borrado (lanzando una excepción). 
	 * Si no tenemos usuario en nuestro sistema, lo crea como candidato.
	 *  
	 * @param  datos .
	 * @return Boolean true si hay usuario logeado en el sistema o false en otro caso.
	 * @throws SQLException en caso de error en la BD.
	 * @throws UVException si noticia no es valida.
	 */
	public Boolean checkUser(UVDatos datos) throws SQLException, UVException {
		Usuario usuArcos = datos.getUsuario();
		
		// no hay usuario logeado, salimos
		if (usuArcos == null) {
			return false;
		} 
		
		// comprobamos si el usuario existe en uvirtual (excepción si no existe)
		UsuarioBolsaEmpleo usuario = getUsuarioByCodCuenta(usuArcos.getUid());
	
		if (Boolean.TRUE.equals(usuario.getExcluido())) {
			throw new UVException("No puede acceder, su perfil ha sido excluido por los siguientes motivos: " + usuario.getRazonExcluido());
		} else if (Boolean.TRUE.equals(usuario.getBorrado())) {
			throw new UVException("No puede acceder, su perfil ha sido borrado de la base de datos");
		} else {
			try {
				listaUsuario(usuArcos.getUid());
			} catch (UVException e) {
				ModeloRol modeloRol = ModeloRol.obtenerInstancia();
				Rol role;
				
				try {
					role = modeloRol.getRoleById(PARAM_ROL_CANDIDATO_ID);
					
					UsuarioBolsaEmpleo usuarioFinal = 
					new UsuarioBolsaEmpleo(usuArcos.getCodigoPersonaArcos(), usuArcos.getUid(), role, true, false, null, null, null);
					
					insertaUsuario(usuarioFinal);
				
					CrearUsuario.refrescarUsuario(usuarioFinal.getCodCuenta());
					
				} catch (SQLException | UVException ex) {
					throw new UVException("Error creando usuario de bolsa de empleo");
				}
			} catch (SQLException e) {
				throw new UVException("Error al buscar usuario de bolsa de empleo");
			}	
			return true;
		}		
	}
	
	/** Elimina un usuario.
	 * @param usuario a borrar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si noticia no es valida
	 */
	public void borraUsuario(UsuarioBolsaEmpleo usuario) throws SQLException, UVException {
		if (usuario == null) {
			throw new UVException("No se puede eliminar un usuario vacía");
		}
		if (usuario.getCodNum() == null) {
			throw new UVException("No se puede eliminar un usuario con id vacío");
		}
		String consulta = "DELETE FROM tbep_usuarios WHERE codnum = ? ";
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(consulta);) {
			int parameterIndex = 1;
			stmt.setInt(parameterIndex++, usuario.getCodNum());
			stmt.executeUpdate();
		}
	}
	
	/** Actualiza Rol usuario.
	 * @param usu para asociar
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si noticia no es valida
	 */
	public void actualizaRol(UsuarioBolsaEmpleo usu) throws SQLException {
		String consultaRol = "UPDATE ADM_USUARIO_ROL "
				+ " SET ROL_CODNUM=?, FLG_ADMIN=?"
				+ " WHERE USERUID=?";
			try (Connection conexion = ConexionUvirtual.obtenerInstancia();
			PreparedStatement stmt = conexion.prepareStatement(consultaRol);) {
				int parameterIndex = 1;
				stmt.setInt(parameterIndex++, usu.getRol().getCodNum());
				stmt.setString(parameterIndex++, "N");
				stmt.setString(parameterIndex++, usu.getCodCuenta());
				stmt.executeUpdate();
			}
	}	
	
	/** Devuelve una lista con los roles de un usuario .
	 * @param uid del usuario
	 * @return Lista de roles del usuario
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si noticia no es valida
	 */
	public List<String> listaRolesUsuario(String uid) throws SQLException {
		ArrayList<String> roles = new ArrayList<>();
		String consultaRol = "SELECT admrol.valor "
				+ "FROM TBEP_USUARIOS bepusu "
				+ "LEFT JOIN ADM_ROL admrol ON admrol.ROL_CODNUM=bepusu.ROL "
				+ "WHERE bepusu.CODCUENTA= ? ";
		
	    	try (Connection conexion = ConexionUvirtual.obtenerInstancia();
	    		 PreparedStatement stmt = conexion.prepareStatement(consultaRol);) {
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
	
	
	/** Cambia flag usuario a borrado con razon de borrado .
	 * @param usuario .
	 * @throws SQLException en caso de error en la BD
	 * @throws UVException si noticia no es valida
	 */
	public void cambiarFlagBorradoUsuarioRazon(UsuarioBolsaEmpleo usuario) throws SQLException {
		String query = "UPDATE TBEP_USUARIOS SET FLGBORRADO = ?, RAZON_BORRADO = ?, FECHA_BORRADO = ? WHERE CODNUM = ?";		
				
		try (Connection conexion = ConexionUvirtual.obtenerInstancia(); PreparedStatement stmt = conexion.prepareStatement(query)) {
			int indexParam = 1;

			stmt.setString(indexParam++, "S");
			stmt.setString(indexParam++, usuario.getRazonBorrado());
			
			Date date = new Date(System.currentTimeMillis());
			
			stmt.setDate(indexParam++, new java.sql.Date(date.getTime()));
			stmt.setInt(indexParam++, usuario.getCodNum()); 
			stmt.executeUpdate();
		}	
	}
	
}
