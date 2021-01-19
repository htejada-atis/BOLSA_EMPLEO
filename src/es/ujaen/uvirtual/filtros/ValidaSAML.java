package es.ujaen.uvirtual.filtros;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import es.ujaen.uvirtual.beans.Acceso;
import es.ujaen.uvirtual.beans.ConfiguracionGlobal;


/**
 * Servlet Filter implementation class SAMLValidator.
 * 20160211 - Stub para gestión sin acceso a un IdP
 */
@WebFilter(
	urlPatterns = "/sso",
	initParams = { 
		@WebInitParam(
				name = "UserAttribute", 
				value = "uid", 
				description = "Atributo que contiene el usuario (Equivalente a AtributoUsuario)"),
		@WebInitParam(
				name = "UserSessionAttribute", 
				value = "uid", 
				description = "Atributo de la sesión donde almacenar el usuario (Equivalente a AtributoUsuarioEnSesion)"),
		@WebInitParam(
				name = "ValidSessionKey", 
				value = "esValidaLaSesion", 
				description = "Atributo a definir cuando la sesión es válida (Equivalente a ClaveSesionValida)")
	},
	filterName = "ValidaSAML")
public class ValidaSAML implements Filter {
	public static final String PARAM_USUARIO = "usuario";
	
	protected FilterConfig configuracionFiltro = null;
	protected String userAttribute = null;
	protected String validSessionKey = null;
	protected String userSessionAttr = null;
	protected String destinationUrl = null;
	protected ServletContext contexto = null;

	/** filtro.
	 * @param request peticion
	 * @param response respuesta
	 * @param chain cadena de filtros
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// Comprueba que la sesión del usuario sea válida o
		// reenvía a la página de autenticación del SSO
		HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(true);
        String uidUsuario = req.getParameter(PARAM_USUARIO);
		if ((session.getAttribute(this.validSessionKey) == null) && (uidUsuario != null) && !"".equals(uidUsuario)) {
			uidUsuario = uidUsuario.trim();
			session.setAttribute(this.userSessionAttr, uidUsuario);
			session.setAttribute(this.validSessionKey, uidUsuario);
			
			// 20121009 - define el usuario que se ha logueado para que se registre el valor correcto.
			Acceso acceso = (Acceso) request.getAttribute(ConfiguracionGlobal.getAtributoLog());
			if (acceso != null) {
				acceso.setUsuario(uidUsuario);
			}
			request.setAttribute(ConfiguracionGlobal.getAtributoLog(), acceso);
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/** destroy.
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		this.configuracionFiltro = null;
		this.userAttribute = null;
		this.validSessionKey = null;
		this.userSessionAttr = null;
		this.destinationUrl = null;
	}

	/** init.
	 * @param fConfig configuracion
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		// Recupera los parámetros de configuración
		ConfiguracionGlobal.cargarDatos(false);
		this.configuracionFiltro = fConfig;
		this.userAttribute = fConfig.getInitParameter("UserAttribute");
		this.validSessionKey = fConfig.getInitParameter("ValidSessionKey");
		this.userSessionAttr = fConfig.getInitParameter("UserSessionAttribute");
		this.destinationUrl = ConfiguracionGlobal.getSamlUrlDestino();
		this.contexto = fConfig.getServletContext();
	}
}