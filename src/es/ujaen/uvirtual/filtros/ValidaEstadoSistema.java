package es.ujaen.uvirtual.filtros;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.ErroresPersonalizados;
import es.ujaen.uvirtual.beans.Sistema;
import es.ujaen.uvirtual.modelo.ModeloAdministracion;

/**
 * Servlet Filter implementation class ValidaEstadoSistema.
 */
@WebFilter(
		urlPatterns = { 
				"/srv/*", 
				"/pub/*"
		}, 
		filterName = "ValidaEstadoSistema")
public class ValidaEstadoSistema implements Filter {

    /**
     * Default constructor. 
     */
    public ValidaEstadoSistema() {
    	//vacio
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

		String atributoError = ConfiguracionGlobal.getAtributoError();
		// 20130605 - julopez - si se ha caído el sistema entero... a mantenimiento
		ConfiguracionGlobal.getAtributoContentType();
		if (!ConfiguracionGlobal.isDatosCargados()) {
			RequestDispatcher despachador = request.getRequestDispatcher("WEB-INF/jsp/vista/nodisponible.jsp");
			despachador.include(request, response);
			return;
		}
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		List<Sistema> sistemas = null;
		try {
			sistemas = modeloAdministracion.listaSistemas();
		} catch (SQLException e) {
			RequestDispatcher despachador = request.getRequestDispatcher("WEB-INF/jsp/vista/nodisponible.jsp");
			despachador.include(request, response);
			return;
		}
		if (sistemas != null) {
			for (Sistema sistema: sistemas) {
				String servNoDisponible = ErroresPersonalizados.ERROR_SERV_NO_DISPONIBLE + ":";
				// Buscamos "uv"
				if (sistema.getCodigo().equals("uvirtual")) {
					if (sistema.isEnMantenimiento() 
							&& (sistema.getFechaEntradaEnMantenimiento() == null || sistema.getFechaEntradaEnMantenimiento().before(new Date()))) {
						// Sistema en mantenimiento
						req.getSession().setAttribute(atributoError, servNoDisponible + sistema.getMotivoDelMantenimiento());
						resp.sendRedirect(ConfiguracionGlobal.getUrlSistemaNoDisponible());
						return;			
					} else if (sistema.isSinConexion()) {
						req.getSession().setAttribute(atributoError, servNoDisponible + "Actualmente no existe conexión con el sistema de información");
						resp.sendRedirect(ConfiguracionGlobal.getUrlSistemaNoDisponible());
						return;						
					}
					break;
				}
			}
		}
		
		chain.doFilter(request, response);
	}
}
