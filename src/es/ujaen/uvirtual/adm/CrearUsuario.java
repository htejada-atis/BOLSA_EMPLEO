package es.ujaen.uvirtual.adm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.uxxirrhh.Cargo;
import es.ujaen.uvirtual.modelo.ModeloAdministracion;
import es.ujaen.uvirtual.modelo.ModeloUsuarioArcos;
import es.ujaen.uvirtual.modelo.ModeloUsuarioUXXIAC;
import es.ujaen.uvirtual.modelo.ModeloUsuarioUXXIRRHH;
import es.ujaen.uvirtual.utilidades.Formateador;
import es.ujaen.uvirtual.utilidades.Memcache;

/**
 * Clase que crea los datos de un usuario.
 */
public class CrearUsuario {
	protected static Logger logger = Logger.getLogger(CrearUsuario.class.getPackage().getName());
	protected static String nombreDeEstaClase = CrearUsuario.class.getName();
	
	private CrearUsuario() { }
	
	/** Creación de un usuario a partir de su uid.
	 * @param uid uid a crear
	 * @return usuario
	 */
	public static Usuario usuario(String uid) {
		Memcache mc = Memcache.getInstance();
		Usuario usuario = (Usuario) mc.get("usuario." + uid);
		if (usuario != null) {
			return usuario;
		}
		return refrescarUsuario(uid);
	}

	private static boolean calculaRolesUvirtual(Usuario usuario, ArrayList<String> rolesDelUsuario, ArrayList<String> rolesAdministrados) {
		boolean errores = false;
		//Permisos de Universidad Virtual normales y de administrador
		try {
			String uid = usuario.getUid();
			ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
			List<String> rolesAdministradosUV = modeloAdministracion.listaRolesUsuario(uid, true);
			List<String> rolesDelUsuarioUV = modeloAdministracion.listaRolesUsuario(uid, false);
			
			for (String grp: rolesDelUsuarioUV) {
				if (!rolesDelUsuario.contains(grp)) {
					rolesDelUsuario.add(grp);
				}
			}
			// Los roles administrados se añaden como administrador y como rol del usr. 
			for (String grp: rolesAdministradosUV) {
				if (!rolesAdministrados.contains(grp)) {
					rolesAdministrados.add(grp);
				}
				if (!rolesDelUsuario.contains(grp)) {
					rolesDelUsuario.add(grp);
				}
			}
		} catch (SQLException e) {
			errores = true; // en caso de error no grabar en memcache
			logger.logp(Level.SEVERE, nombreDeEstaClase, "calculaRolesUvirtual", "Error de acceso a la base de datos universidad virtual: " + e.toString());
		} catch (NullPointerException e) {
			// Excepción cuando no está disponible la base de datos
			errores = true;
		}
		return errores;
	}
	
	private static boolean calculaRolesArcos(Usuario usuario, ArrayList<String> roles) {
		boolean errores = false;
		try {
			// si tenemos uid tenemos acceso a las partes públicas
			roles.add("publico");
			if (usuario.isCuentaInstitucional()) {
				roles.add("institucional");
			}
			// Recuperar roles de arcos y pasarlos a roles de UV.
			ModeloUsuarioArcos modeloUsuarioArcos = new ModeloUsuarioArcos();
			List<String> rolesArcos = modeloUsuarioArcos.listaPermisos(usuario.getCodigoCuentaArcos());
			for (String rol : rolesArcos) {
				if (!roles.contains(rol)) {
					roles.add(rol);
				}
			}
		} catch (SQLException e) {
			errores = true; // en caso de error no grabar en memcache
			logger.logp(Level.SEVERE, nombreDeEstaClase, "calculaRolesArcos", "Error de acceso a la base de datos ARCOS: " + e.toString());
		} catch (NullPointerException e) {
			// Excepción cuando no está disponible la base de datos
			errores = true;
		}
		return errores;
	}
	
	private static boolean calculaRolesRrhh(Usuario usuario, ArrayList<String> rolesDelUsuario) {
		boolean errores = false;
		//Permisos de UXXIRRHH 
		try {
			ModeloUsuarioUXXIRRHH modeloUsuarioRrhh = new ModeloUsuarioUXXIRRHH();
			ArrayList<String> rolesUxxiRrhh = modeloUsuarioRrhh.listaPermisos(usuario.getCodigoRRHH());
			for (String rol : rolesUxxiRrhh) {
				if (!rolesDelUsuario.contains(rol)) {
					rolesDelUsuario.add(rol);
				}
			}
			
			/*
			 * Comprobamos si es director de departamento (UXXIRRHH)
			 */
			usuario.setCargosRRHH(modeloUsuarioRrhh.listaCargos(usuario.getCodigoRRHH()));
			if (usuario.getCargosRRHH() != null) {
				for (Cargo cargo : usuario.getCargosRRHH()) {
					if (cargo.getCodigo().startsWith("13")) {
						rolesDelUsuario.add("dirdepartamento");
						usuario.setDirectorDepartamento("U" + cargo.getCodigo().substring(2));
					}
				}
			}
		} catch (SQLException e) {
			errores = true; // en caso de error no grabar en memcache
			logger.logp(Level.SEVERE, nombreDeEstaClase, "calculaRolesRrhh", "Error de acceso a la base de datos UXXIRRHH: " + Formateador.getStackTrace(e));
		} catch (NullPointerException e) {
			// Excepción cuando no está disponible la base de datos
			logger.logp(Level.SEVERE, nombreDeEstaClase, "calculaRolesRrhh", "null pointer UXXIRRHH: " + Formateador.getStackTrace(e));
			errores = true;
		}
		return errores;
	}

