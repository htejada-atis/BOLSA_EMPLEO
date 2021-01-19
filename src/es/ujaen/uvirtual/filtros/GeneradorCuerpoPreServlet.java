package es.ujaen.uvirtual.filtros;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import es.ujaen.uvirtual.beans.ConfiguracionGlobal;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.ErroresPersonalizados;
import es.ujaen.uvirtual.beans.Menu;
import es.ujaen.uvirtual.beans.vistas.VistaUVirtual;
import es.ujaen.uvirtual.modelo.ModeloAdministracion;

/**
 * Servlet Filter implementation class GeneradorCuerpoPreServlet.
 * 
 * @author julopez
 */
@WebFilter(
	urlPatterns = { "/srv/*", "/pub/*", "/error/*" },
	initParams = { 
		@WebInitParam(name = "URLVista", value = "/WEB-INF/jsp/cuerpo_pre.jsp", description = "Url del fragmento con el contenido previo al cuerpo")
	},
	filterName = "GeneradorCuerpoPreServlet")
public class GeneradorCuerpoPreServlet implements Filter {
	protected static Logger logger = Logger.getLogger(GeneradorCuerpoPreServlet.class.getPackage().getName());
	protected static String nombreDeEstaClase = GeneradorCuerpoPreServlet.class.getName();
	protected static FilterConfig filterConfig = null;
	protected static String urlPreServlet = null;

	/**
	 * Default constructor. 
	 */
	public GeneradorCuerpoPreServlet() {
		// Auto-generated constructor stub
	}

	/** destroy.
	 * @see Filter#destroy()
	 */
	@Override
	public void destroy() {
		filterConfig = null;
		urlPreServlet = null;
	}

	/** filtro.
	 * @param request peticion
	 * @param response respuesta
	 * @param chain cadena de filtros
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings({"checkstyle:ExecutableStatementCount", "checkstyle:CyclomaticComplexity"})
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ModeloAdministracion modeloAdministracion = ModeloAdministracion.obtenerInstancia();
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		UVDatos datos = (UVDatos) req.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		VistaUVirtual vista = (VistaUVirtual) datos.getVistas().get(VistaUVirtual.class.getName());

		Menu menu = vista.getMenu();

		if ((menu != null) && (menu.isCrearMenu()) && (ConfiguracionGlobal.getParametroCadenaNE("ayudaurl.formatohtml").equals(datos.getFormatoSalida()))) {
			Map<Integer, Menu> menus = null;
			List<Menu> todosLosHijos = null;
			List<Menu> todosMismoNivel = null;
			ArrayList<Menu> menusHijos = new ArrayList<>();
			ArrayList<Menu> menusMismoNivel = new ArrayList<>();
			
			try {
				menus = modeloAdministracion.listaMenus();
				
				// recupera la información necesaria para la miga de pan
				vista.setMigaDePan(obtenerMigaDePan(menu, menus));
				
				// recuperamos los hijos del menú
				todosLosHijos = modeloAdministracion.listaMenuHijos(menu);
				// recuperamos los "hermanos" del menú
				if (menu.getCodigoPadre() >= 0) {
					Menu menuPadre = menus.get(menu.getCodigoPadre());
					if (menuPadre != null) {
						todosMismoNivel = modeloAdministracion.listaMenuHijos(menuPadre);
					}
				} 
				
				
				if (todosLosHijos != null) {
					for (Menu menuitem : todosLosHijos) {
						if ((CompruebaAcceso.acceso(datos.getUsuario(), menuitem, request.getRemoteAddr())) 
								&& (menuitem.isMostrar())) { 
							menusHijos.add(menuitem);
						}
					}
				}

				if (todosMismoNivel != null) {
					for (Menu menuitem : todosMismoNivel) {
						if ((CompruebaAcceso.acceso(datos.getUsuario(), menuitem, request.getRemoteAddr())) 
								&& (menuitem.isMostrar())) { 
							menusMismoNivel.add(menuitem);
						}
					}
				} else {
					// al mismo nivel como mínimo está el nodo
					menusMismoNivel.add(menu);
				}

				vista.setMenusHijos(menusHijos);
				vista.setMenusMismoNivel(menusMismoNivel);
				
			} catch (SQLException e) {
				logger.logp(Level.SEVERE, nombreDeEstaClase, "doFilter", "Error de acceso a la base de datos: " + e.toString());
				req.getSession().setAttribute(ConfiguracionGlobal.getParametroCadenaNE("administracion.atributoerror"), ErroresPersonalizados.ERROR_EN_BASEDEDATOS);
				resp.sendRedirect(ConfiguracionGlobal.getParametroCadenaNE("administracion.urlerror"));
				return;
			}
			
			
			datos.getFicherosJSP().add(urlPreServlet);
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
		filterConfig = fConfig;
		urlPreServlet = fConfig.getInitParameter("URLVista");
	}
	

	/**
	 * Obtiene la miga de pan de menús hasta llegar al actual de forma recursiva.
	 * @param menu menú actual del que obtener la miga de pan
	 * @param menus conjunto de todos los menús de la aplicación
	 * @return vector de menú con la miga de pan
	 */
	private ArrayList<Menu> obtenerMigaDePan(Menu menu, Map<Integer, Menu> menus) {
		// caso base: es el elemento raíz o no existe elemento padre
		if ((menu.getCodigo() == ConfiguracionGlobal.getParametroEnteroNE("administracion.codigomenuraiz")) || (!menus.containsKey(menu.getCodigoPadre()))) {
			ArrayList<Menu> resultado = new ArrayList<>();
			resultado.add(menu);
			return resultado;
		} else {
			Menu padre = menus.get(menu.getCodigoPadre());
			ArrayList<Menu> resultado = obtenerMigaDePan(padre, menus);
			resultado.add(menu);
			return resultado;
		}
	}

}
