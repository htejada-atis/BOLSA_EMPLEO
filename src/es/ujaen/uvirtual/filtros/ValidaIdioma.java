package es.ujaen.uvirtual.filtros;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.utilidades.AyudaURL;
import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.ErroresPersonalizados;
import es.ujaen.uvirtual.modelo.ModeloAdministracion;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Servlet Filter implementation class SrvValidator.
 */
@WebFilter(
	urlPatterns = {"/srv/*", "/pub/*" },
	filterName = "ValidaIdioma")
public class ValidaIdioma implements Filter {
	protected static FilterConfig filterConfig = null;
	protected static String codigoErrorBaseDatos = null;
	protected static String codigoErrorIdioma = null;
	
    /**
     * Default constructor. 
     */
    public ValidaIdioma() {
        // Auto-generated constructor stub
    }

	/** destroy.
	 * @see Filter#destroy()
	 */
	public void destroy() {
		filterConfig = null;
		codigoErrorIdioma = null;
	}

	/** filtro.
	 * @param request peticion
	 * @param response respuesta
	 * @param chain cadena de filtros
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings("checkstyle:ReturnCount")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String nombreMetodo = "doFilter";
		Logger logger = Logger.getLogger(ValidaIdioma.class.getPackage().getName());
		String thisClassName = ValidaIdioma.class.getName();
		
		// Comprobación del idioma
		String idioma = AyudaURL.obtenerIdioma(req.getRequestURI());

		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		boolean existeIdioma = false;
		try {
			String[] nombresDeIdiomas = modeloAdministracion.listaIdentificadoresDeIdiomas();
			for (int i = 0; i < nombresDeIdiomas.length; i++) {
				if (nombresDeIdiomas[i].equals(idioma)) {
					existeIdioma = true;
				}
			}
		} catch (Exception e) {
			logger.logp(Level.SEVERE, thisClassName, nombreMetodo, "Error de acceso a la base de datos: " + e.toString());
			req.getSession().setAttribute(ConfiguracionGlobal.getAtributoError(), codigoErrorBaseDatos);
			resp.sendRedirect(ConfiguracionGlobal.getUrlError());
			return;
		} 
		
		logger.logp(Level.FINEST, thisClassName, nombreMetodo, "idioma solicitado: " + idioma);
		
		
		if (!existeIdioma) {
			logger.logp(Level.SEVERE, thisClassName, nombreMetodo, "idiomas '" + idioma + "' no disponible, reenvío a página de error '" + codigoErrorIdioma + "'");
			req.getSession().setAttribute(ConfiguracionGlobal.getAtributoError(), ErroresPersonalizados.ERROR_IDIOMA);
			resp.sendRedirect(ConfiguracionGlobal.getUrlError());
			return;
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/** init.
	 * @param fConfig configuracion
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		//  Auto-generated method stub
		filterConfig = fConfig;
		codigoErrorIdioma = fConfig.getInitParameter("ErrorDeIdioma");
		codigoErrorBaseDatos = fConfig.getInitParameter("ErrorDeBD");

	}

}
