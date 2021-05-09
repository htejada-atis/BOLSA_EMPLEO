package es.ujaen.uvirtual.filtros;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.ujaen.uvirtual.adm.CompruebaAcceso;
import es.ujaen.uvirtual.adm.CrearUsuario;
import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.ErroresPersonalizados;
import es.ujaen.uvirtual.beans.Menu;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.vistas.VistaUVirtual;
import es.ujaen.uvirtual.modelo.ModeloAdministracion;
import es.ujaen.uvirtual.utilidades.AyudaURL;

/**
 * Servlet Filter implementation class GeneradorCabecera.
 * @author julopez
 */
@WebFilter(
	urlPatterns = { "/srv/*", "/pub/*", "/error/*" },
	initParams = { 
		@WebInitParam(name = "URLVista", value = "/WEB-INF/jsp/cabecera.jsp", description = "Url del fragmento con la cabecera")
	},
	filterName = "GeneradorCabecera")
public class GeneradorCabecera implements Filter {
	protected static Logger logger = Logger.getLogger(GeneradorCabecera.class.getPackage().getName());
	protected static String nombreDeEstaClase = GeneradorCabecera.class.getName();
	protected FilterConfig filterConfig = null;
	protected String urlCabecera = null;

    /**
     * Default constructor. 
     */
    public GeneradorCabecera() {
        // Auto-generated constructor stub
    }

	/** destroy.
	 * @see Filter#destroy()
	 */
    @Override
	public void destroy() {
		filterConfig = null;
		urlCabecera = null;
	}

	/** filtro.
	 * @param request peticion
	 * @param response respuesta
	 * @param chain cadena de filtros
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings({"checkstyle:CyclomaticComplexity", "checkstyle:NPathComplexity", 
		"checkstyle:JavaNCSS", "checkstyle:ExecutableStatementCount"})
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		UVDatos datos = (UVDatos) req.getAttribute(UVDatos.NOMBRE_ATRIBUTO);

		VistaUVirtual vista = (VistaUVirtual) datos.getVistas().get(VistaUVirtual.class.getName());

		String uid = datos.getIdentificadorUsuario();

		String controlador = AyudaURL.obtenerControlador(req.getRequestURI());
		if (uid == null) {
			uid = (String) req.getSession().getAttribute(ConfiguracionGlobal.getAtributoUsuario());
		}

		Menu menu = null;
		List<Menu> menuRaiz = null;
		int avisosSinLeer = 0;
		try {
			menu = modeloAdministracion.listaMenuDeControlador(controlador);
			menuRaiz = modeloAdministracion.listaMenuRaiz();
		} catch (SQLException e) {
			logger.logp(Level.SEVERE, nombreDeEstaClase, "doFilter", "Error de acceso a la base de datos: " + e.toString());
			req.getSession().setAttribute(ConfiguracionGlobal.getAtributoError(), ErroresPersonalizados.ERROR_EN_BASEDEDATOS);
			resp.sendRedirect(ConfiguracionGlobal.getUrlError());
			return;
		}

		if ((menu != null) && (menu.isCrearMenu())) {
			Usuario usuario = null;
			if (uid != null) {
				usuario = CrearUsuario.usuario(uid);
			}
			// recuperar la información necesaria para la cabecera e incluirla en el request
			
			// Filtramos el menú raíz en función de los permisos
			ArrayList<Menu> menuUsuario = new ArrayList<>();
			for (Menu menuitem : menuRaiz) {
				boolean tieneAcceso = CompruebaAcceso.acceso(usuario, menuitem, request.getRemoteAddr());

				if (tieneAcceso && (menuitem.isMostrar())) {
					menuUsuario.add(menuitem);
				}
			}

			vista.setIdiomas(new HashMap<>(menu.getIdiomas()));
			vista.setUsuario(usuario);
			vista.setMenuPrincipal(menuUsuario);
			vista.setMenu(menu);
			vista.setAvisosSinLeer(avisosSinLeer);
			vista.setPaginaInicio(ConfiguracionGlobal.getParametroCadenaNE("administracion.urlraiz"));
			if (req.getSession().getAttribute(ConfiguracionGlobal.getParametroCadenaNE("administracion.atributousuarioreal")) != null) {
				String idUsuario = (String) req.getSession().getAttribute(ConfiguracionGlobal.getParametroCadenaNE("administracion.atributousuarioreal"));
				vista.setIdentificadorUsuarioAutenticado(idUsuario);
			}

			datos.getFicherosJSP().add(urlCabecera);
		}
		// Continuar el filtro
		chain.doFilter(request, response);

	}

	/** init.
	 * @param fConfig configuracion
	 * @see Filter#init(FilterConfig)
	 */
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		filterConfig = fConfig;
		urlCabecera = fConfig.getInitParameter("URLVista");
	}

}
