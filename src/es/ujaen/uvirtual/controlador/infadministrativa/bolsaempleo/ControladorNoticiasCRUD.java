package es.ujaen.uvirtual.controlador.infadministrativa.bolsaempleo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.ujaen.uvirtual.beans.UVDatos;
import es.ujaen.uvirtual.beans.Usuario;
import es.ujaen.uvirtual.beans.vistas.uvirtual.bolsaempleo.VistaNoticiasCRUD;
import es.ujaen.uvirtual.utilidades.EscapaHTML;


/** Clase controlador para obtener, cambiar, eliminar y agregar convocatorias.
 * Controlador - Opers. con nombres: obtener,  cambiar,    eliminar, agregar
 * */
@WebServlet(
		name = "informacionadministrativa.bolsaempleo.noticiascrud", 
		description = "Noticias", 
		urlPatterns = { 
				"/srv/es/informacionadministrativa/bolsaempleo/noticiascrud", 
				"/srv/en/informacionadministrativa/bolsaempleo/noticiascrud"
		})
public class ControladorNoticiasCRUD extends HttpServlet {
	//private static final long serialVersionUID = 1L;
	private static final String NOMBREDEESTACLASE = ControladorNoticiasCRUD.class.getName();
	private static final Logger LOGGER = Logger.getLogger(NOMBREDEESTACLASE);
	public static final String PARAM_ACCION = "a";
	
	/** Peticion GET.
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UVDatos datos = (UVDatos) request.getAttribute(UVDatos.NOMBRE_ATRIBUTO);
		datos.setDocType("<!DOCTYPE html>");
		datos.setContentType("text/html");
		
		VistaNoticiasCRUD bean = new VistaNoticiasCRUD();
		
		//ejemplo para obtener el usuario
		Usuario usuario = datos.getUsuario();
		LOGGER.log(Level.FINEST, "usuario que ha entrado en el servlet es {0}", usuario.getUid());
		
		datos.getVistas().put(bean.getClass().getName(), bean);
		datos.getFicherosJSP().add("/WEB-INF/jsp/vista/infadministrativa/bolsaempleo/indice.jsp");
		String nombreAccion = EscapaHTML.ajustaCodificacion(request.getParameter(PARAM_ACCION));
		datos.getFicherosCSS().add("/css/intranet.css");
	}
}
