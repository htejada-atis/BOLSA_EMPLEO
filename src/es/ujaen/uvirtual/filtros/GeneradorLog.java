package es.ujaen.uvirtual.filtros;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import es.ujaen.uvirtual.beans.Acceso;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.modelo.ModeloAdministracion;
import es.ujaen.uvirtual.utilidades.AyudaURL;

/**
 * Servlet Filter implementation class GeneradorLog.
 */
@WebFilter(
		urlPatterns = { 
				"/sso", "/srv/*", "/pub/*", "/error/*", "/json/*", "/salir", "/slo", "/ws/*"
		},
		dispatcherTypes = { javax.servlet.DispatcherType.REQUEST },
		filterName = "GeneradorLog")
public class GeneradorLog implements Filter {

	/**
	 * Default constructor. 
	 */
	public GeneradorLog() {
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		UVDatos datos = null;
		// ¿Por que nos llaman dos veces por petición???
		Acceso acceso = null;
		if (req.getAttribute(UVDatos.NOMBRE_ATRIBUTO) == null) {
			datos = new UVDatos();
			acceso = datos.getAcceso();
			acceso.setTiempo(System.currentTimeMillis());
			acceso.setExcepciones(new ArrayList<>());
			acceso.setServidor(java.net.InetAddress.getLocalHost().getHostAddress());
			acceso.setUrl(req.getRequestURI());
			acceso.setIp(req.getRemoteAddr());
			acceso.setSesion(req.getSession().getId());


			datos.setIdentificadorUsuario((String) req.getSession().getAttribute(UVDatos.ID_USUARIO_SESION));
			if (datos.getIdentificadorUsuario() != null) {
				datos.setUsuarioAutenticado(true);
				datos.getAcceso().setUsuario(datos.getIdentificadorUsuario());
			}
			req.setAttribute(UVDatos.NOMBRE_ATRIBUTO, datos);
			
			// 20161202 - Cuando entra en error debe poner el formato HTML
			if (req.getRequestURI().startsWith(AyudaURL.PREFIJO_URL_ERROR)) {
				datos.setFormatoSalida(AyudaURL.FORMATO_HTML);
			}
		}
		
		// pass the request along the filter chain
		try {
			chain.doFilter(request, response);
			datos = (UVDatos) req.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
			actualizaAcceso(datos, req);
			grabaAcceso(datos.getAcceso());
		} catch (Exception e) {
			// Si tenemos una excepción la capturamos y añadimos los parámetros recibidos
			datos = (UVDatos) req.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
			datos.getAcceso().getExcepciones().add(e);
			actualizaAcceso(datos, req);
			grabaAcceso(datos.getAcceso());
			throw new ServletException(e);
		}
	}

	/** init.
	 * @param fConfig configuracion
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		//
	}

	
	protected void actualizaAcceso(UVDatos datos, HttpServletRequest req) {
		Acceso acceso = datos.getAcceso();
		if (acceso.getTiempo() != 0) {
			acceso.setTiempo(System.currentTimeMillis() - acceso.getTiempo());
		}
		try {
			acceso.setUsuario(datos.getIdentificadorUsuario());
		} catch (IllegalStateException e) { /* Se ha invalidado la sesion */ }

		if (!acceso.getExcepciones().isEmpty()) {
			Enumeration<?> nombresParametros = req.getParameterNames();
			String parametros = null;
			String sep = "?";
			while (nombresParametros.hasMoreElements()) {
				String nombreParametro = (String) nombresParametros.nextElement();
				parametros = ((parametros == null) ? "" : parametros) + sep + nombreParametro + "=" + req.getParameter(nombreParametro);
				sep = "&";
			}
			acceso.setParametros(parametros);
			acceso.setRegistrar(true);
		}
	}
	
	protected void grabaAcceso(Acceso acceso) {
		if (acceso.isRegistrar()) {
			ModeloAdministracion modelo = ModeloAdministracion.obtenerInstancia();
			modelo.insertaAcceso(acceso);
		}
	}
}
