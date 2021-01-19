package es.ujaen.uvirtual.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.uvirtual.beans.Acceso;
import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.Menu;
import es.ujaen.uvirtual.beans.MenuRol;
import es.ujaen.uvirtual.beans.MenuSubred;
import es.ujaen.uvirtual.beans.Sistema;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.conexion.ConexionUvirtual;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.Memcache;

/**
 * Clase de administración implementando el modelo singleton para evitar múltiples conexiones a BD.
 * Modelo - Operaciones con nombres: lista,    actualiza,  borra,    inserta
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * 20120704 - julopez - pasa de eConexion a eDataSource y recupera las conexiones de forma dinámina en lugar de tener la conexión.
 * 						puesto que plantea problemas con los timeouts
 * 
 * @author julopez
 *
 */
public class ModeloAdministracion {
	private static String eNombreDeEstaClase = ModeloAdministracion.class.getName();
    private static final Logger ELOGGER = Logger.getLogger(eNombreDeEstaClase);
    protected static ModeloAdministracion eInstancia = null;

    
    /********************************************** METODOS PARA SINGLETON *******************************************/
    /**
     * Constructor privado de modo que no se pueden crear clases del objeto. 
     */
    private ModeloAdministracion() { }
    
	/** Crea una instancia del objeto.
	 *  de forma sincronizada para protegerse de posibles problemas multi-hilo
	 */
	private static synchronized void crearInstancia() {
		if (eInstancia == null) {
			eInstancia = new ModeloAdministracion();
		}
	}

    /**
     * Obtiene una instancia de la conexión.
     * @return instancia
     */
    public static ModeloAdministracion obtenerInstancia() {
        if (eInstancia == null) {
        	crearInstancia();
        }
        return eInstancia;
    }

    /********************************************** METODOS PÚBLICOS PARA CONSULTAS *******************************************/
   
    /** lista Identificadores de Idiomas.
     * @return Obtiene la lista de identificadores de idiomas como un String[] 
     * @throws SQLException error de conexión a la base de datos
     */
    public String[] listaIdentificadoresDeIdiomas() throws SQLException {
    	String[] salida = null;
		ELOGGER.logp(Level.FINEST, eNombreDeEstaClase, "listaIdentificadoresDeIdiomas", "recuperando idiomas");
		Memcache mc = Memcache.getInstance();
		
		String[] mcData = (String[]) mc.get("madmin.listaIdentificadoresDeIdiomas");
		if (mcData != null) {
			return mcData;
		}
			
    	ArrayList<String> v = new ArrayList<>();
    	try (Connection conexion = ConexionUvirtual.obtenerInstancia();
    		 Statement stmt = conexion.createStatement();) {
    		try (ResultSet rs = stmt.executeQuery("SELECT idm_codalf FROM adm_idioma");) {
	    		while (rs.next()) {
	    			v.add(rs.getString("idm_codalf"));
	    		}
    		}
    	}
    	if (!v.isEmpty()) {
    		String[] r = v.toArray(new String[0]);
    		mc.set("madmin.listaIdentificadoresDeIdiomas", r);
    		salida = r;
    	}
    	return salida;
    }
   
