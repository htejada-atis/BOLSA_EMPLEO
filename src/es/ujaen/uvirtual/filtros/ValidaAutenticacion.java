package es.ujaen.uvirtual.filtros;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.ujaen.uvirtual.beans.Acceso;
import es.ujaen.uvirtual.beans.ConfiguracionGlobal;

/**
 * Servlet Filter implementation class AuthenticatedUser.
 */
@WebFilter(
	urlPatterns = {"/sso", "/srv/*" },
	initParams = { 
		@WebInitParam(name = "UrlSSO", value = "obsoleto", description = "Url de autenticación en el SSO"),
		@WebInitParam(name = "ClaveSesionValida", value = "esValidaLaSesion", description = "Clave que debe existir en la sesión del usuario para que se considere válida"),
		@WebInitParam(name = "UrlDeEntrada", value = "urlDeEntrada", description = "Parámetro de sesión con la URL a reenviar después del SSO")
	},
	filterName = "ValidaAutenticacion")
public class ValidaAutenticacion implements Filter {

	protected FilterConfig filterConfig = null;
	protected String urlAutenticacionUnica = null;
	protected String esValidaLaSesion = null;
	protected String urlDeEntrada = null;
	
    /**
     * Default constructor. 
     */
    public ValidaAutenticacion() {
        // Auto-generated constructor stub
    }

	/** destroy.
	 * @see Filter#destroy()
	 */
    @Override
	public void destroy() {
		this.filterConfig = null;
    	this.urlAutenticacionUnica = null;
    	this.urlDeEntrada = null;
    	this.esValidaLaSesion = null;
	}

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
		HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
		if (session.getAttribute(this.esValidaLaSesion) == null) {
			Enumeration<?> nombresParametros = request.getParameterNames();
			String parametros = null;
			String sep = "?";
			while (nombresParametros.hasMoreElements()) {
				String nombreParametro = (String) nombresParametros.nextElement();
				parametros = ((parametros == null) ? "" : parametros) + sep + nombreParametro + "=" + request.getParameter(nombreParametro);
				sep = "&";
			}
				
			session.setAttribute(this.urlDeEntrada, req.getRequestURI() + ((parametros == null) ? "" : parametros));
			
			// 20121009 - julopez - no registra el intento de acceso previo a la redirección
			Acceso acceso = (Acceso) req.getAttribute(ConfiguracionGlobal.getAtributoLog());
			if (acceso != null) {
				acceso.setRegistrar(false);
			}
			req.setAttribute(ConfiguracionGlobal.getAtributoLog(), acceso);
			resp.sendRedirect(this.urlAutenticacionUnica);
		} else {
			String urlPrevia = (String) session.getAttribute(this.urlDeEntrada);
			if (urlPrevia != null) {
				session.removeAttribute(this.urlDeEntrada);
				resp.sendRedirect(urlPrevia);
				return;
			}
			//pass the request along the filter chain
			chain.doFilter(request, response);
		}
	}

	/** init.
	 * @param fConfig configuracion
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		// Recupera los parámetros de configuración
		ConfiguracionGlobal.cargarDatos(false);
		this.filterConfig = fConfig;
		this.esValidaLaSesion = fConfig.getInitParameter("ClaveSesionValida");
		this.urlDeEntrada = fConfig.getInitParameter("UrlDeEntrada");
		this.urlAutenticacionUnica = ConfiguracionGlobal.getSamlUrlAutenticacion();
	}

}
