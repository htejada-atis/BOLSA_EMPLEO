package es.ujaen.uvirtual.filtros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.utilidades.AyudaURL;

/**
 * Servlet Filter implementation class GeneradorPDF.
 */
@WebFilter(
		urlPatterns = {"/srv/es/*", "/pub/es/*", "/srv/en/*", "/pub/en/*"},
		filterName = "GeneradorPDF")
public class GeneradorPDF implements Filter {
	private HttpServletRequest req;
	private HttpServletResponse resp;
	protected FilterConfig filterConfig = null;
	public static final String ELEMENTO_URL = "generadorPdf:url:";
	public static final String ELEMENTO_URI = "generadorPdf:uri:";
	public static final String ELEMENTO_QUERY = "generadorPdf:query:";

	/**
	 * Default constructor. 
	 */
	public GeneradorPDF() {
		//vacio
	}

	/** destroy.
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		//
	}

	/** filtro.
	 * @param request peticion
	 * @param response respuesta
	 * @param chain cadena de filtros
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		req = (HttpServletRequest) request;
		resp = (HttpServletResponse) response;
		UVDatos datos = (UVDatos) req.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		HashMap<Long, Map<String, String[]>> parametros = (HashMap<Long, Map<String, String[]>>) req.getSession().getAttribute("filtropdf");
		
		datos.setFormatoSalida(AyudaURL.obtenerFormato(req.getRequestURI()));

		long tiempoActual = System.currentTimeMillis();
		long difTiempo = ConfiguracionGlobal.getParametroEnteroLargoNE("administracion.validezUrls");
		if (parametros == null) {
			parametros = new HashMap<>();
		}
		Iterator<Long> it = parametros.keySet().iterator();
		ArrayList<Long> datosABorrar = new ArrayList<>();
		while (it.hasNext()) {
			Long valor = it.next();
			if (tiempoActual > valor.longValue() + difTiempo) {
				// no podemos eliminar aquí por que generaríamos una excepción por acceso concurrente, añadir a una lista y borrar fuera de aquí
				datosABorrar.add(valor);
			}
		}
		for (Long valor : datosABorrar) {
			parametros.remove(valor);
		}
		HashMap<String, String[]> params = new HashMap<>(req.getParameterMap());
		params.put(ELEMENTO_URL, new String[] {req.getRequestURL().toString()});
		params.put(ELEMENTO_URI, new String[] {req.getRequestURI()});
		params.put(ELEMENTO_QUERY, new String[] {req.getQueryString()});
		parametros.put(Long.valueOf(tiempoActual), params);
		req.getSession().setAttribute("filtropdf", parametros);
		datos.setIdentificadorPeticion(tiempoActual);
		chain.doFilter(request, response);
	}

	/** init.
	 * @param fConfig configuracion
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		filterConfig = fConfig;
	}
	
	public HttpServletRequest getRequest() {
		return req;
	}

	public HttpServletResponse getResponse() {
		return resp;
	}
}
