package es.ujaen.uvirtual.adm;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.ujaen.uvirtual.beans.ErroresPersonalizados;
import es.ujaen.uvirtual.beans.Menu;
import es.ujaen.uvirtual.beans.MenuRol;
import es.ujaen.uvirtual.beans.MenuSubred;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.utilidades.AyudaURL;

/** Clase para comprobar el acceso.
 * @author julopez
 */
public class CompruebaAcceso {
	protected static Logger logger = Logger.getLogger(CompruebaAcceso.class.getPackage().getName());
	protected static String nombreDeEstaClase = CompruebaAcceso.class.getName();

	private CompruebaAcceso() {
		//no permitido
	}
	
	/**
	 * Comprueba si un usuario tiene acceso a un elemento de menú.
	 * @param usuario usuario a comprobar
	 * @param menu menú a comprobar
	 * @param direccionIPUsuario dirección IP desde la que solicita el usuaro
	 * @return valor lógico si tiene acceso o no
	 */
	public static boolean acceso(Usuario usuario, Menu menu, String direccionIPUsuario) {
		return errorAcceso(usuario, menu, direccionIPUsuario, false) == null;
	}

	private static String mensajeNoAutorizado(Usuario usuario) {
		String salida = "Acceso no autorizado del usuario '";
		if (usuario == null || usuario.getUid() == null || "".equals(usuario.getUid())) {
			salida += "anónimo";
		} else {
			salida += usuario.getUid();
		}
		salida += "' al recurso '";
		return salida;
	}
	
