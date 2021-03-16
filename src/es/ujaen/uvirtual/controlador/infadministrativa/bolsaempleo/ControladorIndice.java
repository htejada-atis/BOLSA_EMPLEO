package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.uvirtual.bolsaempleo.Noticia;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaNoticiasCRUD;
import es.ujaen.uvirtual.modelo.bolsaempleo.ModeloNoticia;
import es.ujaen.uvirtual.utilidades.BolsaEmpleoUtils;
import es.ujaen.uvirtual.utilidades.EscapaHTML;


/**
 * Servlet implementation class ControladorIndice.
 */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo", 
		description = "Informacion bolsa empleo, raiz", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo", 
				"/srv/en/informacionadministrativa/bolsaempleo"
		})
public class ControladorIndice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String PARAM_ACCION = "a";
	
	// acciones
	public static final String ACCION_ACERCA_DE = "acercade";
	public static final String ACCION_AYUDA = "ayuda";
	public static final String ACCION_DOCUMENTOS = "documentos";
	public static final String ACCION_FAQ = "faq";
	public static final String ACCION_LISTAR_NOTICIAS = "listar_noticias";
	public static final String ACCION_LISTAR_TODAS_NOTICIAS = "listar_todas_noticias";
	
	// ruta vistas
	public static final String RUTA_BEP_INICIO = "/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/inicio/";

	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		
		VistaNoticiasCRUD bean = new VistaNoticiasCRUD();
		
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		if (nombreAccion == null) {
			nombreAccion = ACCION_LISTAR_NOTICIAS;
		}
		try {
			switch (nombreAccion) {
				case ACCION_ACERCA_DE:
					bean.setVista(RUTA_BEP_INICIO + "acercade.jsp");
					break;
				case ACCION_AYUDA:
					bean.setVista(RUTA_BEP_INICIO + "ayuda.jsp");
					break;
				case ACCION_DOCUMENTOS:
					bean.setVista(RUTA_BEP_INICIO + "documentos.jsp");
					break;
				case ACCION_FAQ:
					bean.setVista(RUTA_BEP_INICIO + "faq.jsp");
					break;
				case ACCION_LISTAR_NOTICIAS:
					obtenerNoticias(bean);
					break;
				case ACCION_LISTAR_TODAS_NOTICIAS:
					obtenerTodasNoticias(bean, request, response);
					return;
				default:
					obtenerNoticias(bean);
					break;
			}
		} catch (SQLException e) {
			bean.getMensajesDeError().add("Error al acceder a la base de datos");
		} finally {
			datos.getVistas().put(bean.getClass().getName(), bean);
			datos.getFicherosJSP().add(bean.getVista());
			datos.getFicherosJS().add("/js/jquery-1.latest.min.js");
			datos.getFicherosJS().add("/js/jquery-ui-1.10.4.min.js");
			datos.getFicherosJS().add("/js/jquery.ui.datepicker-es.js");
			datos.getFicherosJS().add("/js/bolsaempleo/utils.js");
			datos.getFicherosJS().add("/js/bolsaempleo/menu.js");
			datos.getFicherosCSS().add("/css/intranet.css");
			datos.getFicherosCSS().add("/css/jqueryujaen/jquery-ui-1.8.16.custom.css");
			datos.getFicherosCSS().add("/css/ujaen_bolsa_empleo.css");
		}
	}
	
	/** muestra todas las noticias.
	 * @param bean bean de la vista a la que poner los valores.
	 * @param request de la petición del servidor
	 * @param response de la respuesta del servidor
	 * @throws SQLException excepcion de bbdd.
	 */
	private void obtenerTodasNoticias(VistaNoticiasCRUD bean, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		ModeloNoticia modelo = new ModeloNoticia();
		
		bean.setVista(RUTA_BEP_INICIO + "inicio.jsp");
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter printWriter = response.getWriter();
		JSONObject json = new JSONObject();
		try {
			JSONArray ids = new JSONArray(request.getParameter("ids_noticias"));
			
			List<String> idsNoticias = new ArrayList<>();
			if (ids != null) {
				for (int i = 0; i < ids.length(); i++) {
					idsNoticias.add(ids.getString(0));
				}
			}
			
			List<Noticia> listaNoticias = modelo.listaNoticias(BolsaEmpleoUtils.consultaNotIn("CODNUM", idsNoticias) + " ORDER BY fecha");
			
			json.put("result", "ok");
			json.put("noticias", listaNoticias);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
        printWriter.print(json);
        printWriter.close();
	}
	
	/** muestra las 3 primeras noticias.
	 * @param bean bean de la vista a la que poner los valores.
	 * @throws SQLException excepcion de bbdd.
	 */
	private void obtenerNoticias(VistaNoticiasCRUD bean) throws SQLException {
		bean.setVista(RUTA_BEP_INICIO + "inicio.jsp");
		ModeloNoticia modelo = new ModeloNoticia();
		List<Noticia> noticias = modelo.listaNoticiasIniciales();
		bean.setNoticias(noticias);
	}
}
