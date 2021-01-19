package es.ujaen.uvirtual.filtros;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;

import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Menu;

/**
 * Servlet Filter que obtiene la información a mostrar tras ejecutar el servlet.
 *   
 * 
 * @author julopez
 * 
 */
@WebFilter(
	urlPatterns = { "/srv/*", "/pub/*", "/error/*" },
	initParams = { 
		@WebInitParam(name = "URLVista", value = "/WEB-INF/jsp/cuerpo_post.jsp", description = "Url del fragmento con el contenido posterior al cuerpo")
	},
	filterName = "GeneradorCuerpoPostServlet")
public class GeneradorCuerpoPostServlet implements Filter {
	protected static Logger logger = Logger.getLogger(GeneradorCuerpoPostServlet.class.getPackage().getName());
	protected static String nombreDeEstaClase = GeneradorCuerpoPostServlet.class.getName();
	protected static FilterConfig filterConfig = null;
	protected static String urlPostServlet = null;

	/**
	 * Default constructor. 
	 */
	public GeneradorCuerpoPostServlet() {
		// Auto-generated constructor stub
	}

	/** destroy.
	 * @see Filter#destroy()
	 */
	public void destroy() {
		filterConfig = null;
		urlPostServlet = null;
	}

	/** filtro.
	 * @param request peticion
	 * @param response respuesta
	 * @param chain cadena de filtros
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// pass the request along the filter chain
		chain.doFilter(request, response);

		
		HttpServletRequest req = (HttpServletRequest) request;
		UVDatos datos = (UVDatos) req.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		
		Menu menu = datos.getMenu();
		if ((menu != null) && (menu.isCrearMenu()) && (ConfiguracionGlobal.getParametroCadenaNE("ayudaurl.formatohtml").equals(datos.getFormatoSalida()))) {
			datos.getFicherosJSP().add(urlPostServlet);
		}


	}

	/** init.
	 * @param fConfig configuracion
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		filterConfig = fConfig;
		urlPostServlet = fConfig.getInitParameter("URLVista");
	}

}