	/**
	 * Comprueba si un usuario tiene acceso a un elemento de menú.
	 * @param usuario usuario a comprobar
	 * @param menu menú a comprobar
	 * @param direccionIPUsuario dirección IP desde la que solicita el usuaro
	 * @param url Url
	 * @param registraErrores registrar los errores con logger
	 * @return cadena de error o nulo si no existe error
	 */
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:ReturnCount", 
		"checkstyle:NPathComplexity", "checkstyle:JavaNCSS", "checkstyle:ExecutableStatementCount"})
	public static String errorAcceso(Usuario usuario, Menu menu, String direccionIPUsuario, String url, boolean registraErrores) {
		boolean tieneAcceso = false;
		String nombreEsteMetodo = "errorAcceso";
		int i = 0;

		List<MenuRol> rolesDelMenu = null;
		List<String> rolesDelUsuario = null;
		List<MenuSubred> subredesConAcceso = menu.getAccesoSubredes();
		
		if (usuario != null) {
			// Comprobamos si está en modo desactivado, con acceso los usuarios con roles de administrador
			if (!menu.isDisponible()) {
				rolesDelMenu = menu.getRolesAdministrador();
				rolesDelUsuario = usuario.getRolesAdministrados();
			} else {
				rolesDelMenu = menu.getRolesAcceso();
				rolesDelUsuario = usuario.getRoles();
			}
			
			if (rolesDelUsuario == null || rolesDelMenu == null) {
				if (registraErrores) {
					logger.logp(Level.SEVERE, nombreDeEstaClase, nombreEsteMetodo, mensajeNoAutorizado(usuario) + menu.getControlador() + "' (sin roles) ");
				}
				return ErroresPersonalizados.ERROR_PRIVILEGIOS;
			}
	
			while (!tieneAcceso && i < rolesDelUsuario.size()) {
				for (MenuRol menuRol : rolesDelMenu) {
					// si coincide y no está desactivado el rol o bien es administrador
					if (menuRol.getValorRol().equals(rolesDelUsuario.get(i)) && (!menuRol.isDesactivado() || menuRol.isAdministrador())) {
						tieneAcceso = true;
					}
				}
				i++;
			}
			
			if (!tieneAcceso) {
				if (registraErrores) {
					logger.logp(Level.SEVERE, nombreDeEstaClase, nombreEsteMetodo, mensajeNoAutorizado(usuario) + menu.getControlador() + "'");
				}
				return ErroresPersonalizados.ERROR_PRIVILEGIOS;
			}
		} else {
			if (url == null || url.startsWith(AyudaURL.PREFIJO_URL_PUBLICA)) {
				if (!menu.isAccesoAnonimo()) {
					if (registraErrores) {
						logger.logp(Level.SEVERE, nombreDeEstaClase, nombreEsteMetodo, "Acceso anónimo no autorizado al recurso '" 
								+ menu.getControlador() + "'");
					}
					return ErroresPersonalizados.ERROR_PRIVILEGIOS;
				} else {
					if (!menu.isDisponible()) {
						if (registraErrores) {
							logger.logp(Level.SEVERE, nombreDeEstaClase, nombreEsteMetodo, "Acceso anónimo al recurso '" 
									+ menu.getControlador() + "' que no está disponbile");
						}
						return ErroresPersonalizados.ERROR_PRIVILEGIOS;
					}
				}
			}
		}
			

		// Comprobación de la red
		tieneAcceso = false;
		i = 0;
		while (!tieneAcceso && i < subredesConAcceso.size()) {
			try {
				if (!subredesConAcceso.get(i).isDesactivado()) {
					tieneAcceso = compruebaIPEnSubred(subredesConAcceso.get(i).getRed(), direccionIPUsuario);
				}
			} catch (Exception e) {
				logger.logp(Level.FINEST, nombreDeEstaClase, "doFilter", "excepcion" + e.getLocalizedMessage());
			}
			i++;
		}
		
		if (!tieneAcceso) {
			if (registraErrores) {
				logger.logp(Level.SEVERE, nombreDeEstaClase, "doFilter", 
						mensajeNoAutorizado(usuario) + menu.getControlador() + "' por máscara de red desde la dirección IP " + direccionIPUsuario);
			}
			return ErroresPersonalizados.ERROR_IP_ACCESO_MENU;
		}
		
		return null;
	}
	
	/**
	 * Comprueba si un usuario tiene acceso a un elemento de menú.
	 * @param usuario usuario a comprobar
	 * @param menu menú a comprobar
	 * @param direccionIPUsuario dirección IP desde la que solicita el usuaro
	 * @param registraErrores registrar los errores con logger
	 * @return cadena de error o nulo si no existe error
	 */
	public static String errorAcceso(Usuario usuario, Menu menu, String direccionIPUsuario, boolean registraErrores) {
		return errorAcceso(usuario, menu, direccionIPUsuario, null, registraErrores);
	}
	

	/**
	 * Comprueba si la ip está en el rango de la subred.
	 * @param subred subred a comprobar en modo CIDR (ip/rango)
	 * @param ip ip a comprobar
	 * @return true si la ip esta en la subred
	 * @throws UnknownHostException si error de host
	 * 
	 * 	   desctivado java:S2183 para << 0 por claridad en el código
	 */
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:BooleanExpressionComplexity", "java:S2183"})
	private static boolean compruebaIPEnSubred(String subred, String ip) throws UnknownHostException {
		int bits = (subred.contains("/")) ? (Integer.parseInt(subred.substring(subred.indexOf('/') + 1))) : 32;
		if (bits == 0) {
			return true;
		}

		Inet4Address ipa = (Inet4Address) InetAddress.getByName(ip);
		byte[] ipb = ipa.getAddress();
		int iip = ((((int) ipb[0]) & 0xff) << 24) | ((((int) ipb[1]) & 0xff) << 16) | ((((int) ipb[2]) & 0xff) << 8) | ((((int) ipb[3]) & 0xff) << 0); 

		Inet4Address nma = (Inet4Address) InetAddress.getByName(subred.substring(0, subred.indexOf('/')));
		byte[] nmb = nma.getAddress();
		int inm = ((((int) nmb[0]) & 0xff) << 24) | ((((int) nmb[1]) & 0xff) << 16) | ((((int) nmb[2]) & 0xff) << 8) | ((((int) nmb[3]) & 0xff) << 0); 
		
		int mask = ~((1 << (32 - bits)) - 1);

		return (inm & mask) == (iip & mask);
		
	}

}
