package es.ujaen.uvirtual.filtros;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.vistas.VistaUVirtual;
import es.ujaen.uvirtual.utilidades.AyudaURL;


/**
 * Servlet Filter que obtiene la información a mostrar previo a la ejecución del servlet.
 */
@WebFilter(
		urlPatterns = { "/srv/*", "/pub/*", "/error/*" },
		filterName = "GeneradorContenido")
public class GeneradorContenido implements Filter {
	protected static FilterConfig filterConfig = null;
	private static String nombreDeEstaClase = GeneradorContenido.class.getName();
    private static final Logger ELOGGER = Logger.getLogger(GeneradorContenido.class.getPackage().getName());
	
    /**
     * Default constructor. 
     */
    public GeneradorContenido() {
        //  Auto-generated constructor stub
    }

	/** destroy.
	 * @see Filter#destroy()
	 */
    @Override
	public void destroy() {
		//  Auto-generated method stub
		filterConfig = null;
	}

	/** filtro.
	 * @param request peticion
	 * @param response respuesta
	 * @param chain cadena de filtros
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity", "checkstyle:ReturnCount", 
		"checkstyle:JavaNCSS", "checkstyle:ExecutableStatementCount"})
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// Inicializa los vectores de vistas, javascript y css
		HttpServletRequest req = (HttpServletRequest) request;
		UVDatos datos = (UVDatos) req.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		VistaUVirtual vista = new VistaUVirtual();
		datos.getVistas().put(VistaUVirtual.class.getName(), vista);

		try {
			// Hack para IE inferior a 9
			String valoresAccept = ((HttpServletRequest) request).getHeader("accept");
			if (valoresAccept != null && valoresAccept.indexOf("application/xhtml+xml") >= 0) {
				datos.setContentType("application/xhtml+xml");
			} else {
				datos.setContentType("text/html");
			}
		} catch (Exception ex) {
			datos.setContentType("text/html");
		}

			
		// Ejecuta la cadena
		chain.doFilter(request, response);
		
		// Comprobamos si se ha mandado ya la respuesta, para no tener que mandar nada más
		if (!datos.isRespuestaEnviada()) {
			String lang = AyudaURL.obtenerIdioma(req.getRequestURI());
			
			// Genera las vistas que nos han indicado
			// 20151028 - julopez - elimina el modo compatibilidad en iexplorer
			((HttpServletResponse) response).addHeader("X-UA-Compatible", "IE=edge");
			
			response.setContentType(datos.getContentType());
			
			
			// Por defecto ... si no tenemos los js se añade jquery (requerido por) uvirtual.js
			if (datos.getContentType() != null && datos.getContentType().indexOf("html") >= 0) {
				boolean estaJQuery = false;
				for (String nombreFicheroJS : datos.getFicherosJS()) {
					if (nombreFicheroJS.indexOf("jquery-1.") >= 0) {
						estaJQuery = true;
						break;
					}
				}
				if (!estaJQuery) {
					datos.getFicherosJS().add("/js/jquery-1.latest.min.js");
				}
			}
			
			RequestDispatcher despachador = null;
			for (String nombreFichero :datos.getFicherosJSP()) {
				try {
					int posExtension = nombreFichero.lastIndexOf('.');
					String nombreLocalizado = nombreFichero.substring(0, posExtension) + "." + lang + nombreFichero.substring(posExtension);
					File fichero = new File(filterConfig.getServletContext().getRealPath(nombreLocalizado));
					if (fichero.exists()) { 
						despachador = request.getRequestDispatcher(nombreLocalizado);
						despachador.include(request, response);
					} else {
						despachador = request.getRequestDispatcher(nombreFichero);
						despachador.include(request, response);
					}
				} catch (Exception e) {
					if (e.toString().indexOf("getOutputStream") >= 0) {
						ELOGGER.logp(Level.SEVERE, nombreDeEstaClase, "doFilter", 
								"Error generando vista '" + nombreFichero + "' debido a que se ha enviado ya la respuesta al usuario");
						break;
					}
					ELOGGER.logp(Level.SEVERE, nombreDeEstaClase, "doFilter", "Error generando vista '" + nombreFichero + "': " + e.toString());
				}
			}
		}
	}

	/** init.
	 * @param fConfig configuracion
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		filterConfig = fConfig;
	}

}