	private static boolean calculaRolesAc(Usuario usuario, ArrayList<String> rolesDelUsuario) {
		boolean errores = false;
		//Permisos de UXXIAC
		try {
			ModeloUsuarioUXXIAC modeloUsuarioAc = new ModeloUsuarioUXXIAC();
			ArrayList<String> rolesUxxiAC = modeloUsuarioAc.listaPermisos(usuario);
			for (String rol: rolesUxxiAC) {
				if (!rolesDelUsuario.contains(rol)) {
					rolesDelUsuario.add(rol);
				}
			}
		} catch (SQLException e) {
			errores = true; // en caso de error no grabar en memcache
			logger.logp(Level.SEVERE, nombreDeEstaClase, "calculaRolesAc", "Error de acceso a la base de datos UXXIAC: " + e.toString());
		} catch (NullPointerException e) {
			// Excepción cuando no está disponible la base de datos
			errores = true;
		}
		return errores;
	}
	
	private static boolean calculaRolesPorDominio(ArrayList<String> rolesDelUsuario, HashMap<String, ArrayList<String>> rolesPorDominio) {
		boolean errores = false;
		// 20130516 - filtrado de los roles al dominio
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		try {
			List<String> dominios = modeloAdministracion.listaDominios();
			for (String dominio : dominios) {
				rolesPorDominio.put(dominio, new ArrayList<>());
			}
			
			HashMap<String, List<String>> dominiosPorRol = modeloAdministracion.listaDominiosPorRol();
			for (String rol : rolesDelUsuario) {
				if (dominiosPorRol.get(rol) != null) {
					for (String dominio : dominiosPorRol.get(rol)) {
						rolesPorDominio.get(dominio).add(rol);
					}
				} else {
					// Si no se ha definido ningún dominio al rol, es para todos los dominios
					for (String dominio : dominios) {
						rolesPorDominio.get(dominio).add(rol);
					}
				}
			}
		} catch (SQLException e) {
			errores = true;
		}
		return errores;
	}
	
	/**
	 * Obtiene los datos y crea un Bean del Usuario a partir del "uid".
	 * @param uid identificador del usuario
	 * @return Bean con los datos del usuario
	 */
	public static Usuario refrescarUsuario(String uid) {
		logger.logp(Level.FINEST, nombreDeEstaClase, "refrescarUsuario", "creando el usuario: " + uid);
		
		ModeloUsuarioArcos modeloUsuarioArcos = new ModeloUsuarioArcos();
		Usuario usuario = null;
		boolean errores = false;
		try {
			// Recuperar roles del usuario.		
			ArrayList<String> rolesAdministrados = new ArrayList<>();
			ArrayList<String> rolesDelUsuario = new ArrayList<>();
			// Recuperamos el usuario de ARCOS
			usuario = modeloUsuarioArcos.listaCuentaUsuario(uid);
			boolean erroresArcos = false;
			boolean erroresUvirtual = false;
			boolean erroresRrhh = false;
			boolean erroresAc = false;
			boolean erroresDominio = false;
			if (usuario != null) {
				erroresArcos = calculaRolesArcos(usuario, rolesDelUsuario);
				erroresUvirtual = calculaRolesUvirtual(usuario, rolesDelUsuario, rolesAdministrados);
				erroresRrhh = calculaRolesRrhh(usuario, rolesDelUsuario);
				erroresAc = calculaRolesAc(usuario, rolesDelUsuario);
				HashMap<String, ArrayList<String>> rolesPorDominio = new HashMap<>();
				erroresDominio = calculaRolesPorDominio(rolesDelUsuario, rolesPorDominio);

				usuario.setRolesPorDominio(rolesPorDominio);
				usuario.setRoles(rolesPorDominio.get(usuario.getDominio()));
				usuario.setRolesAdministrados(rolesAdministrados);
			}
			
			if (erroresArcos || erroresUvirtual || erroresRrhh || erroresAc || erroresDominio || usuario == null) {
				errores = true;
			}
		} catch (SQLException e) {
			errores = true; // en caso de error no grabar en memcache
			logger.logp(Level.SEVERE, nombreDeEstaClase, "refrescarUsuario", "Error al crear usuario: " + e.toString());
		}

		if (!errores) {
			Memcache mc = Memcache.getInstance();
			mc.set("usuario." + uid, usuario);
		}
		return usuario;
	}
}