    /**
     * lista menus.
     * @param controlador si se indica filtra por el controlador indicado
     * @param raiz si no es negativo, indica obtener sólo los nodos hijos directos del menú indicado
     * @return Lista de menús que cumplen la condición
     * @throws SQLException error de conexión a la base de datos
     */
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity", 
		"checkstyle:JavaNCSS", "checkstyle:ExecutableStatementCount", "checkstyle:MethodName"})
    private ArrayList<Menu> _listaMenus(String controlador, int raiz) throws SQLException {
    	ArrayList<Menu> menus = new ArrayList<>();
		
    	String consultaDeMenu 
    		= "select "  
   			+ "    mnu.mnu_codnum, " 
   			+ "    mnu.padre_codnum, " 
   			+ "    mnu.controlador, " 
   			+ "    mnu.flg_disponible, " 
   			+ "    mnu.flg_mostrar, " 
   			+ "    mnu.flg_creamenu, " 
   			+ "    mnu.flg_pdfinterno, " 
   			+ "    mnu.flg_word, " 
   			+ "    mnu.flg_excel, " 
   			+ "    mnu.flg_anonimo, " 
   			+ "    mnu.flg_log, " 
   			+ "    mnu.direccion, " 
   			+ "    mnu.telefono, " 
   			+ "    mnu.fax, " 
   			+ "    mnu.email " 
   			+ "from adm_menu mnu " 
   			+ "where (1=1) ";
    	String consultaDeRoles 
    		= "select mnu_rol.mnu_codnum, mnu_rol.rol_codnum, mnu_rol.flg_desact, mnu_rol.flg_admin, " 
    		+ "       arol.descripcion, arol.valor " 
    		+ "  from adm_rol arol, " 
    		+ "       adm_menu_rol mnu_rol " 
    		+ " where mnu_rol.rol_codnum = arol.rol_codnum " 
    		+ "   and mnu_rol.mnu_codnum = ? ";
    	String consultaDeIdiomas 
    		= "select idm_codalf, descripcion " 
    		+ "  from adm_menu_idm " 
    		+ " where mnu_codnum = ? ";
    	String consultaDeRedes 
    		= "select mnu_codnum, red, descripcion, flg_desact " 
    		+ "  from adm_menu_red " 
    		+ " where mnu_codnum = ? ";
    	String consultaDeSistemas 
    		= "select mnu_codnum, sis_codalf " 
    		+ "  from adm_menu_sis "
    		+ " where mnu_codnum = ? ";
    	
    	if (controlador != null) { 
    		consultaDeMenu += "  and mnu.controlador = ? ";
    	}
    	if (raiz >= 0) {
    		consultaDeMenu += "  and mnu.padre_codnum = ?";
    	}

    	try (Connection conexion = ConexionUvirtual.obtenerInstancia();
    		 PreparedStatement stmt = conexion.prepareStatement(consultaDeMenu);
    		 PreparedStatement stmtRoles = conexion.prepareStatement(consultaDeRoles);
    		 PreparedStatement stmtIdiomas = conexion.prepareStatement(consultaDeIdiomas);
    		 PreparedStatement stmtSubredes = conexion.prepareStatement(consultaDeRedes);
    		 PreparedStatement stmtSistemas = conexion.prepareStatement(consultaDeSistemas);) {
    		int indiceParametro = 1;
    		if (controlador != null) {
    			stmt.setString(indiceParametro++, controlador);
    		}
    		if (raiz >= 0) {
    			stmt.setInt(indiceParametro++, raiz);
    		}
    		
    		try (ResultSet rs = stmt.executeQuery();) {
	    		while (rs.next()) {
	    			Menu menu = new Menu();
	    			menu.setCodigo(rs.getInt("mnu_codnum"));
	    			menu.setCodigoPadre(rs.getInt("padre_codnum"));
	    			menu.setControlador(rs.getString("controlador"));
	    			menu.setDisponible("S".equals(rs.getString("flg_disponible")));
	    			menu.setMostrar("S".equals(rs.getString("flg_mostrar")));
	    			menu.setCrearMenu("S".equals(rs.getString("flg_creamenu")));
	    			menu.setPdfInterno("S".equals(rs.getString("flg_pdfinterno")));
	    			menu.setWord("S".equals(rs.getString("flg_word")));
	    			menu.setExcel("S".equals(rs.getString("flg_excel")));
	    			menu.setAccesoAnonimo("S".equals(rs.getString("flg_anonimo")));
	    			menu.setRegistrarLog("S".equals(rs.getString("flg_log")));
	    			menu.setDireccion(rs.getString("direccion"));
	    			menu.setTelefono(rs.getString("telefono"));
	    			menu.setFax(rs.getString("fax"));
	    			menu.setEmail(rs.getString("email"));
	    			// Recuperamos los roles y administradores
	    			
	    			stmtRoles.setInt(1, menu.getCodigo());
	    			ArrayList<MenuRol> rolesAcceso = new ArrayList<>();
	    			ArrayList<MenuRol> rolesAdministradores = new ArrayList<>();
	    			try (ResultSet rsRoles = stmtRoles.executeQuery();) {
		    			while (rsRoles.next()) {
		    				MenuRol menuRol = new MenuRol(
		    						rsRoles.getInt("mnu_codnum"),
		    						rsRoles.getInt("rol_codnum"),
		    						"S".equals(rsRoles.getString("flg_desact")),
		    						"S".equals(rsRoles.getString("flg_admin")),
		    						rsRoles.getString("descripcion"),
		    						rsRoles.getString("valor")
		    						);
		    				rolesAcceso.add(menuRol);
		    				if ("S".equals(rsRoles.getString("flg_admin"))) {
		    					rolesAdministradores.add(menuRol);
		    				}
		    			}
	    			}
	    			
	    			stmtIdiomas.setInt(1, menu.getCodigo());
	    			HashMap<String, String> idiomas = new HashMap<>();
	    			try (ResultSet rsIdiomas = stmtIdiomas.executeQuery();) {
		    			while (rsIdiomas.next()) {
		    				idiomas.put(rsIdiomas.getString("idm_codalf"), rsIdiomas.getString("descripcion"));
		    			}
	    			}
	    			
	    			// Recuperamos las redes
	    			stmtSubredes.setInt(1, menu.getCodigo());
	    			ArrayList<MenuSubred> subredes = new ArrayList<>();
	    			try (ResultSet rsSubredes = stmtSubredes.executeQuery();) {
		    			while (rsSubredes.next()) {
		    				subredes.add(
		    						new MenuSubred(
		    								rsSubredes.getInt("mnu_codnum"),
		    								"S".equals(rsSubredes.getString("flg_desact")),
		    								rsSubredes.getString("descripcion"),
		    								rsSubredes.getString("red")
		    								)
		    							);
		    			}
	    			}
	    			
	    			// Recuperamos los sistemas
	    			stmtSistemas.setInt(1, menu.getCodigo());
	    			ArrayList<String> sistemas = new ArrayList<>();
	    			try (ResultSet rsSistemas = stmtSistemas.executeQuery();) {
		    			while (rsSistemas.next()) {
		    				sistemas.add(rsSistemas.getString("sis_codalf"));
		    			}
	    			}
	    			
	    			menu.setRolesAcceso(rolesAcceso);
	    			menu.setRolesAdministrador(rolesAdministradores);
	    			menu.setIdiomas(idiomas);
	    			menu.setAccesoSubredes(subredes);
	    			if (!sistemas.isEmpty()) {
	    				menu.setSistemas(sistemas);
	    			}
	    			menus.add(menu);
	    		}
    		}
    	}
    	return menus;
    }
    
    /**
     * Obtener todos los menús (sin relación entre padres).
     * @return menús de la aplicación en un Hashmap(int, Menu)
     * @throws SQLException error de conexión a base de datos
     */
    public Map<Integer, Menu> listaMenus() throws SQLException {
		ELOGGER.logp(Level.FINEST, eNombreDeEstaClase, "listaMenus", "recuperando todos los menús");
		Memcache mc = Memcache.getInstance();
		
		@SuppressWarnings("unchecked")
		HashMap<Integer, Menu> menus = (HashMap<Integer, Menu>) mc.get("madmin.listaMenus"); 
		if (menus != null) {
			// memcache hit
			return menus;
		}
			
		
    	ArrayList<Menu> vmenus = _listaMenus(null, -1);
    	menus = new HashMap<>();
    	for (Menu emenu: vmenus) {
    		menus.put(emenu.getCodigo(), emenu);
    	}
    	mc.set("madmin.listaMenus", menus);
    	return menus;
    }

    /**
     * Obtener todos los elementos del menú raíz.
     * @return menú raíz de la aplicación en un Vector(Menu)
     * @throws SQLException error de conexión a base de datos
     */
    public List<Menu> listaMenuRaiz() throws SQLException {
		ELOGGER.logp(Level.FINEST, eNombreDeEstaClase, "listaMenuRaiz", "recuperando menú raíz");
		Memcache mc = Memcache.getInstance();
		
		@SuppressWarnings("unchecked")
		ArrayList<Menu> menus = (ArrayList<Menu>) mc.get("madmin.listaMenuRaiz"); 
		if (menus != null) {
			// memcache hit
			return menus;
		}
			
		
    	menus = _listaMenus(null, ConfiguracionGlobal.getCodigoMenuRaiz());  
    	mc.set("madmin.listaMenuRaiz", menus);
    	return menus;
    }

    /**
     * Obtener todos los hijos directos de un menú.
     * @param menu menu
     * @return un Vector(Menu) con los nodos hijos del dado
     * @throws SQLException error de conexión a base de datos
     */
    public List<Menu> listaMenuHijos(Menu menu) throws SQLException {
		ELOGGER.logp(Level.FINEST, eNombreDeEstaClase, "listaMenuHijos", "recuperando hijos del menú: " + menu.getCodigo());
    	return _listaMenus(null, menu.getCodigo());
    }
    
    
    /**
     * Obtener un menú determinado.
     * @param controlador controlador
     * @return el menú indicado o null si no existe
     * @throws SQLException error de conexión a la base de datos
     */
    public Menu listaMenuDeControlador(String controlador) throws SQLException {
		ELOGGER.logp(Level.FINEST, eNombreDeEstaClase, "listaMenu", "recuperando menú " + controlador);
		Memcache mc = Memcache.getInstance();
		Menu menu = (Menu) mc.get("madmin.listaMenu." + controlador);
		if (menu != null) {
			// memcache hit
			return menu;
		}
			
		
    	ArrayList<Menu> v = _listaMenus(controlador, -1);
    	menu = (v.isEmpty()) ? null : v.get(0);
    	if (menu != null) {
    		mc.set("madmin.listaMenu." + controlador, menu);
    	}
    	return menu;
    }

    /**
     * Obtiene los roles de un usuario.
     * @param uid identificador del usuario
     * @param admin	si filtramos sólo a roles que administra el usuario o roles que no administra
     * @return el vector de roles definidos para el usuario
     * @throws SQLException error de conexión a la base de datos
     */
    public List<String> listaRolesUsuario(String uid, boolean admin) throws SQLException {
		ELOGGER.logp(Level.FINEST, eNombreDeEstaClase, "listaRolesDeUsuario", "recuperando datos de usuario " + uid);
		ArrayList<String> roles = new ArrayList<>();
		
		String consulta 
			= "select arol.valor " 
   			+ "  from adm_rol arol, " 
   			+ "       adm_usuario_rol usr_rol " 
   			+ " where usr_rol.rol_codnum = arol.rol_codnum "  
   			+ "   and usr_rol.useruid = ? " 
   			+ "   and usr_rol.flg_admin = ? ";
		if (!admin) {
			consulta += 
				  "union all " 
				+ " select arol.valor " 
   				+ "   from adm_rol arol, " 
   				+ "        control_acceso_rol acc_rol " 
   				+ "  where acc_rol.rol_codnum = arol.rol_codnum "  
   				+ "    and acc_rol.useruid = ? ";
		}
		
    	try (Connection conexion = ConexionUvirtual.obtenerInstancia();
    		 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
    		int parameterIndex = 1;
    		stmt.setString(parameterIndex++, uid);
    		stmt.setString(parameterIndex++, admin ? "S" : "N");
    		if (!admin) {
        		stmt.setString(parameterIndex++, uid);
    		}
    		try (ResultSet rs = stmt.executeQuery();) {
	    		while (rs.next()) {
	    			roles.add(rs.getString("valor"));
	    		}
    		}
    	}
		return roles;
    }

	/**
	 * Log de acceso a Universidad Virtual.
	 * @param acceso acceso
	 * @throws SQLException si error bd
	 */
	public void insertaAcceso(Acceso acceso) {
		String consulta =
			"insert into log_acceso(fecha, servidor, acceso, ip, sesion, usuario, tiempo, comentario, params, excepcion) values (sysdate, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	try (Connection conexion = ConexionUvirtual.obtenerInstancia();
    		 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
    		int i = 1;
    		stmt.setString(i++, acceso.getServidor());
    		stmt.setString(i++, acceso.getUrl());
    		stmt.setString(i++, acceso.getIp());
    		stmt.setString(i++, acceso.getSesion());
    		stmt.setString(i++, acceso.getUsuario());
    		stmt.setLong(i++, acceso.getTiempo());
    		stmt.setString(i++, acceso.getComentarios());
    		stmt.setString(i++, acceso.getParametros());
    		stmt.setString(i++, acceso.getExcepcionesComoCadena());
    		stmt.executeUpdate();
    	} catch (SQLException e) {
    		// Dummy catch para evitar lanzar excepcion SQL
    		ELOGGER.logp(Level.SEVERE, eNombreDeEstaClase, "insertaAcceso", "Imposible registrar acceso");
    	}		
	}
	
	/**
	 * Recupera los sistemas y su estado (no se almacena en memcached por que puede requerirse rapidez).
	 * @return vector con los sistemas y su estado
	 * @throws SQLException si error db
	 */
	public List<Sistema> listaSistemas() throws SQLException {
    	ArrayList<Sistema> sistemas = new ArrayList<>();
		
    	String consulta 
    		= "select sis_codalf, descripcion, flg_manto, dat_manto, desc_manto " 
   			+ "  from adm_sistema ";

    	try (Connection conexion = ConexionUvirtual.obtenerInstancia();
    		 PreparedStatement stmt = conexion.prepareStatement(consulta);
    		 ResultSet rs = stmt.executeQuery();) {
    		while (rs.next()) {
    			Sistema sistema = new Sistema();
    			sistema.setCodigo(rs.getString("sis_codalf"));
    			sistema.setDescripcion(rs.getString("descripcion"));
    			sistema.setEnMantenimiento("S".equals(rs.getString("flg_manto")));
    			sistema.setFechaComprobacion(new java.util.Date());
    			if (sistema.isEnMantenimiento()) {
    				sistema.setFechaEntradaEnMantenimiento((rs.getString("dat_manto") != null) ? rs.getDate("dat_manto") : null);
    				sistema.setMotivoDelMantenimiento(rs.getString("desc_manto"));
    			}
    			sistemas.add(sistema);
    		}
    	}
    	return sistemas;
	}
	
	/**
	 * Obtiene el estado de los sistemas con la información almacenada en memcached.
	 * @return estado sistemas
	 */
	public static List<Sistema> listaEstadoSistemas() {
		Memcache mc = Memcache.getInstance();
		
		@SuppressWarnings("unchecked")
		ArrayList<Sistema> sistemas = (ArrayList<Sistema>) mc.get(ConfiguracionGlobal.getAtributoSistemas());
		return sistemas;
	}

	/**
	 * Lista los avisos o notificaciones del sistema.
	 * @param usuario usuario
	 * @return avisos del sistema
	 */
	public List<String> listaAvisosDelSistema(Usuario usuario) {
		return listaMensajesDelSistema("I", usuario);
	}

	/**
	 * Lista las advertencias o notificaciones del sistema.
	 * @param usuario usuario
	 * @return advertencias del sistema
	 */
	public List<String> listaAdvertenciasDelSistema(Usuario usuario) {
		return listaMensajesDelSistema("A", usuario);
	}
	

	/** lista mensajes del sistema.
	 * @param tipo tipo de mensaje
	 * @param usuario usuario
	 * @return lista de mensajes del sistema
	 */
	public List<String> listaMensajesDelSistema(String tipo, Usuario usuario) {
		List<String> mensajes = new ArrayList<>();
		
		String whereIn = " and rol.valor in ('publico'";
		List<String> whereValues = new ArrayList<>();
		if (usuario != null) {
			for (String rol : usuario.getRolesPorDominio().get(usuario.getDominio())) {
				whereValues.add(rol);
				whereIn += ", ?";
			}
		}
		whereIn += ")";
		
    	String consulta 
    		= "select avi.codnum, avi.descripcion, avi.texto_ampliado " 
    		+ "  from adm_aviso_inicio avi, " 
   			+ "       adm_aviso_rol rolavi, " 
   			+ "       adm_rol rol " 
   			+ " where avi.codnum = rolavi.avi_codnum " 
   			+ "   and rol.rol_codnum = rolavi.rol_codnum " 
   			+ "   and tipo = ? " 
   			+ whereIn 
   			+ "   and sysdate between datini and datfin " 
   			+ " order by datini desc, avi.codnum ";

    	try (Connection conexion = ConexionUvirtual.obtenerInstancia();
    		 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
    		int parameterIndex = 1;
    		stmt.setString(parameterIndex++, tipo);
    		for (String rol : whereValues) {
    			stmt.setString(parameterIndex++, rol);
    		}
    		try (ResultSet rs = stmt.executeQuery();) {
    		int idPrevio = -1;
	    		while (rs.next()) {
	    			if (rs.getInt("codnum") != idPrevio) {
	    				mensajes.add(rs.getString("descripcion") + ((rs.getString("texto_ampliado") == null) ? "" : ("\n" + rs.getString("texto_ampliado"))));
	    				idPrevio = rs.getInt("codnum");
	    			}
	    		}
    		}
    	} catch (SQLException e) {
    		ELOGGER.logp(Level.SEVERE, eNombreDeEstaClase, "listaMensajesDelSistema", Formateador.getStackTrace(e));
    	}
    	return mensajes;
	}


	/** lista dominios.
	 * @return dominios
	 * @throws SQLException si error db
	 */
	public List<String> listaDominios() throws SQLException {
		ELOGGER.logp(Level.FINEST, eNombreDeEstaClase, "listaDominios", "recuperando datos de dominios registrados");
		ArrayList<String> dominios = new ArrayList<>();

		Memcache mc = Memcache.getInstance();
		@SuppressWarnings("unchecked")
		ArrayList<String> mcdominios = (ArrayList<String>) mc.get("madmin.listaDominios");
		if (mcdominios != null) {
			// memcache hit
			return mcdominios;
		}
		
		String consulta = "select dom_codalf from adm_dom ";
    	try (Connection conexion = ConexionUvirtual.obtenerInstancia();
    		 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
    		try (ResultSet rs = stmt.executeQuery();) {
	    		while (rs.next()) {
	    			dominios.add(rs.getString("dom_codalf"));
	    		}
    		}
    		mc.set("madmin.listaDominios", dominios);
    	}
		return dominios;    	
    }
	
	/** lista dominios por rol.
	 * @return dominios por rol
	 * @throws SQLException si error en db
	 */
	public HashMap<String, List<String>> listaDominiosPorRol() throws SQLException {
		ELOGGER.logp(Level.FINEST, eNombreDeEstaClase, "listaDominiosPorRol", "recuperando datos de dominios por rol registrados");
		HashMap<String, List<String>> dominiosPorRol = new HashMap<>();

		Memcache mc = Memcache.getInstance();
		@SuppressWarnings("unchecked")
		HashMap<String, List<String>> mcdomxrol = (HashMap<String, List<String>>) mc.get("madmin.listaDominiosPorRol");
		if (mcdomxrol != null) {
			// memcache hit
			return mcdomxrol;
		}
			

		String consulta 
			= "select valor, dom_codalf " 
   			+ "  from adm_rol, adm_rol_dom " 
   			+ " where adm_rol_dom.rol_codnum = adm_rol.rol_codnum ";
 
    	try (Connection conexion = ConexionUvirtual.obtenerInstancia();
        	 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
    		try (ResultSet rs = stmt.executeQuery();) {
	    		while (rs.next()) {
	    			List<String> dominios = dominiosPorRol.get(rs.getString("valor"));
	    			if (dominios == null) {
	    				dominios = new ArrayList<>();
	    			}
	    			dominios.add(rs.getString("dom_codalf"));
	    			dominiosPorRol.put(rs.getString("valor"), dominios);
	    		}
    		}
    		mc.set("madmin.listaDominiosPorRol", dominiosPorRol);
    	}
		return dominiosPorRol;
    }

	/** lista configuracion.
	 * @param configuracion codigo de la configuracion
	 * @return claves y valor de la configuracion
	 * @throws SQLException si error en db
	 */
	public Map<String, String> listaConfiguracion(String configuracion) throws SQLException {
		final String nombreMetodo = "listaConfiguracion";
		ELOGGER.logp(Level.INFO, eNombreDeEstaClase, nombreMetodo, "recuperando la configuración " + configuracion);
		HashMap<String, String> datosDeLaConfiguracion = null;
		String consulta 
			= "select config_codalf, param_codalf, valor " 
			+ "  from adm_parametros " 
			+ " where config_codalf = ? " 
			+ "union all " 
			+ "select p1.config_codalf, p1.param_codalf, p1.valor " 
			+ "  from adm_parametros p1, " 
			+ "       adm_parametros p2 " 
			+ " where p1.config_codalf = 'default' " 
			+ "   and p1.param_codalf = p2.param_codalf(+) " 
			+ "   and p2.config_codalf(+) = ? " 
			+ "   and p2.config_codalf is null ";

    	try (Connection conexion = ConexionUvirtual.obtenerInstancia();
    		 PreparedStatement stmt = conexion.prepareStatement(consulta);) {
    		int parameterIndex = 1;
    		stmt.setString(parameterIndex++, configuracion);
    		stmt.setString(parameterIndex++, configuracion);
    		try (ResultSet rs = stmt.executeQuery();) {
	    		while (rs.next()) {
	    			if (datosDeLaConfiguracion == null) {
	    				datosDeLaConfiguracion = new HashMap<>();
	    			}
	    			ELOGGER.logp(Level.FINE, eNombreDeEstaClase, nombreMetodo, 
	    					"recuperando parámetro " + rs.getString("param_codalf") + " de la configuración " 
	    					+ rs.getString("config_codalf") + " con valor " + rs.getString("valor"));
	    			datosDeLaConfiguracion.put(rs.getString("param_codalf"), rs.getString("valor"));
	    		}
    		}
    		ELOGGER.logp(Level.INFO, eNombreDeEstaClase, nombreMetodo, "Se ha cargado correctamente la configuración " + configuracion);
    	}
		return datosDeLaConfiguracion;
	}
}