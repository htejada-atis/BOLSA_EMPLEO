package es.ujaen.uvirtual.filtros;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.adm.CompruebaAcceso;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.Acceso;
import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.ErroresPersonalizados;
import es.ujaen.uvirtual.beans.Menu;
import es.ujaen.uvirtual.beans.Sistema;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.modelo.ModeloAdministracion;
import es.ujaen.uvirtual.utilidades.AyudaURL;


/**
 * Servlet Filter implementation class ACLValidator.
 */
@WebFilter(
	urlPatterns = { 
		"/sso", "/srv/*", "/pub/*", "/error/*", "/json/*", "/salir", "/slo", "/ws/*"
	},
	filterName = "ValidaAcceso")
public class ValidaAcceso implements Filter {
	protected static FilterConfig filterConfig = null;
	protected static Logger logger = Logger.getLogger(ValidaAcceso.class.getName());
	protected static String nombreDeEstaClase = ValidaAcceso.class.getName();
	
	/**
	 * Default constructor. 
	 */
	public ValidaAcceso() {
		// Auto-generated constructor stub
	}

	/** destroy.
	 * @see Filter#destroy()
	 */
	public void destroy() {
		logger = null;
		nombreDeEstaClase = null;
	}

	/**
	 * Filtro que verifica que el usuario pertenece a alguno de los roles que tienen permitido acceso a la acción.
	 * Que la acción esté disponible
	 * Que la URL desde la que se accede esté admitida
	 * @param request peticion
	 * @param response respuesta
	 * @param chain cadena de filtros
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity", "checkstyle:ReturnCount", 
			"checkstyle:JavaNCSS", "checkstyle:ExecutableStatementCount"})
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String atributroError = ConfiguracionGlobal.getParametroCadenaNE("administracion.atributoerror");

		String controlador = AyudaURL.obtenerControlador(req.getRequestURI());
		UVDatos datos = (UVDatos) req.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		
		String uid = datos.getIdentificadorUsuario();
		
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();

		Usuario usuario = null;
		Menu menu = null;
		String error = null;
		try {
			// Obtenemos la información de la acción
			menu = modeloAdministracion.listaMenuDeControlador(controlador);
			if (menu == null) {
				error = ErroresPersonalizados.ERROR_CONTROLADOR_NO_EXISTE;
				logger.logp(Level.SEVERE, nombreDeEstaClase, "doFilter", "No se han encontrado datos del controlador: " + controlador);
				throw new Exception("No se han encontrado datos del controlador");
			}
			
			// Obtenemos la información del usuario
			if (req.getRequestURI().startsWith(AyudaURL.PREFIJO_URL_AUTENTICADA)) {
				usuario = CrearUsuario.usuario(uid);
				if (usuario == null) { 
					error = ErroresPersonalizados.ERROR_USUARIO_NO_EXISTE;
					logger.logp(Level.SEVERE, nombreDeEstaClase, "doFilter", "No se han encontrado datos del usuario: " + uid);
					throw new Exception("No se han encontrado datos de usuario");
				}
			}
		
			// Almacenamos información de la acción y del usuario en la petición
			datos.setUsuario(usuario);
			datos.setMenu(menu);
			
		} catch (Exception e) {
			if (error == null) {
				error = ErroresPersonalizados.ERROR_EN_BASEDEDATOS;
				logger.logp(Level.SEVERE, nombreDeEstaClase, "doFilter", "Error de acceso a la base de datos: " + e.toString());
			}
			req.getSession().setAttribute(atributroError, error);
			resp.sendRedirect(ConfiguracionGlobal.getParametroCadenaNE("administracion.urlerror"));
			return;
		}

		String errorAcceso = CompruebaAcceso.errorAcceso(usuario, menu, req.getRemoteAddr(), req.getRequestURI(), true);
		if (errorAcceso != null) {
			req.getSession().setAttribute(atributroError, errorAcceso);
			resp.sendRedirect(ConfiguracionGlobal.getParametroCadenaNE("administracion.urlerror"));
			return;
		}

		// Define si se debe loguear el acceso
		Acceso acceso = datos.getAcceso();
		if (acceso != null) {
			acceso.setRegistrar(menu.isRegistrarLog());
		}
		
		// Comprobamos si el sistema está disponible
		if (menu.getSistemas() != null) {
			List<Sistema> sistemas = ModeloAdministracion.listaEstadoSistemas();
			if (sistemas == null) {
				try {
					sistemas = modeloAdministracion.listaSistemas();
				} catch (Exception e) {
					// No tenemos acceso a la lista de sistemas ... uv está caído
					req.getSession().setAttribute(atributroError, ErroresPersonalizados.ERROR_UV_NO_DISPONIBLE);
					resp.sendRedirect(ConfiguracionGlobal.getParametroCadenaNE("administracion.sistemanodisponible"));
					return;
				}
			}
			for (Sistema sistema : sistemas) {
				if (menu.getSistemas().contains(sistema.getCodigo())) {
					String servNoDospinible = ErroresPersonalizados.ERROR_SERV_NO_DISPONIBLE + ":";
					if (sistema.isEnMantenimiento() && (sistema.getFechaEntradaEnMantenimiento() == null)) {
						// Sistema en mantenimiento
						req.getSession().setAttribute(atributroError, servNoDospinible + sistema.getMotivoDelMantenimiento());
						resp.sendRedirect(ConfiguracionGlobal.getParametroCadenaNE("administracion.sistemanodisponible"));
						return;
					} else if (sistema.isEnMantenimiento() && (sistema.getFechaEntradaEnMantenimiento().before(new Date()))) {
						// Sistema entra en mantenimiento y ya ha llegado la fecha
						req.getSession().setAttribute(atributroError, servNoDospinible + sistema.getMotivoDelMantenimiento());
						resp.sendRedirect(ConfiguracionGlobal.getParametroCadenaNE("administracion.sistemanodisponible"));
						return;
					} else if (sistema.isSinConexion()) {
						req.getSession().setAttribute(atributroError, servNoDospinible + "Actualmente no existe conexión con el sistema de información");
						resp.sendRedirect(ConfiguracionGlobal.getParametroCadenaNE("administracion.sistemanodisponible"));
						return;
					}
				}
			}
			
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}


	
	/** init.
	 * @param fConfig configuracion
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		filterConfig = fConfig;
	}

}
